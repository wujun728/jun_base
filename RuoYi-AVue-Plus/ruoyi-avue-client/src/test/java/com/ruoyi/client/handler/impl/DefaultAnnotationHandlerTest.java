package com.ruoyi.client.handler.impl;

import com.ruoyi.client.annotation.AVueConfig;
import com.ruoyi.client.annotation.AVueFromOption;
import com.ruoyi.client.annotation.AVuePage;
import com.ruoyi.client.annotation.AVueTableOption;
import com.ruoyi.client.enums.AVueAttrLevel;
import com.ruoyi.client.helper.HandlerHelper;
import com.ruoyi.client.helper.AttrPostProcessHelper;
import com.ruoyi.client.model.AVueCrudModel;
import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

public class DefaultAnnotationHandlerTest {

    private HandlerHelper handlerHelper;
    private AttrPostProcessHelper attrPostProcessHelper;

    private DefaultAnnotationHandler defaultAnnotationHandler = new DefaultAnnotationHandler();

    private Map<Class<? extends Annotation>, AVueAttrLevel> annotationMap = new HashMap<>();

    @Before
    public void init() {
        annotationMap.put(AVueConfig.class, AVueAttrLevel.CONFIG);
        annotationMap.put(AVuePage.class, AVueAttrLevel.PAGE);
        annotationMap.put(AVueFromOption.class, AVueAttrLevel.OPTION);
        annotationMap.put(AVueTableOption.class, AVueAttrLevel.OPTION);
    }

    @Test
    public void testAnnotationObject() {
        getMap(AVueCrudModel.class);
    }

    public Map<String, Object> getMap(Class clazz) {

        // 获取类的
        Annotation[] declaredAnnotations = clazz.getDeclaredAnnotations();
        Map<String, Map<String, Object>> jsonData = new LinkedHashMap<>();

        // 处理类级别的
        for (int i = 0; i < declaredAnnotations.length; i++) {

            Annotation declaredAnnotation = declaredAnnotations[i];

            AVueAttrLevel aVueAttrLevel = annotationMap.get(declaredAnnotation);

            String keyName = aVueAttrLevel.getKeyName();

            // 这里应该是个List
            Map<String, Object> handlerResult = handlerHelper.handler(aVueAttrLevel, clazz, declaredAnnotation);

            attrPostProcessHelper.handler(aVueAttrLevel, clazz, handlerResult);

            jsonData.put(keyName, handlerResult);
        }

        Map<String, Object> option =
            jsonData.computeIfAbsent(AVueAttrLevel.OPTION.getKeyName(), k -> new LinkedHashMap<>());

        // option级别
        Field[] declaredFields = clazz.getDeclaredFields();

        List<Map<String, Object>> columnList = new ArrayList<>();
        for (int i = 0; i < declaredFields.length; i++) {
            Field declaredField = declaredFields[i];
            // 得到字段的注解
            Annotation[] fieldAnnotations = declaredField.getAnnotations();

            Map<String, Object> fieldMap = new LinkedHashMap<>();

            // 字段可能是多个注解构成，每个注解都只可能为一个字段服务
            for (int j = 0; j < fieldAnnotations.length; j++) {
                Annotation fieldAnnotation = declaredField.getDeclaredAnnotations()[j];
                Map<String, Object> handlerResult =
                    handlerHelper.handler(AVueAttrLevel.OPTION_COLUMN, declaredField, fieldAnnotation);
                fieldMap.putAll(handlerResult);
            }

            attrPostProcessHelper.handler(AVueAttrLevel.OPTION_COLUMN, declaredField, fieldMap);

            columnList.add(fieldMap);
        }

        option.put(AVueAttrLevel.OPTION_COLUMN.getKeyName(), columnList);

        return null;
    }

}