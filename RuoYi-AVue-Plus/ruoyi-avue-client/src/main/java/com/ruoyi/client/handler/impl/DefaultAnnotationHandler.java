package com.ruoyi.client.handler.impl;

import com.ruoyi.client.config.props.AVueProperties;
import com.ruoyi.client.enums.AVueAttrLevel;
import com.ruoyi.client.handler.AVueHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认的注解处理器
 * <p>
 * 如果你需要改造,继承该类,重写内部所有方法
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/8/17 - 12:53
 */
public class DefaultAnnotationHandler implements AVueHandler {

    private String defaultPackagePrefix = "com.ruoyi.client.annotation";

    private Map<String, Map<String, Object>> cacheMap = new ConcurrentHashMap<>();

    @Autowired
    private AVueProperties prop;

    private Set<Class<? extends Annotation>> excludeAnnotationSet = new HashSet<>();

    @Override
    public boolean preHandler(AVueAttrLevel level, Annotation element) {
        return element.annotationType().getPackage().getName().startsWith(defaultPackagePrefix) && !excludeAnnotationSet
            .contains(element.annotationType());
    }

    @Override
    public void handler(AVueAttrLevel level, AnnotatedElement element, Annotation annotation,
        Map<String, Object> levelMap) {
        // doSomething
    }

    @Override
    public Map<String, Object> handler(AVueAttrLevel leve, AnnotatedElement element, Annotation annotation) {
        String key = getKey(element, annotation);
        if (prop.isDebug()) {
            return getAnnotationValueMap(element, annotation);
        }
        return cacheMap.computeIfAbsent(key, k -> getAnnotationValueMap(element, annotation));
    }

    private Map<String, Object> getAnnotationValueMap(AnnotatedElement element, Annotation annotation) {
        Map<String, Object> annotationValueMap = AnnotationUtils.getAnnotationAttributes(element, annotation);
        // 直接显示默认值
        if (prop.isShowDefaultValue()) {
            return annotationValueMap;
        } else {
            Map<String, Object> updateValueMap = new HashMap<>();
            annotationValueMap.forEach((K, V) -> {
                if (V != null) {
                    Object defaultValue = AnnotationUtils.getDefaultValue(annotation.annotationType(), K);
                    if (!V.equals(defaultValue) || K.equals("type")) {
                        Class<?> fieldClazz = V.getClass();
                        if (fieldClazz.isArray() && !fieldClazz.getName().startsWith("[Ljava.lang")) {
                            Object[] arrayAnnotation = (Object[])V;
                            if (arrayAnnotation.length > 0) {
                                List<Map<String, Object>> ruleList = new ArrayList<>();
                                for (Object annotationObject : arrayAnnotation) {
                                    ruleList.add(getAnnotationValueMap(null, (Annotation)annotationObject));
                                }
                                updateValueMap.put(K, ruleList);
                            }
                        }
                        // 如果是值是注解内容，那么递归再获取一遍
                        else if (V instanceof Annotation) {
                            updateValueMap.put(K, getAnnotationValueMap(null, (Annotation)V));
                        } else {
                            updateValueMap.put(K, V);
                        }
                    }

                }
            });
            return updateValueMap;
        }
    }

    protected String getKey(AnnotatedElement element, Annotation annotation) {
        String elementSimpleName = null;
        if (element instanceof Class) {
            elementSimpleName = ((Class)element).getSimpleName();
        } else if (element instanceof Field) {
            Field field = (Field)element;
            elementSimpleName = field.getDeclaringClass().getSimpleName() + "-" + field.getName();
        }
        String annotationSimpleName = annotation.annotationType().getSimpleName();
        return elementSimpleName + "-" + annotationSimpleName;
    }

    public void addExcludeAnnotationSet(Class<? extends Annotation> annotation) {
        this.excludeAnnotationSet.add(annotation);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
