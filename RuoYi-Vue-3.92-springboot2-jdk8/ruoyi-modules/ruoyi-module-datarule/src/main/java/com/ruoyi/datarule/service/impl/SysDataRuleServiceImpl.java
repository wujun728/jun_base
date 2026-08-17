package com.ruoyi.datarule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.datarule.domain.SysDataRule;
import com.ruoyi.datarule.mapper.SysDataRuleMapper;
import com.ruoyi.datarule.service.ISysDataRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
@Service
public class SysDataRuleServiceImpl extends ServiceImpl<SysDataRuleMapper, SysDataRule> implements ISysDataRuleService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> getModelList() {
        String sql = "select TABLE_NAME as code, TABLE_COMMENT as name from information_schema.TABLES " +
                "where TABLE_SCHEMA = 'ry-vue' and TABLE_NAME not like 'QRTZ_%' and TABLE_NAME not like 'gen_%' " +
                "and TABLE_NAME != 'sys_tenant' order by TABLE_NAME";
        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public List<Map<String, Object>> getFieldList(String tableName) {
        String sql = "select COLUMN_NAME as code, COLUMN_COMMENT as name from information_schema.COLUMNS " +
                "where TABLE_SCHEMA = 'ry-vue' and TABLE_NAME = ? order by ORDINAL_POSITION";
        return jdbcTemplate.queryForList(sql, tableName);
    }

    @Override
    public List<Map<String, Object>> getRoleList() {
        return jdbcTemplate.queryForList("select role_id as id, role_name as name from sys_role");
    }
}