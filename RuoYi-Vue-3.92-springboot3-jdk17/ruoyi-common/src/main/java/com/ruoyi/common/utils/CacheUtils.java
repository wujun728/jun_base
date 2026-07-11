package com.ruoyi.common.utils;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.Cache;
import org.springframework.util.ObjectUtils;
import com.ruoyi.common.core.cache.TtlCacheManager;
import com.ruoyi.common.utils.spring.SpringUtils;

public class CacheUtils {

    public static TtlCacheManager getCacheManager() {
        return SpringUtils.getBean(TtlCacheManager.class);
    }

    public static Cache getCache(String cacheName) {
        Cache cache = getCacheManager().getCache(cacheName);
        if (Objects.isNull(cache)) {
            throw new IllegalArgumentException("no cache found: " + cacheName);
        }
        return cache;
    }

    public static Set<String> getkeys(String cacheName) {
        Cache cache = getCache(cacheName);
        return getCacheManager().getCachekeys(cache);
    }

    public static void put(String cacheName, String key, Object value) {
        put(cacheName, key, value, 0, null);
    }

    public static void putIfAbsent(String cacheName, String key, Object value) {
        if (ObjectUtils.isEmpty(get(cacheName, key))) {
            put(cacheName, key, value, 0, null);
        }
    }

    public static boolean hasKey(String cacheName, String key) {
        return !ObjectUtils.isEmpty(get(cacheName, key));
    }

    public static void put(String cacheName, String key, Object value, long timeout, TimeUnit unit) {
        if (timeout != 0 && unit != null) {
            getCacheManager().setCacheObject(cacheName, key, value, timeout, unit);
        } else {
            getCacheManager().setCacheObject(cacheName, key, value);
        }
    }

    public static Cache.ValueWrapper get(String cacheName, String key) {
        return getCache(cacheName).get(key);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String cacheName, String key, Class<T> type) {
        return getCache(cacheName).get(key, type);
    }

    public static void remove(String cacheName, String key) {
        getCache(cacheName).evictIfPresent(key);
    }

    public static boolean removeIfPresent(String cacheName, String key) {
        return getCache(cacheName).evictIfPresent(key);
    }

    public static void clear(String cacheName) {
        getCache(cacheName).clear();
    }

    private CacheUtils() {
    }
}
