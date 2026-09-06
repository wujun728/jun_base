package com.ruoyi.client.handler.impl;

import com.ruoyi.client.annotation.column.AVueDynamic;
import com.ruoyi.client.annotation.column.AVueTable;
import com.ruoyi.client.enums.AVueAttrLevel;
import com.ruoyi.client.handler.AVueAnnotationHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 支持嵌套注解相关的属性
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/8/30 - 10:41
 */
public class SupportNestingAnnotationHandler extends DefaultAnnotationHandler {

    private Set<Class<? extends Annotation>> nestingAnnotationList = new HashSet<>();

    @Autowired
    private AVueAnnotationHandler annotationHandler;

    @Override
    public Map<String, Object> handler(AVueAttrLevel leve, AnnotatedElement element, Annotation annotation) {
        Class<?> type = ((Field) element).getType();

        Map<String, Object> handler = super.handler(leve, element, annotation);

        Map<String, Map<String, Object>> clazzAnnotationMap = annotationHandler.parse(type);

        if (clazzAnnotationMap.size() > 0) {
            Map<String, Object> result = clazzAnnotationMap.get(AVueAttrLevel.OPTION.getKeyName());
            handler.put("children", result);
        }
        return handler;
    }

    @Override
    public boolean preHandler(AVueAttrLevel level, Annotation element) {
        return getNestingAnnotationList().contains(element.annotationType());
    }

    public void addNestingAnnotation(Class<? extends Annotation> nestingAnnotation) {
        this.nestingAnnotationList.add(nestingAnnotation);
    }

    public Set<Class<? extends Annotation>> getNestingAnnotationList() {
        return nestingAnnotationList;
    }
}
