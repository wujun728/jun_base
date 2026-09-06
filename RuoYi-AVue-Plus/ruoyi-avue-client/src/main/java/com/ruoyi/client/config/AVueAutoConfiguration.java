package com.ruoyi.client.config;

import com.ruoyi.client.annotation.AVueGroup;
import com.ruoyi.client.annotation.column.AVueDynamic;
import com.ruoyi.client.annotation.column.AVueJson;
import com.ruoyi.client.annotation.column.AVueTable;
import com.ruoyi.client.config.props.AVueProperties;
import com.ruoyi.client.handler.impl.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * AVue使用自动化配置
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/8/18 - 16:56
 */
@EnableConfigurationProperties(AVueProperties.class)
@ComponentScan(basePackages = {"com.ruoyi.client"})
@Configuration
public class AVueAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(value = DefaultAnnotationHandler.class)
    public DefaultAnnotationHandler defaultAnnotationHandler() {
        DefaultAnnotationHandler defaultAnnotationHandler = new DefaultAnnotationHandler();
        defaultAnnotationHandler.addExcludeAnnotationSet(AVueGroup.class);
        return defaultAnnotationHandler;
    }

    @Bean
    @ConditionalOnMissingBean(value = SupportNestingAnnotationHandler.class)
    public SupportNestingAnnotationHandler dynamicAnnotationHandler() {
        SupportNestingAnnotationHandler supportNestingAnnotationHandler = new SupportNestingAnnotationHandler();
        supportNestingAnnotationHandler.addNestingAnnotation(AVueDynamic.class);
        supportNestingAnnotationHandler.addNestingAnnotation(AVueTable.class);
        return supportNestingAnnotationHandler;
    }


    @Bean
    @ConditionalOnMissingBean(value = GroupAnnotationHandler.class)
    public GroupAnnotationHandler groupAnnotationHandler() {
        return new GroupAnnotationHandler();
    }

    @Bean
    @ConditionalOnMissingBean(value = DefaultAnnotationValueHandler.class)
    public DefaultAnnotationValueHandler defaultAnnotationValueHandler() {
        return new DefaultAnnotationValueHandler();
    }

    @Bean
    @ConditionalOnMissingBean(value = NewImportComponentsHandler.class)
    public NewImportComponentsHandler newImportComponentsHandler() {
        // 新注册的组件处理
        NewImportComponentsHandler newImportComponentsHandler = new NewImportComponentsHandler();
        newImportComponentsHandler.addRegisterComponent(AVueJson.class);
        newImportComponentsHandler.addRegisterComponent(AVueTable.class);
        return newImportComponentsHandler;
    }

}
