package com.ruoyi.ehcache.support;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import org.ehcache.config.CacheRuntimeConfiguration;
import org.ehcache.spi.loaderwriter.BulkCacheLoadingException;
import org.ehcache.spi.loaderwriter.BulkCacheWritingException;
import org.ehcache.spi.loaderwriter.CacheLoadingException;
import org.ehcache.spi.loaderwriter.CacheWritingException;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.lang.Nullable;

import com.ruoyi.common.core.cache.TimedValue;

/**
 * Spring Cache 包装器,用于 Ehcache 原生缓存
 * 
 * @author ruoyi
 */
public class EhcacheNativeCache extends AbstractValueAdaptingCache implements org.ehcache.Cache<Object, Object> {

    private final org.ehcache.Cache<Object, Object> cache;
    private final String name;

    public EhcacheNativeCache(String name, org.ehcache.Cache<Object, Object> cache) {
        super(true);
        this.name = name;
        this.cache = cache;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public org.ehcache.Cache<Object, Object> getNativeCache() {
        return this.cache;
    }

    @Override
    @Nullable
    protected Object lookup(Object key) {
        Object raw = cache.get(key);
        if (raw instanceof TimedValue<?>) {
            return ((TimedValue<?>) raw).getData();
        }
        return raw;
    }

    @Override
    public void put(Object key, @Nullable Object value) {
        cache.put(key, toStoreValue(value));
    }

    @Override
    @Nullable
    public ValueWrapper putIfAbsent(Object key, @Nullable Object value) {
        Object existing = cache.putIfAbsent(key, toStoreValue(value));
        return toValueWrapper(existing);
    }

    @Override
    public void evict(Object key) {
        cache.remove(key);
    }

    @Override
    public boolean evictIfPresent(Object key) {
        Object existing = cache.get(key);
        if (existing != null) {
            cache.remove(key);
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    @Nullable
    public <T> T get(Object key, Callable<T> valueLoader) {
        @SuppressWarnings("unchecked")
        T value = (T) lookup(key);
        if (value != null) {
            return value;
        }

        // 使用同步块避免重复加载
        synchronized (this.cache) {
            @SuppressWarnings("unchecked")
            T cachedValue = (T) lookup(key);
            if (cachedValue != null) {
                return cachedValue;
            }

            try {
                T loadedValue = valueLoader.call();
                put(key, loadedValue);
                return loadedValue;
            } catch (Exception ex) {
                throw new ValueRetrievalException(key, valueLoader, ex);
            }
        }
    }

    /**
     * 获取缓存中的所有 key
     */
    public Iterable<Entry<Object, Object>> getAll() {
        return () -> cache.iterator();
    }

    @Override
    public boolean containsKey(Object arg0) {
        return this.cache.containsKey(arg0);
    }

    @Override
    public Map<Object, Object> getAll(Set<? extends Object> arg0) throws BulkCacheLoadingException {
        return this.cache.getAll(arg0);
    }

    @Override
    public CacheRuntimeConfiguration<Object, Object> getRuntimeConfiguration() {
        return this.cache.getRuntimeConfiguration();
    }

    @Override
    public Iterator<Entry<Object, Object>> iterator() {
        return this.cache.iterator();
    }

    @Override
    public void putAll(Map<? extends Object, ? extends Object> arg0) throws BulkCacheWritingException {
        this.cache.putAll(arg0);
    }

    @Override
    public void remove(Object arg0) throws CacheWritingException {
        this.cache.remove(arg0);
    }

    @Override
    public boolean remove(Object arg0, Object arg1) throws CacheWritingException {
        return this.cache.remove(arg0, arg1);
    }

    @Override
    public void removeAll(Set<? extends Object> arg0) throws BulkCacheWritingException {
        this.cache.removeAll(arg0);
    }

    @Override
    public Object replace(Object arg0, Object arg1) throws CacheLoadingException, CacheWritingException {
        return this.cache.replace(arg0, arg1);
    }

    @Override
    public boolean replace(Object arg0, Object arg1, Object arg2) throws CacheLoadingException, CacheWritingException {
        return this.cache.replace(arg0, arg1, arg2);
    }
}
