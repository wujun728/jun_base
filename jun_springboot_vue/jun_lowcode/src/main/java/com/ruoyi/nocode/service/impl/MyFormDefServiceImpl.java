package com.ruoyi.nocode.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.nocode.domain.MyFormAttr;
import com.ruoyi.nocode.domain.MyFormDef;
import com.ruoyi.nocode.mapper.MyFormAttrMapper;
import com.ruoyi.nocode.mapper.MyFormDefMapper;
import com.ruoyi.nocode.service.IMyFormDefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 表单定义Service业务层处理
 *
 * @date 2022-07-27
 */
@Service
public class MyFormDefServiceImpl implements IMyFormDefService {
    @Autowired
    private MyFormDefMapper myFormDefMapper;
    @Autowired
    private MyFormAttrMapper myFormAttrMapper;

    /**
     * 查询表单定义
     *
     * @param id 表单定义ID
     * @return 表单定义
     */
    @Override
    public MyFormDef selectMyFormDefById(String id) {
        return myFormDefMapper.selectMyFormDefById(id);
    }

    /**
     * 查询表单定义列表
     *
     * @param myFormDef 表单定义
     * @return 表单定义
     */
    @Override
    public List<MyFormDef> selectMyFormDefList(MyFormDef myFormDef) {
        return myFormDefMapper.selectMyFormDefList(myFormDef);
    }

    /**
     * 新增表单定义
     *
     * @param myFormDef 表单定义
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertMyFormDef(MyFormDef myFormDef) {
        String id = UUID.randomUUID().toString();
        myFormDef.setId(id);
        myFormDef.setCreateBy(SecurityUtils.getUsername());
        myFormDef.setCreateName(SecurityUtils.getLoginUser().getUser().getNickName());
        myFormDef.setCreateTime(DateUtils.getNowDate());

        JSONObject jsonObject = JSONObject.parseObject(myFormDef.getDefination());
        JSONArray list = jsonObject.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            parseAttr(list.getJSONObject(i), id);
        }
        attrInsert(id, "创建者", "_createName");
        attrInsert(id, "创建时间", "_createTime");
        attrInsert(id, "创建者id", "_createBy");
        return myFormDefMapper.insertMyFormDef(myFormDef);
    }

    private void parseAttr(JSONObject object, String formId) {
        String type = object.getString("type");
        if (StringUtils.isEmpty(type))
            return;
        if ("grid".equals(type)) {
            parseGridAttr(object, formId);
        } else {
//            parseCommonAttr(object, formId);
            attrInsert(formId, object.getString("name"), object.getString("model"));
        }
    }

    private void parseGridAttr(JSONObject object, String formId) {
        JSONArray list = object.getJSONArray("columns");
        for (int i = 0; i < list.size(); i++) {
            JSONArray array = list.getJSONObject(i).getJSONArray("list");
            if (array != null && array.size() > 0) {
                for (int j = 0; j < array.size(); j++) {
                    parseAttr(array.getJSONObject(j), formId);
                }
            }
        }
    }

//    private void parseCommonAttr(JSONObject object, String formId) {
//        attrInsert(formId, object.getString("name"), object.getString("model"));
////        MyFormAttr myFormAttr = new MyFormAttr();
////        myFormAttr.setId(UUID.randomUUID().toString());
////        myFormAttr.setFormId(formId);
////        myFormAttr.setAttrName(object.getString("name"));
////        myFormAttr.setAttrCode(object.getString("model"));
////        myFormAttr.setCreateBy(SecurityUtils.getUsername());
////        myFormAttr.setCreateName(SecurityUtils.getLoginUser().getUser().getNickName());
////        myFormAttr.setCreateTime(DateUtils.getNowDate());
////        myFormAttrMapper.insertMyFormAttr(myFormAttr);
//    }

    private void attrInsert(String formId, String name, String model) {
        MyFormAttr myFormAttr = new MyFormAttr();
        myFormAttr.setId(UUID.randomUUID().toString());
        myFormAttr.setFormId(formId);
        myFormAttr.setAttrName(name);
        myFormAttr.setAttrCode(model);
        myFormAttr.setCreateBy(SecurityUtils.getUsername());
        myFormAttr.setCreateName(SecurityUtils.getLoginUser().getUser().getNickName());
        myFormAttr.setCreateTime(DateUtils.getNowDate());
        myFormAttrMapper.insertMyFormAttr(myFormAttr);
    }

    /**
     * 修改表单定义
     *
     * @param myFormDef 表单定义
     * @return 结果
     */
    @Override
    public AjaxResult updateMyFormDef(MyFormDef myFormDef) {
        MyFormDef old = myFormDefMapper.selectMyFormDefById(myFormDef.getId());
        myFormDef.setUpdateTime(DateUtils.getNowDate());
        if (!StringUtils.isEmpty(myFormDef.getRefProcKey()) && !myFormDef.getRefProcKey().equals(old.getRefProcKey())) {
            List<MyFormDef> list = myFormDefMapper.selectMyFormDefByProcKey(myFormDef.getRefProcKey());
            if (!CollectionUtils.isEmpty(list)) {
                return AjaxResult.error("此流程已被绑定");
            }
        }
        myFormDefMapper.updateMyFormDef(myFormDef);
        return AjaxResult.success("更新成功");
    }

    /**
     * 批量删除表单定义
     *
     * @param ids 需要删除的表单定义ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteMyFormDefByIds(String[] ids) {
        myFormAttrMapper.deleteMyFormAttrByFormIds(ids);
        return myFormDefMapper.deleteMyFormDefByIds(ids);
    }

    /**
     * 删除表单定义信息
     *
     * @param id 表单定义ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteMyFormDefById(String id) {
        myFormAttrMapper.deleteMyFormAttrByFormId(id);
        return myFormDefMapper.deleteMyFormDefById(id);
    }

}
