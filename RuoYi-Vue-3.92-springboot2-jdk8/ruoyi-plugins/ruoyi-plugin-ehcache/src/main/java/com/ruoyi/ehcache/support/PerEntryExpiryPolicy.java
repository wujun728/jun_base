package com.ruoyi.ehcache.support;

import java.time.Duration;
import java.util.function.Supplier;

import org.ehcache.expiry.ExpiryPolicy;

import com.ruoyi.common.core.cache.TimedValue;

/**
 * 根据 TimedValue 动态决定每个条目的过期时间；未包装的值视为永久存活。
 */
public class PerEntryExpiryPolicy implements ExpiryPolicy<Object, Object> {

    /** 获取创建的到期时间  */
    @Override
    public Duration getExpiryForCreation(Object key, Object value) {
        if (value instanceof TimedValue<?> tv) {
            long ttl = tv.getTtlMillis();
            return ttl > 0 ? Duration.ofMillis(ttl) : Duration.ZERO; // 立即过期
        }
        return Duration.ofMillis(Long.MAX_VALUE); // 视为无限（Ehcache 会截断为实现允许的最大值）
    }

    /** 获取访问的到期时间  */
    @Override
    public Duration getExpiryForAccess(Object key, Supplier<? extends Object> value) {
        return null; // null = 保持现有剩余寿命
    }

    /** 获取更新的到期时间  */
    @Override
    public Duration getExpiryForUpdate(Object key, Supplier<? extends Object> oldValue, Object newValue) {
        if (newValue instanceof TimedValue<?> tv) {
            long ttl = tv.getTtlMillis();
            return ttl > 0 ? Duration.ofMillis(ttl) : Duration.ZERO;
        }
        return null; // 保持旧值剩余寿命
    }
}
