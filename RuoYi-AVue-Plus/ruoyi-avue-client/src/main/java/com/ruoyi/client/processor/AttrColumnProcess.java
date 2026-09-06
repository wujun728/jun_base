package com.ruoyi.client.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.client.annotation.AVueAttr;
import com.ruoyi.client.enums.AVueAttrLevel;
import com.ruoyi.client.utils.JsonParseUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.AnnotatedElement;
import java.util.Map;

/**
 * 属性字段的attr拓展属性
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/8/18 - 16:17
 */
@Component
public class AttrColumnProcess implements AVueAttrPostProcess {

    private String attrValue = "attrExt";

    @Override
    public void attrPostProcess(AVueAttrLevel level, AnnotatedElement element, Map<String, Object> attrMap) {
        if (attrMap.containsKey(attrValue)) {
            AVueAttr[] attrs = (AVueAttr[])attrMap.get(attrValue);
            for (AVueAttr attr : attrs) {
                String name = attr.name();
                String value = attr.value();
                Class<?> aClass = attr.valueType();
                if (aClass == String.class) {
                    attrMap.put(name, value);
                } else {
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        objectMapper.readValue(value, aClass);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    Object object = JsonParseUtils.parse(value, aClass);
                    attrMap.put(name, object);
                }
            }
            attrMap.remove(attrValue);
        }
    }
}
