package com.ruoyi.client.processor;

import com.ruoyi.client.enums.AVueAttrLevel;
import com.ruoyi.client.utils.JsonParseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.AnnotatedElement;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 通用的数据处理器
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/9/9 - 17:31
 */
public abstract class AbstractAttrDataProcess implements AVueAttrPostProcess {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private Set<String> fieldList = new HashSet<>();

    @Override
    public void attrPostProcess(AVueAttrLevel level, AnnotatedElement element, Map<String, Object> attrMap) {
        fieldList.forEach((attrName) -> {
            if (attrMap.containsKey(attrName)) {
                try {
                    Object apply = apply(level, element, attrMap, attrName);
                    attrMap.put(attrName, apply);
                } catch (Exception e) {
                    logger.warn("属性处理异常", e);
                }
            }
        });
    }

    public abstract Object apply(AVueAttrLevel level, AnnotatedElement element, Map<String, Object> attrMap,
        String attrName);

    public void addAttrName(String name) {
        fieldList.add(name);
    }

    public void restAttrName() {
        fieldList.clear();
    }

}
