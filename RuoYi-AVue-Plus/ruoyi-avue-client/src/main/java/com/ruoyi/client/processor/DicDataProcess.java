package com.ruoyi.client.processor;

import com.ruoyi.client.attrDate.DicEnumCallback;
import com.ruoyi.client.attrDate.DictionaryDataCallback;
import com.ruoyi.client.enums.AVueAttrLevel;
import com.ruoyi.client.helper.DicEnumsHelper;
import com.ruoyi.client.model.PropsModel;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.AnnotatedElement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 本地数据字典转换
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/8/18 - 20:00
 */
@Component
public class DicDataProcess implements AVueAttrPostProcess, InitializingBean {

    private String dicData = "dicData";

    @Autowired(required = false)
    private List<DictionaryDataCallback> dictionaryDataCallbacks;

    private Map<String, DictionaryDataCallback> dictionaryDataCallbackMap = new HashMap<>();

    @Override
    public void attrPostProcess(AVueAttrLevel level, AnnotatedElement element, Map<String, Object> attrMap) {
        if (attrMap.containsKey(dicData)) {
            String dicValue = attrMap.get(dicData).toString();
            DictionaryDataCallback<?> dictionaryDataCallback = dictionaryDataCallbackMap.get(dicValue);
            if (dictionaryDataCallback != null) {
                PropsModel props = dictionaryDataCallback.props();
                attrMap.put("props", props);
                List<?> dicData = dictionaryDataCallback.call(level, element, attrMap);
                attrMap.put("dicData", dicData);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.dictionaryDataCallbacks != null) {
            for (DictionaryDataCallback<?> dictionaryDataCallback : dictionaryDataCallbacks) {
                String keyName = dictionaryDataCallback.callName();
                dictionaryDataCallbackMap.put(keyName, dictionaryDataCallback);
            }
        }

        // 将扫描到的字典注入执行器
        DicEnumCallback dictionaryDataCallback = new DicEnumCallback();
        DicEnumsHelper.getINSTANCE().getKeys().stream().forEach((enums) -> {
            dictionaryDataCallbackMap.put(enums, dictionaryDataCallback);
        });
    }
}
