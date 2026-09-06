package com.geek.framework.config;

import java.math.BigInteger;
import java.util.TimeZone;

import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.ser.std.ToStringSerializer;

/**
 * 程序注解配置
 *
 * @author geek
 */
@Configuration
// 表示通过aop框架暴露该代理对象,AopContext能够访问
@EnableAspectJAutoProxy(exposeProxy = true)
public class ApplicationConfig {
    @Bean
    public JsonMapperBuilderCustomizer jacksonObjectMapperCustomization() {
        return builder -> {
            // 时区配置
            builder.defaultTimeZone(TimeZone.getDefault());

            // 将 long/Long/BigInteger 序列化为字符串，避免前端 JS 精度丢失
            SimpleModule longToStringModule = new SimpleModule();
            longToStringModule.addSerializer(Long.class, ToStringSerializer.instance);
            longToStringModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
            longToStringModule.addSerializer(BigInteger.class, ToStringSerializer.instance);
            builder.addModule(longToStringModule);
        };
    }
}
