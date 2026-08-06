package com.ruoyi.online.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.framework.web.service.PermissionService;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.online.domain.OnlineMb;
import com.ruoyi.online.service.IOnlineMbService;
import com.ruoyi.online.utils.SqlMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 在线接口
 * 
 * @author Dftre
 * @date 2024-01-26
 */
@RestController
@Anonymous
@RequestMapping("/online")
public class OnLineController extends BaseController {
    @Autowired
    private IOnlineMbService onlineMbService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @SuppressWarnings("unchecked")
    public Map<String, Object> getParams(HashMap<String, Object> params, HashMap<String, Object> data) {
        Map<String, Object> object = new HashMap<>();
        HashMap<String, Object> object_params = new HashMap<String, Object>();
        String keyregex = "params\\[(.*?)\\]";
        Pattern pattern = Pattern.compile(keyregex);
        if (params != null) {
            params.keySet().forEach(key -> {
                Matcher matcher = pattern.matcher(key);
                if (matcher.find()) {
                    object_params.put(matcher.group(1), params.get(key));
                } else {
                    object.put(key, params.get(key));
                }
            });
        }
        if (data != null) {
            if (data.containsKey("params")) {
                object_params.putAll((HashMap<String, Object>) data.get("params"));
                data.remove("params");
            }
            object.putAll(data);
        }
        object.put("params", object_params);
        return object;
    }

    public Boolean checkPermission(String permissionType, String permissionValue) {
        if (permissionType == null) {
            return true;
        }
        return switch (permissionType) {
            case "hasPermi" -> permissionService.hasPermi(permissionValue);
            case "lacksPermi" -> permissionService.lacksPermi(permissionValue);
            case "hasAnyPermi" -> permissionService.hasAnyPermi(permissionValue);
            case "hasRole" -> permissionService.hasRole(permissionValue);
            case "lacksRole" -> permissionService.lacksRole(permissionValue);
            case "hasAnyRoles" -> permissionService.hasAnyRoles(permissionValue);
            default -> true;
        };
    }

    public Object processingMapper(String sqlContext, String actuatot, Map<String, Object> params) {
        String sql = "<script>\n" + sqlContext + "\n</script>";
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            SqlMapper sqlMapper = new SqlMapper(sqlSession);
            Object res = null;
            res = switch (actuatot) {
                case "selectList" -> getDataTable(sqlMapper.selectList(sql, params));
                case "insert" -> toAjax(sqlMapper.insert(sql, params));
                case "selectOne" -> success(sqlMapper.selectOne(sql, params));
                case "update" -> toAjax(sqlMapper.update(sql, params));
                case "delete" -> toAjax(sqlMapper.delete(sql, params));
                default -> AjaxResult.error(500, "系统错误，执行器错误");
            };
            return res;
        } finally {
            sqlSession.close();
        }

    }

    @RequestMapping("/api/**")
    public Object api(@RequestParam(required = false) HashMap<String, Object> params,
            @RequestBody(required = false) HashMap<String, Object> data, HttpServletRequest request,
            HttpServletResponse response) {
        OnlineMb selectOnlineMb = new OnlineMb();
        selectOnlineMb.setPath(request.getRequestURI().replace("/online/api", ""));
        selectOnlineMb.setMethod(request.getMethod());

        Map<String, Object> object = getParams(params, data);

        List<OnlineMb> selectOnlineMbList = onlineMbService.selectOnlineMbList(selectOnlineMb);
        if (selectOnlineMbList.size() == 0) {
            return AjaxResult.error("没有资源" + selectOnlineMb.getPath());
        } else if (selectOnlineMbList.size() > 1) {
            return AjaxResult.error(500, "系统错误，在线接口重复");
        } else {
            OnlineMb onlineMb = selectOnlineMbList.get(0);
            if (!checkPermission(onlineMb.getPermissionType(), onlineMb.getPermissionValue())) {
                return AjaxResult.error(403, "没有权限，请联系管理员授权");
            }
            if (onlineMb.getDeptId() != null && onlineMb.getDeptId().equals("1")) {
                object.put("deptId", SecurityUtils.getDeptId());
            }
            if (onlineMb.getUserId() != null && onlineMb.getUserId().equals("1")) {
                object.put("userId", SecurityUtils.getUserId());
            }
            return processingMapper(onlineMb.getSqlText(), onlineMb.getActuator(), object);
        }
    }

}
