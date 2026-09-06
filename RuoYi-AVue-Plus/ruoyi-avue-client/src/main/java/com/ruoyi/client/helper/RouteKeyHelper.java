package com.ruoyi.client.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/8/18 - 17:41
 */
public class RouteKeyHelper {

    private Map<String, Class> classMap = new ConcurrentHashMap<>();
    private static Logger logger = LoggerFactory.getLogger(RouteKeyHelper.class);
    private static RouteKeyHelper INSTANCE = new RouteKeyHelper();

    public static RouteKeyHelper getInstance() {
        return INSTANCE;
    }

    public void register(String key, Class clazz) {
        Class oldValue = classMap.put(key, clazz);
        if (oldValue != null) {
            logger.warn("route key 存在重复 : " + key + " , " + clazz.getName() + "\t " + oldValue.getName());
        }
    }

    public Class get(String key) {
        return classMap.get(key);
    }
}
