package com.geek.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.geek.common.processor.serializer.SensitiveJsonSerializer;

import tools.jackson.databind.annotation.JsonSerialize;

/**
 * 数据脱敏注解
 *
 * @author geek
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveJsonSerializer.class)
public @interface Sensitive
{
    String desensitizedType();
}