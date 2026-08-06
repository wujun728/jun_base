package com.ruoyi.tenant;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import lombok.AllArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;

@AllArgsConstructor
public class CommonTenantHandler implements TenantLineHandler {

    /**
     * 获取租户ID
     *
     * @return 租户ID
     */
    @Override
    public Expression getTenantId() {
        if (SecurityUtils.getLoginUser() != null) {
            String tenantId = SecurityUtils.getLoginUser().getTenantId();
            if (!StringUtils.isEmpty(tenantId)) {
                return new StringValue(tenantId);
            }
        }
        return new StringValue(TenantProperties.getInstance().getDefaultTenantId());
    }
    /**
     * 获取租户字段名称
     *
     * @return 租户字段名称
     */
    @Override
    public String getTenantIdColumn() {
        return com.ruoyi.tenant.TenantProperties.getInstance().getInstance().getColumn();
    }

    /**
     * 过滤租户表
     *
     * @param tableName 表名
     * @return 是否进行过滤 返回true 表示不进行多租户处理
     */
    @Override
    public boolean ignoreTable(String tableName) {
        if (!com.ruoyi.tenant.TenantProperties.getInstance().getEnable()){
            return true;
        }
        return com.ruoyi.tenant.TenantProperties.getInstance().getIgnoreTables().contains(tableName);
    }
}
