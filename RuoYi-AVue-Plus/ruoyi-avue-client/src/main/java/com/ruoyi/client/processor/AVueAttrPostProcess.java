package com.ruoyi.client.processor;

import com.ruoyi.client.enums.AVueAttrLevel;
import com.ruoyi.client.handler.AVueLevelHandler;

import java.lang.reflect.AnnotatedElement;
import java.util.Map;

/**
 * 属性执行的生命周期
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/8/17 - 11:16
 */
public interface AVueAttrPostProcess {

    public void attrPostProcess(AVueAttrLevel level, AnnotatedElement element, Map<String, Object> attrMap);

}
