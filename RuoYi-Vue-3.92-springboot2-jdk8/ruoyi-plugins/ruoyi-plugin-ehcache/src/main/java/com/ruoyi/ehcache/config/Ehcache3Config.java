package com.ruoyi.ehcache.config;

import java.io.File;

import org.ehcache.PersistentCacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ruoyi.common.core.cache.TtlCacheManager;
import com.ruoyi.ehcache.serializer.FastJson2JsonEhcacheSerializer;
import com.ruoyi.ehcache.support.EhcacheNativeCacheManager;
import com.ruoyi.ehcache.support.PerEntryExpiryPolicy;

import lombok.extern.slf4j.Slf4j;

/**
 * Ehcache 3 閰嶇疆绫?鏀寔鎸佷箙鍖?
 * 
 * @author ruoyi
 */
@Configuration
@ConditionalOnProperty(prefix = "spring.cache", name = { "type" }, havingValue = "jcache", matchIfMissing = false)
@Slf4j
public class Ehcache3Config {

    private EhcacheNativeCacheManager nativeCacheManager;
    private String[] cacheNames = new String[] {
            "temp_cache", "eternal_cache", "sys_dict", "sys_config",
            "repeat_submit", "captcha_codes", "login_tokens",
            "ip_err_cnt_key", "rate_limit", "pwd_err_cnt"
    };

    @Bean
    public TtlCacheManager ehcacheManager() {
        File persistenceDir = new File(System.getProperty("user.home"), ".ehcache/ruoyi-cache");
        CacheManagerBuilder<PersistentCacheManager> builder = CacheManagerBuilder
                .newCacheManagerBuilder()
                .with(CacheManagerBuilder.persistence(persistenceDir));

        for (String cacheName : cacheNames) {
            CacheConfiguration<Object, Object> cacheConfig = CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class,
                            ResourcePoolsBuilder.newResourcePoolsBuilder()
                                    .heap(100, EntryUnit.ENTRIES)
                                    .disk(500, MemoryUnit.MB, true))
                    .withExpiry(new PerEntryExpiryPolicy())
                    .withKeySerializer(FastJson2JsonEhcacheSerializer.class)
                    .withValueSerializer(FastJson2JsonEhcacheSerializer.class)
                    .build();

            builder = builder.withCache(cacheName, cacheConfig);
        }
        this.nativeCacheManager = new EhcacheNativeCacheManager(builder.build(true));
        return this.nativeCacheManager;
    }
}