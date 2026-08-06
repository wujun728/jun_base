package com.ruoyi.tenant;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Data
public class TenantProperties {

    private static TenantProperties instance;

    private Boolean enable = true;

    private String defaultTenantId = "00000000";

    /**
     * 多租户字段名称
     */
    private String column = "tenant_id";

    /**
     * 多租户系统数据表
     */
    private List<String> ignoreTables = new ArrayList<>();


    private TenantProperties() {
    }

    public static TenantProperties getInstance() {
        if (instance == null) {
            instance = new TenantProperties();
            instance.getIgnoreTables().addAll(Arrays.asList("sys_menu", "sys_config", "sys_dict_data", "sys_dict_type", "sys_role_data_rule", "gen_table","columns","tables",
                    "gen_table_column", "sys_role_menu", "sys_logininfor", "sys_tenant", "sys_user", "sys_user_post","sys_role_dept", "sys_role_menu", "sys_role", "sys_dept", "sys_user_role", "sys_job", "sys_job_log", "sys_oper_log"));
        }
        return instance;
    }
}
