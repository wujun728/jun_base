package com.geek.framework.datasource.manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.alibaba.druid.pool.DruidDataSource;
import com.geek.framework.datasource.properties.DynamicDataSourceProperties;
import com.mybatisflex.core.datasource.FlexDataSource;

import jakarta.annotation.PreDestroy;

@Configuration
@DependsOn("liquibase")
public class DataSourceManager implements InitializingBean {
    protected final Logger logger = LoggerFactory.getLogger(DataSourceManager.class);
    private Map<String, DataSource> targetDataSources = new HashMap<>();

    @Autowired
    private DynamicDataSourceProperties dataSourceProperties;

    @Bean
    public FlexDataSource dataSource() {
        String primary = dataSourceProperties.getPrimary();
        DataSource primaryDataSource = targetDataSources.get(primary);
        FlexDataSource dynamicDataSource = new FlexDataSource(primary, primaryDataSource);
        for (Map.Entry<String, DataSource> entry : targetDataSources.entrySet()) {
            dynamicDataSource.addDataSource(entry.getKey(), entry.getValue());
        }
        return dynamicDataSource;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        dataSourceProperties.getDatasource().forEach((name, props) -> {
            long start = System.currentTimeMillis();
            Properties properties = dataSourceProperties.build(props);
            DruidDataSource dataSource = createDataSource(name, properties);
            logger.info("数据源：{} 链接成功，耗时：{}ms", name, System.currentTimeMillis() - start);
            targetDataSources.put(name, dataSource);
        });
    }

    public DruidDataSource createDataSource(String name, Properties prop) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setConnectProperties(prop);
        dataSource.setConnectionProperties(prop.getProperty("druid.connectionProperties"));
        try (Connection conn = dataSource.getConnection()) {
            dataSource.validateConnection(conn);
            return dataSource;
        } catch (SQLException e) {
            throw new RuntimeException("数据源连接验证失败", e);
        }
    }

    @PreDestroy
    public void destroy() {
        for (DataSource dataSource :  targetDataSources.values()) {
            if (dataSource instanceof DruidDataSource) {
                ((DruidDataSource) dataSource).close();
            }
        }
    }

    public DataSource getDataSource(String name) {
        return targetDataSources.get(name);
    }

    public DataSource getPrimaryDataSource() {
        return targetDataSources.get(dataSourceProperties.getPrimary());
    }
}
