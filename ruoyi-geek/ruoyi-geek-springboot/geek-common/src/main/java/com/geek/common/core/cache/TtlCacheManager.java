package com.geek.common.core.cache;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

public interface TtlCacheManager extends CacheManager {

    public <T> void setCacheObject(final String cacheName, final String key, final T value);

    public <T> void setCacheObject(final String cacheName, final String key, final T value, final long timeout,
            final TimeUnit timeUnit);

    public Set<String> getCachekeys(final Cache cache);
}
