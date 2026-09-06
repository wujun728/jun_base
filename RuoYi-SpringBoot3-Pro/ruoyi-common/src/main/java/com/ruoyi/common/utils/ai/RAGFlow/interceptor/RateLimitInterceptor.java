package com.ruoyi.common.utils.ai.RAGFlow.interceptor;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 限流拦截器
 * 限制请求频率，防止被限流
 *
 * @author RuoYi-SpringBoot3-Pro
 */
public class RateLimitInterceptor implements Interceptor {

    /**
     * 默认每秒最大请求数
     */
    private static final int DEFAULT_MAX_REQUESTS_PER_SECOND = 10;

    /**
     * 默认每分钟最大请求数
     */
    private static final int DEFAULT_MAX_REQUESTS_PER_MINUTE = 200;

    /**
     * 请求时间窗口（秒）
     */
    private static final long WINDOW_SIZE_SECONDS = 60;

    private final Map<String, RateLimitBucket> buckets = new ConcurrentHashMap<>();

    /**
     * 每秒最大请求数
     */
    private int maxRequestsPerSecond = DEFAULT_MAX_REQUESTS_PER_SECOND;

    /**
     * 每分钟最大请求数
     */
    private int maxRequestsPerMinute = DEFAULT_MAX_REQUESTS_PER_MINUTE;

    /**
     * 限流处理策略
     */
    private RateLimitStrategy strategy = RateLimitStrategy.WAIT_AND_RETRY;

    /**
     * 等待时间（毫秒）
     */
    private long waitTimeMs = 100;

    /**
     * 限流策略枚举
     */
    public enum RateLimitStrategy {
        /** 等待后重试 */
        WAIT_AND_RETRY,
        /** 直接失败 */
        FAIL_FAST,
        /** 跳过请求 */
        SKIP
    }

    public RateLimitInterceptor() {
        this(DEFAULT_MAX_REQUESTS_PER_SECOND, DEFAULT_MAX_REQUESTS_PER_MINUTE);
    }

    public RateLimitInterceptor(int maxRequestsPerSecond, int maxRequestsPerMinute) {
        this.maxRequestsPerSecond = maxRequestsPerSecond;
        this.maxRequestsPerMinute = maxRequestsPerMinute;
    }

    @Override
    public boolean interceptRequest(HttpRequest request, InterceptorContext context) {
        String key = getBucketKey(request);
        RateLimitBucket bucket = buckets.computeIfAbsent(key, k -> new RateLimitBucket());

        if (!bucket.tryAcquire(maxRequestsPerSecond, maxRequestsPerMinute)) {
            System.err.println("[RAGFlow] 触发限流: " + context.getUrl());

            switch (strategy) {
                case WAIT_AND_RETRY:
                    try {
                        Thread.sleep(waitTimeMs);
                        // 重试一次
                        return bucket.tryAcquire(maxRequestsPerSecond, maxRequestsPerMinute);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return false;
                    }
                case FAIL_FAST:
                    return false;
                case SKIP:
                    return false;
            }
        }
        return true;
    }

    @Override
    public HttpResponse interceptResponse(HttpResponse response, InterceptorContext context) {
        return response;
    }

    @Override
    public boolean interceptException(Exception exception, InterceptorContext context) {
        return true;
    }

    @Override
    public int getOrder() {
        return -90;
    }

    @Override
    public String getName() {
        return "RateLimitInterceptor";
    }

    private String getBucketKey(HttpRequest request) {
        // 根据API端点分组限流
        String url = request.getUrl();
        if (url.contains("/chats/")) {
            return "chat";
        } else if (url.contains("/datasets/")) {
            return "dataset";
        } else if (url.contains("/documents/")) {
            return "document";
        } else if (url.contains("/retrieval")) {
            return "retrieval";
        }
        return "default";
    }

    // Builder模式
    public RateLimitInterceptor maxRequestsPerSecond(int maxRequestsPerSecond) {
        this.maxRequestsPerSecond = maxRequestsPerSecond;
        return this;
    }

    public RateLimitInterceptor maxRequestsPerMinute(int maxRequestsPerMinute) {
        this.maxRequestsPerMinute = maxRequestsPerMinute;
        return this;
    }

    public RateLimitInterceptor strategy(RateLimitStrategy strategy) {
        this.strategy = strategy;
        return this;
    }

    public RateLimitInterceptor waitTimeMs(long waitTimeMs) {
        this.waitTimeMs = waitTimeMs;
        return this;
    }

    /**
     * 速率限制桶
     */
    private static class RateLimitBucket {
        private final AtomicInteger secondCounter = new AtomicInteger(0);
        private final AtomicInteger minuteCounter = new AtomicInteger(0);
        private volatile long secondStart = Instant.now().getEpochSecond();
        private volatile long minuteStart = Instant.now().getEpochSecond();

        public synchronized boolean tryAcquire(int maxPerSecond, int maxPerMinute) {
            long now = Instant.now().getEpochSecond();

            // 重置秒计数器
            if (now > secondStart) {
                secondStart = now;
                secondCounter.set(0);
            }

            // 重置分钟计数器
            if (now > minuteStart) {
                minuteStart = now;
                minuteCounter.set(0);
            }

            // 检查限制
            if (secondCounter.get() >= maxPerSecond) {
                return false;
            }
            if (minuteCounter.get() >= maxPerMinute) {
                return false;
            }

            // 增加计数器
            secondCounter.incrementAndGet();
            minuteCounter.incrementAndGet();

            return true;
        }
    }
}
