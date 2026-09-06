package com.geek.common.utils.jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;

import tools.jackson.databind.DefaultTyping;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

/**
 * 缓存场景使用的 Jackson Mapper 构建工厂。
 */
public final class CacheObjectMapperFactory {

    private static final BasicPolymorphicTypeValidator CACHE_TYPE_VALIDATOR = BasicPolymorphicTypeValidator.builder()
            .allowIfSubType("java.")
            .allowIfSubType("javax.")
            .allowIfSubType("jakarta.")
            .allowIfSubType("com.geek.")
            .allowIfSubTypeIsArray()
            .build();

    private CacheObjectMapperFactory() {
    }

    public static ClassLoader resolveClassLoader(Class<?> anchor) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = anchor.getClassLoader();
        }
        return classLoader;
    }

    public static ObjectMapper createPlainMapper(ObjectMapper baseMapper, ClassLoader classLoader) {
        return createPlainMapper(baseMapper, classLoader, false);
    }

    public static ObjectMapper createPlainMapper(ObjectMapper baseMapper, ClassLoader classLoader,
            boolean forceAllVisibility) {
        ObjectMapper base = (baseMapper != null ? baseMapper : JsonMapper.builder().build());
        var builder = base.rebuild()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .typeFactory(base.getTypeFactory().withClassLoader(classLoader));
        if (forceAllVisibility) {
            builder.changeDefaultVisibility(
                    vc -> vc.withVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY));
        }
        return builder.build();
    }

    public static ObjectMapper createTypedMapper(ObjectMapper baseMapper, ClassLoader classLoader) {
        return createTypedMapper(createPlainMapper(baseMapper, classLoader));
    }

    public static ObjectMapper createTypedMapper(ObjectMapper plainMapper) {
        return plainMapper.rebuild()
                .activateDefaultTyping(CACHE_TYPE_VALIDATOR, DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY)
                .build();
    }
}
