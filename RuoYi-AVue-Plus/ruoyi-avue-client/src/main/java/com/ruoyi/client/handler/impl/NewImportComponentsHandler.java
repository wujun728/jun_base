package com.ruoyi.client.handler.impl;

import com.ruoyi.client.enums.AVueAttrLevel;
import com.ruoyi.client.enums.AVueConstants;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 新增的非AVue的组件注册处理
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/9/15 - 10:14
 */
public class NewImportComponentsHandler extends DefaultAnnotationHandler {

    private Set<Class<? extends Annotation>> registerComponentSet = new HashSet<>();

    @Override
    public boolean preHandler(AVueAttrLevel level, Annotation element) {
        return registerComponentSet.contains(element.annotationType());
    }

    @Override
    public void handler(AVueAttrLevel level, AnnotatedElement element, Annotation annotation,
        Map<String, Object> optionsMap) {
        if (this.registerComponentSet.contains(annotation.annotationType())) {
            Set<String> registerSet =
                (Set<String>)optionsMap.computeIfAbsent(AVueConstants.REGISTER_FIELD_KEY, k -> new HashSet<String>());
            Object prop = AnnotationUtils.getValue(annotation, "prop");
            // 如果属性没填写，那么按照字段名称给
            if (prop == null || "".equals(prop)) {
                prop = ((Field)element).getName();
            }
            registerSet.add(prop.toString());
        }
    }

    public void addRegisterComponent(Class<? extends Annotation> registerComponentSet) {
        this.registerComponentSet.add(registerComponentSet);
    }

    public void setRegisterComponentSet(Set<Class<? extends Annotation>> registerComponentSet) {
        this.registerComponentSet = registerComponentSet;
    }
}
