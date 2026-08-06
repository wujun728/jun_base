package com.ruoyi.datarule.config;

import com.ruoyi.datarule.handler.DataRuleHandler;
import com.ruoyi.datarule.handler.DataRuleSqlHandler;
import com.ruoyi.datarule.interceptor.DataRuleInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;


@Configuration
@AllArgsConstructor
public class DataScopeConfiguration {

    private JdbcTemplate jdbcTemplate;

    @Bean
    @ConditionalOnMissingBean(DataRuleHandler.class)
    public DataRuleHandler dataRuleHandler() {
        DataRuleHandler dataRuleHandler = new DataRuleHandler(jdbcTemplate);
        dataRuleHandler.init();
        return dataRuleHandler;
    }


    @Bean
    @ConditionalOnBean(DataRuleHandler.class)
    @ConditionalOnMissingBean(DataRuleSqlHandler.class)
    public DataRuleSqlHandler dataScopeHandler(DataRuleHandler dataRuleHandler) {
        return new DataRuleSqlHandler(dataRuleHandler);
    }

    @Bean
    @ConditionalOnBean({DataRuleSqlHandler.class, DataRuleHandler.class})
    @ConditionalOnMissingBean(DataRuleInterceptor.class)
    public DataRuleInterceptor interceptor(DataRuleSqlHandler dataRuleSqlHandler, DataRuleHandler dataRuleHandler) {
        return new DataRuleInterceptor(dataRuleSqlHandler, dataRuleHandler);
    }

}
