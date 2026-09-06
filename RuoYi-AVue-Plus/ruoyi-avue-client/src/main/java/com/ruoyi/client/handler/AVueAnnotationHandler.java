package com.ruoyi.client.handler;

import com.ruoyi.client.annotation.*;
import com.ruoyi.client.enums.AVueAttrLevel;
import com.ruoyi.client.helper.AttrPostProcessHelper;
import com.ruoyi.client.helper.HandlerHelper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * AVue注解解析器
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/8/18 - 14:56
 */
@Component
public class AVueAnnotationHandler implements InitializingBean {

    @Autowired
    private HandlerHelper handlerHelper;

    @Autowired
    private AttrPostProcessHelper attrPostProcessHelper;

    private Map<Class<? extends Annotation>, AVueAttrLevel> annotationMap = new HashMap<>();

    public Map<String, Map<String, Object>> parse(Class clazz) {

        // 获取类的
        Annotation[] declaredAnnotations = clazz.getDeclaredAnnotations();

        Map<String, Map<String, Object>> jsonData = new LinkedHashMap<>();

        // 处理类级别的
        for (int i = 0; i < declaredAnnotations.length; i++) {

            Annotation declaredAnnotation = declaredAnnotations[i];

            AVueAttrLevel aVueAttrLevel =
                annotationMap.getOrDefault(declaredAnnotation.annotationType(), AVueAttrLevel.CUSTOMIZE);

            String keyName = aVueAttrLevel.getKeyName();

            // 这里应该是个List

            Map<String, Object> handlerResult = handlerHelper.handler(aVueAttrLevel, clazz, declaredAnnotation);
            if (handlerResult.size() > 0) {

                attrPostProcessHelper.handler(aVueAttrLevel, clazz, handlerResult);

                Map<String, Object> map = jsonData.computeIfAbsent(keyName, k -> new LinkedHashMap<>());

                map.putAll(handlerResult);
            }
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

            if (fieldAnnotations.length > 0) {
                Map<String, Object> fieldMap = new LinkedHashMap<>();

                // 字段可能是多个注解构成，每个注解都只可能为一个字段服务
                for (int j = 0; j < fieldAnnotations.length; j++) {
                    Annotation fieldAnnotation = declaredField.getDeclaredAnnotations()[j];

                    AVueAttrLevel level =
                        annotationMap.getOrDefault(fieldAnnotation.annotationType(), AVueAttrLevel.OPTION_COLUMN);

                    // 如果是非列的又是属于option层级下面的数据
                    if (level == AVueAttrLevel.OPTION_COLUMN) {
                        Map<String, Object> handlerResult =
                            handlerHelper.handler(level, declaredField, fieldAnnotation);
                        fieldMap.putAll(handlerResult);
                    }

                    handlerHelper.handler(level, declaredField, fieldAnnotation, option);

                }

                attrPostProcessHelper.handler(AVueAttrLevel.OPTION_COLUMN, declaredField, fieldMap);

                if (fieldMap.size() > 0) {
                    columnList.add(fieldMap);
                }

            }
        }

        option.put(AVueAttrLevel.OPTION_COLUMN.getKeyName(), columnList);

        return jsonData;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        annotationMap.put(AVueConfig.class, AVueAttrLevel.CONFIG);
        annotationMap.put(AVuePage.class, AVueAttrLevel.PAGE);
        annotationMap.put(AVueFromOption.class, AVueAttrLevel.OPTION);
        annotationMap.put(AVueTableOption.class, AVueAttrLevel.OPTION);
        annotationMap.put(AVueEventButtons.class, AVueAttrLevel.OPTION);
        annotationMap.put(AVueGroup.class, AVueAttrLevel.OPTION_GROUP);
    }
}
