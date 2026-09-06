package com.geek.framework.datasource.properties;

import java.util.Properties;

import org.apache.commons.collections4.map.LinkedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.datasource.dynamic")
public class DynamicDataSourceProperties {

    private LinkedMap<String, DynamicSourceProperties> datasource;
    private String primary;

    @Autowired
    DruidProperties druidProperties;

    public Properties build(DynamicSourceProperties dataSourceProperties) {
        Properties prop = new Properties();
        setPropertiesIfNotNull(prop, "druid.name", dataSourceProperties.getName());
        setPropertiesIfNotNull(prop, "druid.url", dataSourceProperties.getUrl());
        setPropertiesIfNotNull(prop, "druid.username", dataSourceProperties.getUsername());
        setPropertiesIfNotNull(prop, "druid.password", dataSourceProperties.getPassword());
        setPropertiesIfNotNull(prop, "druid.driverClassName", dataSourceProperties.getDriverClassName());
        setPropertiesIfNotNull(prop, "druid.testWhileIdle", druidProperties.isTestWhileIdle());
        setPropertiesIfNotNull(prop, "druid.testOnBorrow", druidProperties.isTestOnBorrow());
        setPropertiesIfNotNull(prop, "druid.validationQuery", druidProperties.getValidationQuery());
        setPropertiesIfNotNull(prop, "druid.filters", druidProperties.getFilters());
        setPropertiesIfNotNull(prop, "druid.initialSize", druidProperties.getInitialSize());
        setPropertiesIfNotNull(prop, "druid.minIdle", druidProperties.getMinIdle());
        setPropertiesIfNotNull(prop, "druid.maxActive", druidProperties.getMaxActive());
        setPropertiesIfNotNull(prop, "druid.maxWait", druidProperties.getMaxWait());
        setPropertiesIfNotNull(prop, "druid.testOnReturn", druidProperties.isTestOnReturn());
        setPropertiesIfNotNull(prop, "druid.connectionProperties", druidProperties.getConnectionProperties());
        setPropertiesIfNotNull(prop, "druid.timeBetweenEvictionRunsMillis",
                druidProperties.getTimeBetweenEvictionRunsMillis());
        setPropertiesIfNotNull(prop, "druid.minEvictableIdleTimeMillis",
                druidProperties.getMinEvictableIdleTimeMillis());
        setPropertiesIfNotNull(prop, "druid.maxEvictableIdleTimeMillis",
                druidProperties.getMaxEvictableIdleTimeMillis());
        return prop;
    }

    public void setPropertiesIfNotNull(Properties prop, String key, Object value) {
        if (value != null) {
            prop.setProperty(key, String.valueOf(value));
        }
    }

    @Data
    public static class DynamicSourceProperties {
        private String name;
        private String url;
        private String username;
        private String password;
        private String driverClassName;
    }
}
