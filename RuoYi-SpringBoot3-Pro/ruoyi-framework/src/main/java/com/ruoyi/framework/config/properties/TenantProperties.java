package com.ruoyi.framework.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 多租户配置属性类
 */
@Data
@Component
@ConfigurationProperties(prefix = "tenant")
public class TenantProperties {
    /**
     * 是否开启多租户
     */
    private Boolean enable;

    /**
     * 租户id字段名
     */
    private String column;

    /**
     * 需要进行多租户过滤的表
     */
    private List<String> filterTables;

    /**
     * 需要忽略的多租户的表
     */
    private List<String> ignoreTables;

    /**
     * 需要忽略的多租户的登录用户名
     */
    private List<String> ignoreLoginNames;
}
