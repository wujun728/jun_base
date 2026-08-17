package com.ruoyi.datarule.config;

import com.ruoyi.datarule.resolver.DataRuleVariableResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DataScopeConfiguration {

    @Bean
    public DataRuleVariableResolver dataRuleVariableResolver(JdbcTemplate jdbcTemplate) {
        return new DataRuleVariableResolver(jdbcTemplate);
    }

}