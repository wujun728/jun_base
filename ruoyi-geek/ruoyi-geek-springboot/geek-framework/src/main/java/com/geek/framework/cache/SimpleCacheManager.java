package com.geek.framework.cache;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.jspecify.annotations.Nullable;
import org.springframework.cache.Cache;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;
import org.springframework.cache.transaction.TransactionAwareCacheDecorator;
import org.springframework.core.serializer.support.SerializationDelegate;

import com.geek.common.core.cache.TimedValue;
import com.geek.common.core.cache.TtlCacheManager;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class SimpleCacheManager extends AbstractTransactionSupportingCacheManager implements TtlCacheManager {

    @Nullable
    private SerializationDelegate serialization;
    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap(16);
    private volatile boolean dynamic = true;
    private boolean allowNullValues = true;
    private boolean storeByValue = false;

    public SimpleCacheManager() {
    }

    public SimpleCacheManager(String... cacheNames) {
        this.setCacheNames(Arrays.asList(cacheNames));
    }

    @Override
    public <T> void setCacheObject(String cacheName, String key, T value) {
        this.getCache(cacheName).put(key, value);
    }

    @Override
    public <T> void setCacheObject(String cacheName, String key, T value, long timeout, TimeUnit timeUnit) {
        long ttlMillis = timeUnit.toMillis(timeout);
        this.getCache(cacheName).put(key, new TimedValue<>(value, ttlMillis));
    }

    @Override
    public Set<String> getCachekeys(Cache cache) {
        Set<String> keyset = new HashSet<>();

        Cache target = cache;
        if (target instanceof TransactionAwareCacheDecorator) {
            target = ((TransactionAwareCacheDecorator) target).getTargetCache();
        }

        if (target instanceof SimpleCacheCache) {
            Object nativeCache = ((SimpleCacheCache) target).getNativeCache();
            if (nativeCache instanceof ConcurrentMap) {
                ConcurrentMap map = (ConcurrentMap) nativeCache;
                for (Object k : map.keySet()) {
                    if (k != null) {
                        keyset.add(String.valueOf(k));
                    }
                }
            }
        }

        return keyset;
    }

    @Override
    public Cache getCache(String name) {
        Cache cache = (Cache) this.cacheMap.get(name);
        if (cache == null && this.dynamic) {
            cache = (Cache) this.cacheMap.computeIfAbsent(name, this::createConcurrentMapCache);
        }

        return cache;
    }

    @Override
    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(this.cacheMap.keySet());
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
        return this.cacheMap.values();
    }

    @Nullable
    @Override
    protected Cache getMissingCache(String name) {
        return this.dynamic ? this.createConcurrentMapCache(name) : null;
    }

    public void setCacheNames(@Nullable Collection<String> cacheNames) {
        if (cacheNames != null) {
            Iterator var2 = cacheNames.iterator();

            while (var2.hasNext()) {
                String name = (String) var2.next();
                this.cacheMap.put(name, this.createConcurrentMapCache(name));
            }

            this.dynamic = false;
        } else {
            this.dynamic = true;
        }

    }

    public void setAllowNullValues(boolean allowNullValues) {
        if (allowNullValues != this.allowNullValues) {
            this.allowNullValues = allowNullValues;
            this.recreateCaches();
        }

    }

    public boolean isAllowNullValues() {
        return this.allowNullValues;
    }

    public void setStoreByValue(boolean storeByValue) {
        if (storeByValue != this.storeByValue) {
            this.storeByValue = storeByValue;
            this.recreateCaches();
        }

    }

    public boolean isStoreByValue() {
        return this.storeByValue;
    }

    public void setBeanClassLoader(ClassLoader classLoader) {
        this.serialization = new SerializationDelegate(classLoader);
        if (this.isStoreByValue()) {
            this.recreateCaches();
        }

    }

    public void resetCaches() {
        this.cacheMap.values().forEach(Cache::clear);
        if (this.dynamic) {
            this.cacheMap.clear();
        }

    }

    public void removeCache(String name) {
        this.cacheMap.remove(name);
    }

    private void recreateCaches() {
        Iterator var1 = this.cacheMap.entrySet().iterator();

        while (var1.hasNext()) {
            Map.Entry<String, Cache> entry = (Map.Entry) var1.next();
            entry.setValue(this.createConcurrentMapCache((String) entry.getKey()));
        }

    }

    protected Cache createConcurrentMapCache(String name) {
        SerializationDelegate actualSerialization = this.isStoreByValue() ? this.serialization : null;
        return new SimpleCacheCache(name, new ConcurrentHashMap(256), this.isAllowNullValues(), actualSerialization);
    }

}
