package com.ruoyi.client.attrDate;

import com.ruoyi.client.enums.AVueAttrLevel;
import com.ruoyi.client.model.PropsModel;

import java.lang.reflect.AnnotatedElement;
import java.util.List;
import java.util.Map;

/**
 * 加载本地数据
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/8/18 - 21:25
 */
public interface DictionaryDataCallback<T> {

    /**
     * 回调名称
     *
     * @return
     */
    public String callName();

    default PropsModel props() {
        return new PropsModel();
    }

    /**
     * 加载方法回调
     *
     * @param level
     * @param element
     * @param attrMap
     * @return
     */
    public List<T> call(AVueAttrLevel level, AnnotatedElement element, Map<String, Object> attrMap);

}
