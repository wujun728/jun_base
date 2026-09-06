package com.geek.framework.datasource.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.geek.framework.datasource.properties.DynamicDataSourceProperties;
import com.geek.framework.datasource.properties.DynamicDataSourceProperties.DynamicSourceProperties;

import liquibase.integration.spring.SpringLiquibase;

@Configuration
@ConditionalOnProperty(name = "spring.liquibase.enabled", havingValue = "true", matchIfMissing = true)
public class LiquibaseConfig {

    @Autowired
    private DynamicDataSourceProperties dataSourceProperties;

    @Bean
    public SpringLiquibase liquibase() {
        String primary = dataSourceProperties.getPrimary();
        DynamicSourceProperties primaryProps = dataSourceProperties.getDatasource().get(primary);
        DriverManagerDataSource nativeDs = new DriverManagerDataSource();
        nativeDs.setUrl(primaryProps.getUrl());
        nativeDs.setUsername(primaryProps.getUsername());
        nativeDs.setPassword(primaryProps.getPassword());
        nativeDs.setDriverClassName(primaryProps.getDriverClassName());
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(nativeDs);
        liquibase.setChangeLog("classpath:db/changelog/db.changelog-master.yaml");
        liquibase.setShouldRun(true);
        return liquibase;
    }
}
