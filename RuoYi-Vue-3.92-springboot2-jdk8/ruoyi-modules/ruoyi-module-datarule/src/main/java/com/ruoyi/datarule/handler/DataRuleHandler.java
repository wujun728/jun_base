package com.ruoyi.datarule.handler;

import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.datarule.model.DataRuleModel;
import com.ruoyi.datarule.model.RoleDataRuleModel;
import com.ruoyi.datarule.model.TreeEntityModel;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class DataRuleHandler {


    HashMap<String, List<String>> roleDataRuleModelHashMap = new HashMap<>();
    HashMap<String, DataRuleModel> dataRuleModelHashMap = new HashMap<>();
    HashMap<String, Map<String, Object>> userModelHashMap = new HashMap<>();
    HashMap<String, List<TreeEntityModel>> treeEntityModelMap = new HashMap<>();

    private final JdbcTemplate jdbcTemplate;

    public void init() {
        List<DataRuleModel> list = jdbcTemplate.query("select * from sys_data_rule where del_flag = ?", new Object[]{0}, new BeanPropertyRowMapper<>(DataRuleModel.class));
        list.forEach(item -> {
            dataRuleModelHashMap.put(item.getScopeClass(), item);
            if (StringUtils.isEmpty(item.getTableName())) {
                return;
            }
            List<TreeEntityModel> treeEntityModelList = jdbcTemplate.query(String.format("select * from %s where del_flag = ?", item.getTableName()), new Object[]{0}, new BeanPropertyRowMapper<>(TreeEntityModel.class));
            treeEntityModelMap.put(item.getTableName(), treeEntityModelList);
        });

        refreshRole();
    }

    public DataRuleModel getDataRuleModel(String mapperId) {
        if (dataRuleModelHashMap.containsKey(mapperId)) {
            return dataRuleModelHashMap.get(mapperId);
        }
        return null;
    }

    public List<String> getChildren(String tableName, String id) {
        if (!treeEntityModelMap.containsKey(tableName)) {
            List<TreeEntityModel> treeEntityModelList = jdbcTemplate.query(String.format("select * from %s where del_flag = ?", tableName), new Object[]{0}, new BeanPropertyRowMapper<>(TreeEntityModel.class));
            treeEntityModelMap.put(tableName, treeEntityModelList);
        }
        List<String> idList = new ArrayList<>();
        List<TreeEntityModel> treeEntityModelList = treeEntityModelMap.get(tableName);
        treeEntityModelList.forEach(item -> {
            if (item.getId().equals(id) || (!StringUtils.isEmpty(item.getAncestors()) && item.getAncestors().contains(id + ","))) {
                idList.add(item.getId());
            }
        });
        return idList;
    }


    public void refreshTreeEntity(String tableName) {
        List<TreeEntityModel> treeEntityModelList = jdbcTemplate.query(String.format("select * from %s where del_flag = ?", tableName), new Object[]{0}, new BeanPropertyRowMapper<>(TreeEntityModel.class));
        treeEntityModelMap.put(tableName, treeEntityModelList);
    }

    public void refreshRole() {
        List<RoleDataRuleModel> roleDataRuleModelList = jdbcTemplate.query("select * from sys_role_data_rule", new Object[]{}, new BeanPropertyRowMapper<>(RoleDataRuleModel.class));
        roleDataRuleModelHashMap.clear();
        roleDataRuleModelList.forEach(item -> {
            if (!roleDataRuleModelHashMap.containsKey(item.getRoleId())) {
                roleDataRuleModelHashMap.put(item.getRoleId(), new ArrayList<>());
            }
            roleDataRuleModelHashMap.get(item.getRoleId()).add(item.getDataRuleId());
        });
    }

    public void refreshDataRule(String id) {
        DataRuleModel dataRuleModel = jdbcTemplate.queryForObject("select * from sys_data_rule where id = ?", new Object[]{id}, new BeanPropertyRowMapper<>(DataRuleModel.class));
        for (Map.Entry<String, DataRuleModel> entry : dataRuleModelHashMap.entrySet()) {
            if (entry.getValue().getId().equals(id)) {
                dataRuleModelHashMap.remove(entry.getKey());
                break;
            }
        }
        dataRuleModelHashMap.put(dataRuleModel.getScopeClass(), dataRuleModel);
        if (StringUtils.isEmpty(dataRuleModel.getTableName())) {
            return;
        }
        List<TreeEntityModel> treeEntityModelList = jdbcTemplate.query(String.format("select * from %s where del_flag = ?", dataRuleModel.getTableName()), new Object[]{0}, new BeanPropertyRowMapper<>(TreeEntityModel.class));
        treeEntityModelMap.put(dataRuleModel.getTableName(), treeEntityModelList);

    }

    public void deleteDataRule(String id) {
        for (Map.Entry<String, DataRuleModel> entry : dataRuleModelHashMap.entrySet()) {
            if (entry.getValue().getId().equals(id)) {
                dataRuleModelHashMap.remove(entry.getKey());
                break;
            }
        }
    }

    public DataRuleModel getDataRule(String mapperId, List<SysRole> roleList) {
        for (SysRole sysRole : roleList) {
            String roleId = sysRole.getRoleId().toString();
            DataRuleModel dataRuleModel = getDataRuleModel(mapperId);
            if (dataRuleModel == null) {
                return null;
            }
            if (!roleDataRuleModelHashMap.containsKey(roleId)) {
                //如果找不到，刷新角色权限
                refreshRole();
                //如果还不找不到，返回null
                if (!roleDataRuleModelHashMap.containsKey(roleId)) {
                    continue;
                }
            }
            boolean isMatch = roleDataRuleModelHashMap.get(roleId).stream().anyMatch(dataRuleModel.getId()::contains);
            if (isMatch) {
                return dataRuleModel;
            }
        }

        return null;

    }

    public void refreshUser(String id) {
        SysUser userModel = jdbcTemplate.queryForObject("select * from sys_user where user_id = ?", new Object[]{id}, new BeanPropertyRowMapper<>(SysUser.class));
        try {
            Map<String, Object> map = BeanUtils.convertBean(userModel);
            userModelHashMap.put(id, map);
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Object> getUser(Long id) {
        if (!userModelHashMap.containsKey(id)) {
            refreshUser("" + id);
        }
        if (userModelHashMap.containsKey(id)) {
            return userModelHashMap.get(id);
        }
        return null;
    }
}
