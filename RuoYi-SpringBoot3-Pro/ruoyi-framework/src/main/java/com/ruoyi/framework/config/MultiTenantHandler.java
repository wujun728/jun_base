package com.ruoyi.framework.config;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.config.properties.TenantProperties;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.schema.Column;

import java.util.List;

public class MultiTenantHandler implements TenantLineHandler {
    private final TenantProperties properties;

    public MultiTenantHandler(TenantProperties properties) {
        this.properties = properties;
    }

    private boolean validateAuthentication() {
        return SecurityUtils.getAuthentication() != null && !StringUtils.equals("anonymousUser", String.valueOf(SecurityUtils.getAuthentication().getPrincipal()));
    }

    @Override
    public Expression getTenantId() {
        return validateAuthentication() ? new LongValue(SecurityUtils.getLoginUser().getUser().getTenantId()) : null;
    }

    @Override
    public String getTenantIdColumn() {
        return properties.getColumn();
    }

    @Override
    public boolean ignoreTable(String tableName) {
        if (validateAuthentication()) {
            List<String> ignoreLoginNames = properties.getIgnoreLoginNames();
            String loginName = SecurityUtils.getUsername();
            if (null != ignoreLoginNames && ignoreLoginNames.contains(loginName)) {
                return true;
            }
        }

        List<String> ignoreTables = properties.getIgnoreTables();
        if (null != ignoreTables && ignoreTables.contains(tableName)) {
            return true;
        }

        return false;
    }

    @Override
    public boolean ignoreInsert(List<Column> columns, String tenantIdColumn) {
        return TenantLineHandler.super.ignoreInsert(columns, tenantIdColumn);
    }
}
