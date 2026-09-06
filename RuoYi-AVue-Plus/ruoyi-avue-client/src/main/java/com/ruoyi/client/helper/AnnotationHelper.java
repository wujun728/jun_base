package com.ruoyi.client.helper;

import com.ruoyi.client.annotation.AVueFromOption;
import com.ruoyi.client.annotation.AVueTableOption;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 获取默认配置注解信息
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/8/16 - 19:48
 */
public class AnnotationHelper {

    private static AnnotationHelper INSTANS = new AnnotationHelper();

    private Map<Annotation, Map> annotatedElementCache = new ConcurrentHashMap<>();

    private Set<String> skipMethod = new HashSet<>();

    {
        skipMethod.add("equals");
        skipMethod.add("toString");
        skipMethod.add("hashCode");
        skipMethod.add("annotationType");
    }

    public static AnnotationHelper getInstance() {
        return INSTANS;
    }

    /**
     * 全局注解默认值注册
     *
     * @param annotatedElement
     * @param elementMap
     */
    public void register(Annotation annotatedElement, Map<String, Object> elementMap) {
        annotatedElementCache.put(annotatedElement, elementMap);
    }

    /**
     * 获取注解类的默认配置类
     *
     * @param annotatedElement
     * @return
     */
    public Map<String, Object> getAnnotationClassDefaultValue(Annotation annotatedElement) {
        return annotatedElementCache
            .computeIfAbsent(annotatedElement, k -> AnnotationUtils.getAnnotationAttributes(annotatedElement));
    }

    public static void main(String[] args) {
        Method[] methods = AVueFromOption.class.getMethods();
    }
}
