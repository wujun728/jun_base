package com.ruoyi.framework.config;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.ruoyi.common.constant.UserConstants;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MybatisPlus配置文件
 *
 * @author ruoyi
 */
@EnableTransactionManagement
@Configuration
@MapperScan("com.ruoyi.**.mapper")
@Order(1)
public class MybatisPlusConfig {
    @Value("${mybatis-plus.global-config.db-config.db-type}")
    private String dbType;

    /**
     * 分页插件
     * 最新版 3.4.0及以上
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页插件
        PaginationInnerInterceptor innerInterceptor = new PaginationInnerInterceptor();
        innerInterceptor.setDbType(DbType.getDbType(dbType));
        innerInterceptor.setOverflow(true);

        interceptor.addInnerInterceptor(innerInterceptor);

        // 防止全表更新与删除
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return interceptor;
    }

    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> configuration.setUseDeprecatedExecutor(false);
    }

    @Bean
    public GlobalConfig globalConfiguration() {
        GlobalConfig conf = new GlobalConfig();
        conf.setEnableSqlRunner(true)
                .setDbConfig(new GlobalConfig.DbConfig()
                        .setLogicDeleteValue(UserConstants.IS_DELETE)
                        .setLogicNotDeleteValue(UserConstants.UN_DELETE));
        return conf;
    }
}
