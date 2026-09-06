package com.ruoyi.framework.config;

import java.math.BigInteger;

import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.ser.std.ToStringSerializer;

/**
 * JSON序列化配置。
 *
 * JavaScript 对超过 Number.MAX_SAFE_INTEGER 的整数会丢失精度，旧SQ数据中存在大量雪花ID，
 * 统一转为字符串返回，避免前端关联字段变形。
 */
@Configuration
public class JacksonConfig
{
    @Bean
    public JsonMapperBuilderCustomizer longToStringJsonMapperBuilderCustomizer()
    {
        return builder -> {
            SimpleModule module = new SimpleModule("LongToStringModule");
            module.addSerializer(Long.class, ToStringSerializer.instance);
            module.addSerializer(Long.TYPE, ToStringSerializer.instance);
            module.addSerializer(BigInteger.class, ToStringSerializer.instance);
            builder.addModule(module);
        };
    }
}
