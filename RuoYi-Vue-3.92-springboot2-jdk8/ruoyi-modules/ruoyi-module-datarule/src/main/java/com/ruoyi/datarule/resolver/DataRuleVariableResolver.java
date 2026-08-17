package com.ruoyi.datarule.resolver;

import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;

public class DataRuleVariableResolver {

    private final JdbcTemplate jdbcTemplate;

    public DataRuleVariableResolver(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static List<Map<String, String>> getBuiltInVariables() {
        List<Map<String, String>> list = new ArrayList<>();
        list.add(create("${currUserId}", "当前用户ID"));
        list.add(create("${currUserName}", "当前用户名"));
        list.add(create("${currUserRoles}", "当前用户全部角色"));
        list.add(create("${getTenants}", "当前用户可访问的项目编号"));
        list.add(create("${getCurrProjectId}", "当前用户所在项目Id"));
        list.add(create("${getCurrTenant}", "当前用户所在项目编号"));
        return list;
    }

    private static Map<String, String> create(String code, String name) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("code", code);
        map.put("name", name);
        return map;
    }

    public String resolve(String expression) {
        if (expression == null) return null;
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) return expression;

        switch (expression) {
            case "${currUserId}":
                return String.valueOf(loginUser.getUserId());
            case "${currUserName}":
                return loginUser.getUsername();
            case "${currUserRoles}":
                return String.join(",", loginUser.getUser().getRoles().stream()
                        .map(r -> r.getRoleKey()).toArray(String[]::new));
            case "${getTenants}":
                return getTenants();
            case "${getCurrProjectId}":
                return String.valueOf(loginUser.getDeptId());
            case "${getCurrTenant}":
                return getTenant();
            default:
                return expression;
        }
    }

    private String getTenants() {
        try {
            List<Map<String, Object>> list = jdbcTemplate.queryForList(
                    "select tenant_id from sys_tenant where user_id = ?",
                    SecurityUtils.getUserId());
            return list.stream().map(m -> String.valueOf(m.get("tenant_id")))
                    .reduce((a, b) -> a + "," + b).orElse("");
        } catch (Exception e) {
            return "";
        }
    }

    private String getTenant() {
        try {
            LoginUser loginUser = SecurityUtils.getLoginUser();
            if (loginUser != null) {
                return loginUser.getTenantId();
            }
        } catch (Exception ignored) {}
        return "";
    }
}