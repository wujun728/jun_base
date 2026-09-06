package com.ruoyi.client.helper;

import com.ruoyi.client.enums.AVueAttrLevel;
import com.ruoyi.client.handler.AVueHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/8/17 - 16:22
 */
@Component
public class HandlerHelper implements InitializingBean {

    @Autowired(required = false)
    private List<AVueHandler> handlerList;

    public Map<String, Object> handler(AVueAttrLevel level, AnnotatedElement element, Annotation annotation) {
        Map<String, Object> all = new LinkedHashMap<>();
        if (handlerList != null) {

            for (AVueHandler handler : handlerList) {

                if (!handler.preHandler(level, annotation)) {
                    continue;
                }

                Map<String, Object> result = handler.handler(level, element, annotation);

                if (result != null) {
                    all.putAll(result);
                }
            }
        }
        return all;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    public void handler(AVueAttrLevel level, AnnotatedElement element, Annotation annotation,
        Map<String, Object> option) {
        if (handlerList != null) {

            for (AVueHandler handler : handlerList) {

                if (!handler.preHandler(level, annotation)) {
                    continue;
                }

                handler.handler(level, element, annotation, option);

            }
        }
    }
}
