package com.geek.framework.cache;

import java.time.Duration;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.Cache;
import org.springframework.cache.transaction.TransactionAwareCacheDecorator;

import com.geek.common.core.cache.TimedValue;

public class SimpleCacheExpiredEntryCleaner {
    private final SimpleCacheManager cacheManager;
    private final Duration interval;
    private final int maxScanPerCache;

    private final ScheduledExecutorService executor;
    private volatile ScheduledFuture<?> future;

    SimpleCacheExpiredEntryCleaner(SimpleCacheManager cacheManager, Duration interval, int maxScanPerCache) {
        this.cacheManager = cacheManager;
        this.interval = interval.isNegative() || interval.isZero() ? Duration.ofSeconds(30) : interval;
        this.maxScanPerCache = Math.max(100, maxScanPerCache);
        this.executor = Executors.newSingleThreadScheduledExecutor(new DaemonThreadFactory("simple-cache-cleaner"));

        long delayMs = Math.max(1000L, this.interval.toMillis());
        this.future = this.executor.scheduleWithFixedDelay(this::cleanSafely, delayMs, delayMs, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        ScheduledFuture<?> localFuture = this.future;
        if (localFuture != null) {
            localFuture.cancel(false);
        }
        this.executor.shutdown();
    }

    private void cleanSafely() {
        try {
            cleanOnce();
        } catch (Throwable ignored) {
            // 清理失败不影响业务线程；下次周期再尝试
        }
    }

    private void cleanOnce() {
        for (String cacheName : cacheManager.getCacheNames()) {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache == null) {
                continue;
            }

            if (cache instanceof TransactionAwareCacheDecorator) {
                cache = ((TransactionAwareCacheDecorator) cache)
                        .getTargetCache();
            }

            if (!(cache instanceof SimpleCacheCache)) {
                continue;
            }

            ConcurrentMap<Object, Object> nativeCache = ((SimpleCacheCache) cache).getNativeCache();
            if (!(nativeCache instanceof ConcurrentMap)) {
                continue;
            }

            int scanned = 0;
            for (var entry : nativeCache.entrySet()) {
                if (++scanned > maxScanPerCache) {
                    break;
                }
                Object v = entry.getValue();
                if (v instanceof TimedValue<?> timedValue && timedValue.isExpired()) {
                    nativeCache.remove(entry.getKey(), v);
                }
            }
        }
    }

    static final class DaemonThreadFactory implements ThreadFactory {
        private final String namePrefix;
        private int index = 0;

        DaemonThreadFactory(String namePrefix) {
            this.namePrefix = namePrefix;
        }

        @Override
        public synchronized Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName(namePrefix + "-" + (++index));
            t.setDaemon(true);
            return t;
        }
    }
}