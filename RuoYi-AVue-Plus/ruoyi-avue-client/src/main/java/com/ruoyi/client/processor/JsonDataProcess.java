package com.ruoyi.client.processor;

import com.ruoyi.client.enums.AVueAttrLevel;
import com.ruoyi.client.utils.JsonParseUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.lang.reflect.AnnotatedElement;
import java.util.Map;

/**
 * 将字符串转成Map对象
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/9/9 - 17:25
 */
@Component
public class JsonDataProcess extends AbstractAttrDataProcess implements InitializingBean {

    @Override
    public Object apply(AVueAttrLevel level, AnnotatedElement element, Map<String, Object> attrMap, String attrName) {
        Object obj = attrMap.get(attrName);
        if (obj instanceof String) {
            String dicValue = attrMap.get(attrName).toString();
            return JsonParseUtils.parse(dicValue, Map.class);
        }
        return obj;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        addAttrName("dicQuery");
        addAttrName("props");
    }
}
