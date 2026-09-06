package com.ruoyi.client.processor;

import com.ruoyi.client.attrDate.DictionaryDataCallback;
import com.ruoyi.client.enums.AVueAttrLevel;
import com.ruoyi.client.model.PropsModel;
import org.springframework.stereotype.Component;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/8/19 - 15:08
 */
@Component
public class TestModelProcessor implements DictionaryDataCallback<Map<String, Object>> {

    @Override
    public String callName() {
        return "test-likeStar-map";
    }

    @Override
    public PropsModel props() {
        return new PropsModel("username", "id");
    }

    @Override
    public List<Map<String, Object>> call(AVueAttrLevel level, AnnotatedElement element, Map<String, Object> attrMap) {
        // 模拟数据
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(mock(1, "周杰伦"));
        list.add(mock(2, "林俊杰"));
        list.add(mock(3, "王力宏"));
        list.add(mock(4, "许嵩"));
        return list;
    }

    private Map<String, Object> mock(int id, String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("username", name);
        return map;
    }
}
