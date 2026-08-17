package com.ruoyi.datarule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.datarule.domain.SysDataRule;

import java.util.List;
import java.util.Map;

public interface ISysDataRuleService extends IService<SysDataRule> {
    List<Map<String, Object>> getModelList();
    List<Map<String, Object>> getFieldList(String tableName);
    List<Map<String, Object>> getRoleList();
}