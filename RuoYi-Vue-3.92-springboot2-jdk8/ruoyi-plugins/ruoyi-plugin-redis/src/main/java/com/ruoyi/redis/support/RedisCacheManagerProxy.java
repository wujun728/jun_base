package com.ruoyi.redis.support;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import com.ruoyi.common.core.cache.TtlCacheManager;
import com.ruoyi.common.utils.StringUtils;

public class RedisCacheManagerProxy implements TtlCacheManager {

    RedisCacheManager redisCacheManager;
    RedisTemplate<Object, Object> redisTemplate;

    public RedisCacheManagerProxy(RedisCacheManager redisCacheManager, RedisTemplate<Object, Object> redisTemplate) {
        this.redisCacheManager = redisCacheManager;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public <T> void setCacheObject(String cacheName, String key, T value) {
        redisTemplate.opsForValue().set(cacheName + ":" + key, value);
    }

    @Override
    public <T> void setCacheObject(String cacheName, String key, T value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(cacheName + ":" + key, value, timeout, timeUnit);
    }

    @Override
    public Set<String> getCachekeys(Cache cache) {
        Set<String> keyset = new HashSet<>();
        Set<Object> keysets = redisTemplate.keys(cache.getName() + "*");
        for (Object s : keysets) {
            keyset.add(StringUtils.replace(s.toString(), cache.getName() + ":", ""));
        }
        return keyset;
    }

    @Override
    public Cache getCache(String arg0) {
        return redisCacheManager.getCache(arg0);
    }

    @Override
    public Collection<String> getCacheNames() {
        return redisCacheManager.getCacheNames();
    }
}
