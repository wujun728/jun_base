package com.ruoyi.client.handler.impl;

import com.ruoyi.client.enums.AVueAttrLevel;
import com.ruoyi.client.handler.AVueHandler;
import com.ruoyi.client.helper.ObjectConfigCacheHelper;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Map;

/**
 * 负责处理全局定义好的默认值进行赋值
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/9/24 - 15:55
 */
public class DefaultAnnotationValueHandler implements AVueHandler {

    @Override
    public Map<String, Object> handler(AVueAttrLevel leve, AnnotatedElement element, Annotation annotation) {
        return ObjectConfigCacheHelper.getINSTANCE().getAnnotationValue(annotation.annotationType());
    }

    @Override
    public boolean preHandler(AVueAttrLevel level, Annotation element) {
        return ObjectConfigCacheHelper.getINSTANCE().getAnnotationValue(element.annotationType()) != null;
    }

    @Override
    public void handler(AVueAttrLevel level, AnnotatedElement element, Annotation annotation,
        Map<String, Object> levelMap) {
        // 不想做什么
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}
