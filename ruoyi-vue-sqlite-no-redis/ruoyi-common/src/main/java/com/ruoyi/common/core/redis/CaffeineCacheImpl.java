package com.ruoyi.common.core.redis;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.apache.poi.ss.formula.functions.T;
import org.checkerframework.checker.index.qual.NonNegative;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * caffeine缓存实现，用来替换redis
 *
 * @author hancher
 * @date 2025/1/16 10:41
 * @since 3.8.9
 */

@Component
public class CaffeineCacheImpl implements RedisCache {

    private final int MAXIMUM_SIZE = 10000;

    @Data
    @AllArgsConstructor
    protected static class CacheObject<T> {
        private T value;
        private Duration expire;
        /**
         * 过期时间点，毫秒
         */
        private Long expireTime;

        public CacheObject(T value, Duration expire) {
            this.value = value;
            this.expire = expire;
        }
    }

    /**
     * 缓存管理器
     */
    @Getter
    private final Cache<String, CacheObject<?>> caffeineCache;



    public CaffeineCacheImpl() {
        // 默认1w个空间
        caffeineCache = Caffeine.newBuilder()
                .maximumSize(MAXIMUM_SIZE)
                .recordStats()
                .expireAfter(new Expiry<String, CacheObject<?>>() {
                    @Override
                    public long expireAfterCreate(String key, CacheObject value, long currentTime) {
                        if (Objects.isNull(value.expire)) {
                            // 模拟永不过期
                            return Long.MAX_VALUE;
                        }
                        value.expireTime = System.currentTimeMillis() + value.expire.toMillis();
                        return value.expire.toNanos();
                    }

                    @Override
                    public long expireAfterUpdate(String key, CacheObject value, long currentTime, @NonNegative long currentDuration) {
                        return currentDuration;
                    }

                    @Override
                    public long expireAfterRead(String key, CacheObject value, long currentTime, @NonNegative long currentDuration) {
                        return currentDuration;
                    }
                })
                .build();
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    @Override
    public <T> void setCacheObject(String key, T value) {
        caffeineCache.put(key, new CacheObject<>(value, null));
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param timeout  时间
     * @param timeUnit 时间颗粒度
     */
    @Override
    public <T> void setCacheObject(String key, T value, Integer timeout, TimeUnit timeUnit) {
        caffeineCache.put(key, new CacheObject<>(value, Duration.ofSeconds(timeUnit.toSeconds(timeout))));


    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间,秒
     * @return true=设置成功；false=设置失败
     */
    @Override
    public boolean expire(String key, long timeout) {
        CacheObject<?> v = caffeineCache.getIfPresent(key);
        if (Objects.isNull(v)) {
            return false;
        }
        caffeineCache.invalidate(key); // 通过重新创建缓存的方式触发crate事件，重置超时时间
        v.setExpire(Duration.ofSeconds(timeout));
        caffeineCache.put(key, v);
        return true;
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return true=设置成功；false=设置失败
     */
    @Override
    public boolean expire(String key, long timeout, TimeUnit unit) {
        return expire(key, unit.toSeconds(timeout));
    }

    /**
     * 获取有效时间
     *
     * @param key Redis键
     * @return 有效时间
     */
    @Override
    public long getExpire(String key) {
        // 与redis 一致
        if (!hasKey(key)) {
            return -1;
        }

        CacheObject<?> v = caffeineCache.getIfPresent(key);
        if (Objects.isNull(v) || Objects.isNull(v.getExpireTime())) {
            return -2;
        }
        // 增加20ms 误差，针对990毫秒返回0秒的情况
        long ttl = v.getExpireTime() - System.currentTimeMillis() + 20;
        return ttl > 0 ? ttl/1000 : 0;
    }

    /**
     * 判断 key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    @Override
    public Boolean hasKey(String key) {
        return caffeineCache.getIfPresent(key) != null;
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    @Override
    public <T> T getCacheObject(String key) {
        CacheObject<T> v = (CacheObject<T>) caffeineCache.getIfPresent(key);
        if (Objects.isNull(v)) {
            return null;
        }
        return v.getValue();
    }

    /**
     * 删除单个对象
     *
     * @param key
     */
    @Override
    public boolean deleteObject(String key) {
        caffeineCache.invalidate(key);
        return true;
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     * @return
     */
    @Override
    public boolean deleteObject(Collection collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return false;
        }

        collection.forEach(e-> caffeineCache.invalidate(Objects.toString(e)));
        return true;
    }

    /**
     * 缓存List数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    @Override
    public <T> long setCacheList(String key, List<T> dataList) {
        setCacheObject(key, dataList);

        return dataList.size();
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    @Override
    public <T> List<T> getCacheList(String key) {
        return getCacheObject(key);
    }

    /**
     * 缓存Set
     *
     * @param key     缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    @Override
    public <T> BoundSetOperations<String, T> setCacheSet(String key, Set<T> dataSet) {
        throw new UnsupportedOperationException("caffeine does not support set operation for now!");
    }

    /**
     * 获得缓存的set
     *
     * @param key
     * @return
     */
    @Override
    public <T> Set<T> getCacheSet(String key) {
        throw new UnsupportedOperationException("caffeine does not support set operation for now!");

    }

    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap 如果map需要修改，请传可修改的map类型
     */
    @Override
    public <T> void setCacheMap(String key, Map<String, T> dataMap) {
        setCacheObject(key, dataMap);
    }

    /**
     * 获得缓存的Map
     *
     * @param key
     * @return
     */
    @Override
    public <T> Map<String, T> getCacheMap(String key) {
        return getCacheObject(key);
    }

    /**
     * 往Hash中存入数据
     *
     * @param key   Redis键
     * @param hKey  Hash键
     * @param value 值
     */
    @Override
    public <T> void setCacheMapValue(String key, String hKey, T value) {
        Map<String, Object> cacheMap = getCacheMap(key);
        if (Objects.isNull(cacheMap)) {
            cacheMap = new HashMap<>();
            setCacheObject(key, cacheMap);
        }
        cacheMap.put(hKey, value);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    @Override
    public <T> T getCacheMapValue(String key, String hKey) {
        Map<String, Object> cacheMap = getCacheMap(key);
        if (Objects.nonNull(cacheMap)) {
            return (T) cacheMap.get(hKey);
        }
        return null;
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key   Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    @Override
    public <T> List<T> getMultiCacheMapValue(String key, Collection<Object> hKeys) {
        Map<String, Object> cacheMap = getCacheMap(key);
        if (Objects.nonNull(cacheMap)) {
            List<T> res = new ArrayList<>();
            for (Object hKey : hKeys) {
                res.add((T) cacheMap.get(hKey));
            }

            return res;
        }
        return null;
    }

    /**
     * 删除Hash中的某条数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return 是否成功
     */
    @Override
    public boolean deleteCacheMapValue(String key, String hKey) {
        CacheObject<Map<String, T>> v = (CacheObject<Map<String, T>>) caffeineCache.getIfPresent(key);
        if (Objects.isNull(v) || Objects.isNull(v.getValue().get(hKey))) {
            return false;
        }

        v.getValue().remove(hKey);
        caffeineCache.put(key, v);

        return true;
    }

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    @Override
    public Collection<String> keys(String pattern) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        // 这里使用缓存的所有键进行匹配（模拟 Redis 的 keys 命令）
        return caffeineCache.asMap().keySet().stream()
                .filter(key -> antPathMatcher.match(pattern, key))  // 使用正则表达式进行匹配
                .collect(Collectors.toSet());
    }

    public int getMaxSize() {
        return MAXIMUM_SIZE;
    }
}
