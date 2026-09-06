package com.ruoyi.client.handler;

import com.ruoyi.client.enums.AVueAttrLevel;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Map;

/**
 * 类的注解描述解析
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/8/17 - 11:45
 */
public interface AVueHandler extends AVueLevelHandler {

    /**
     * 处理当前字段所携带的注解器
     *
     * @param leve
     * @param element
     * @param annotation
     * @return
     */
    public Map<String, Object> handler(AVueAttrLevel leve, AnnotatedElement element, Annotation annotation);

    /**
     * 处理当前字段所携带的注解器
     *
     * @param leve
     * @param element
     * @param annotation
     * @return
     */
    default Map<String, Object> handler(AVueAttrLevel leve, AnnotatedElement element, Annotation[] annotation) {
        return null;
    }

    /**
     * 处理当前字段所携带的处理器
     *
     * @param level      层级
     * @param element    字段属性
     * @param annotation 注解属性
     * @param levelMap   层级对象
     * @return
     */
    void handler(AVueAttrLevel level, AnnotatedElement element, Annotation annotation, Map<String, Object> levelMap);
}
