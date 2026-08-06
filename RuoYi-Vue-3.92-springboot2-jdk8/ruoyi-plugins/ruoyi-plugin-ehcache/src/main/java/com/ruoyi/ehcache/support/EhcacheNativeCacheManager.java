package com.ruoyi.ehcache.support;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.springframework.cache.Cache;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;

import com.ruoyi.common.core.cache.TimedValue;
import com.ruoyi.common.core.cache.TtlCacheManager;
import com.ruoyi.ehcache.serializer.FastJson2JsonEhcacheSerializer;

/**
 * Spring CacheManager 实现,包装 Ehcache 原生 CacheManager
 * 
 * @author ruoyi
 */
public class EhcacheNativeCacheManager extends AbstractTransactionSupportingCacheManager
        implements TtlCacheManager {

    private final org.ehcache.CacheManager nativeCacheManager;

    public EhcacheNativeCacheManager(org.ehcache.CacheManager nativeCacheManager) {
        this.nativeCacheManager = nativeCacheManager;
        this.setTransactionAware(true);
    }

    @Override
    public Collection<String> getCacheNames() {
        Collection<String> names = new LinkedHashSet<>();
        nativeCacheManager.getRuntimeConfiguration().getCacheConfigurations().keySet().forEach(names::add);
        return names;
    }

    public void close() {
        nativeCacheManager.close();
    }

    @Override
    public Set<String> getCachekeys(Cache cache) {
        Set<String> keyset = new LinkedHashSet<>();
        Cache target = cache;
        EhcacheNativeCache nativeCache = (EhcacheNativeCache) target;
        nativeCache.forEach(entry -> {
            if (entry.getKey() != null) {
                keyset.add(String.valueOf(entry.getKey()));
            }
        });
        return keyset;
    }

    @Override
    public <T> void setCacheObject(String cacheName, String key, T value) {
        Cache cache = getCache(cacheName);
        if (cache != null) {
            cache.put(key, value);
        }
    }

    @Override
    public <T> void setCacheObject(String cacheName, String key, T value, long timeout, TimeUnit timeUnit) {
        Cache cache = getCache(cacheName);
        long ttlMillis = timeUnit.toMillis(timeout);
        cache.put(key, new TimedValue<>(value, ttlMillis));
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
        Set<Cache> caches = new LinkedHashSet<>();
        nativeCacheManager.getRuntimeConfiguration().getCacheConfigurations().keySet().forEach(name -> {
            org.ehcache.Cache<Object, Object> nativeCache = nativeCacheManager.getCache(name, Object.class,
                    Object.class);
            caches.add(new EhcacheNativeCache(name, nativeCache));
        });
        return caches;
    }

    @Override
    protected Cache getMissingCache(String name) {
        CacheConfiguration<Object, Object> cacheConfig = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(Object.class, Object.class,
                        ResourcePoolsBuilder.newResourcePoolsBuilder()
                                .heap(100, EntryUnit.ENTRIES)
                                .disk(500, MemoryUnit.MB, true))
                .withExpiry(new PerEntryExpiryPolicy())
                .withKeySerializer(FastJson2JsonEhcacheSerializer.class)
                .withValueSerializer(FastJson2JsonEhcacheSerializer.class)
                .build();
        org.ehcache.Cache<Object, Object> cache = nativeCacheManager.createCache(name, cacheConfig);
        return new EhcacheNativeCache(name, cache);
    }
}
