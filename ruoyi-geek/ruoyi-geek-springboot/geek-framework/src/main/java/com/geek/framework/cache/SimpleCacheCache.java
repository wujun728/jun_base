package com.geek.framework.cache;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;

import org.jspecify.annotations.Nullable;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.core.serializer.support.SerializationDelegate;

import com.geek.common.core.cache.TimedValue;

@SuppressWarnings("unchecked")
public class SimpleCacheCache extends ConcurrentMapCache {

    public SimpleCacheCache(String name) {
        super(name);
    }

    protected SimpleCacheCache(String name, ConcurrentMap<Object, Object> store, boolean allowNullValues,
            @Nullable SerializationDelegate serialization) {
        super(name, store, allowNullValues, serialization);
    }

    @Nullable
    protected Object lookup(Object key) {
        Object raw = super.lookup(key);
        if (raw instanceof TimedValue<?>) {
            TimedValue<?> timedValue = (TimedValue<?>) raw;
            if (timedValue.isExpired()) {
                this.evict(key);
                return null;
            }
            return timedValue.getData();
        }
        return raw;
    }

    public <T> T get(Object key, Callable<T> valueLoader) {
        Object value = super.get(key, valueLoader);
        if (value instanceof TimedValue<?>) {
            TimedValue<?> timedValue = (TimedValue<?>) value;
            if (timedValue.isExpired()) {
                this.evict(key);
                return null;
            }
            return (T) timedValue.getData();
        }
        return (T) value;
    }
}
