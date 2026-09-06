package com.ruoyi.client.helper;

import com.ruoyi.client.annotation.column.AVueUpload;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 对象缓存配置处理器
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/9/24 - 15:47
 */
public class ObjectConfigCacheHelper {

    private Map<String, Object> cacheMap = new ConcurrentHashMap<>();

    private static ObjectConfigCacheHelper INSTANCE = new ObjectConfigCacheHelper();

    public static ObjectConfigCacheHelper getINSTANCE() {
        return INSTANCE;
    }

    public void register(String group, String key, Object value) {
        cacheMap.put(group + "-" + key, value);
    }

    public <T> T getObejct(String group, String key, Class<T> clazz) {
        return (T)cacheMap.get(group + "-" + key);
    }

    public void registerAnnotationValue(Class<? extends Annotation> clazz, Map<String, Object> value) {
        register("AV_", clazz.getSimpleName(), value);
    }

    public Map<String, Object> getAnnotationValue(Class<? extends Annotation> clazz) {
        return getObejct("AV_", clazz.getSimpleName(), Map.class);
    }

}
