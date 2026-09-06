package com.ruoyi.common.utils.ai.RAGFlow.interceptor;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 指标收集拦截器
 * 收集API调用的统计指标
 *
 * @author RuoYi-SpringBoot3-Pro
 */
public class MetricsInterceptor implements Interceptor {

    /**
     * 请求计数
     */
    private final Map<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();

    /**
     * 成功计数
     */
    private final Map<String, AtomicInteger> successCounts = new ConcurrentHashMap<>();

    /**
     * 失败计数
     */
    private final Map<String, AtomicInteger> failureCounts = new ConcurrentHashMap<>();

    /**
     * 总耗时
     */
    private final Map<String, AtomicLong> totalDurations = new ConcurrentHashMap<>();

    /**
     * 最大耗时
     */
    private final Map<String, AtomicLong> maxDurations = new ConcurrentHashMap<>();

    /**
     * API端点分类
     */
    private String getApiCategory(String url) {
        if (url.contains("/chats") && url.contains("/completions")) {
            return "chat_completion";
        } else if (url.contains("/chats_openai")) {
            return "chat_openai";
        } else if (url.contains("/agents_openai")) {
            return "agent_openai";
        } else if (url.contains("/datasets")) {
            if (url.contains("/documents")) {
                return "document";
            } else if (url.contains("/chunks")) {
                return "chunk";
            } else if (url.contains("/retrieval")) {
                return "retrieval";
            }
            return "dataset";
        } else if (url.contains("/agents")) {
            if (url.contains("/sessions")) {
                return "agent_session";
            } else if (url.contains("/completions")) {
                return "agent_completion";
            }
            return "agent";
        } else if (url.contains("/memories")) {
            return "memory";
        } else if (url.contains("/messages")) {
            return "message";
        } else if (url.contains("/file")) {
            return "file";
        } else if (url.contains("/system/healthz")) {
            return "health";
        }
        return "other";
    }

    @Override
    public boolean interceptRequest(HttpRequest request, InterceptorContext context) {
        String category = getApiCategory(request.getUrl());
        requestCounts.computeIfAbsent(category, k -> new AtomicInteger(0)).incrementAndGet();
        return true;
    }

    @Override
    public HttpResponse interceptResponse(HttpResponse response, InterceptorContext context) {
        String category = getApiCategory(context.getUrl());

        if (response.getStatus() >= 200 && response.getStatus() < 300) {
            successCounts.computeIfAbsent(category, k -> new AtomicInteger(0)).incrementAndGet();
        } else {
            failureCounts.computeIfAbsent(category, k -> new AtomicInteger(0)).incrementAndGet();
        }

        // 记录耗时
        if (context.getDuration() != null) {
            long duration = context.getDuration();
            totalDurations.computeIfAbsent(category, k -> new AtomicLong(0)).addAndGet(duration);
            maxDurations.computeIfAbsent(category, k -> new AtomicLong(0))
                    .updateAndGet(current -> Math.max(current, duration));
        }

        return response;
    }

    @Override
    public boolean interceptException(Exception exception, InterceptorContext context) {
        String category = getApiCategory(context.getUrl());
        failureCounts.computeIfAbsent(category, k -> new AtomicInteger(0)).incrementAndGet();
        return true;
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE - 1;
    }

    @Override
    public String getName() {
        return "MetricsInterceptor";
    }

    /**
     * 获取指标快照
     */
    public MetricsSnapshot getSnapshot() {
        return new MetricsSnapshot(
                new ConcurrentHashMap<>(requestCounts),
                new ConcurrentHashMap<>(successCounts),
                new ConcurrentHashMap<>(failureCounts),
                new ConcurrentHashMap<>(totalDurations),
                new ConcurrentHashMap<>(maxDurations)
        );
    }

    /**
     * 重置所有指标
     */
    public void reset() {
        requestCounts.clear();
        successCounts.clear();
        failureCounts.clear();
        totalDurations.clear();
        maxDurations.clear();
    }

    /**
     * 指标快照
     */
    public static class MetricsSnapshot {
        private final Map<String, AtomicInteger> requestCounts;
        private final Map<String, AtomicInteger> successCounts;
        private final Map<String, AtomicInteger> failureCounts;
        private final Map<String, AtomicLong> totalDurations;
        private final Map<String, AtomicLong> maxDurations;

        public MetricsSnapshot(Map<String, AtomicInteger> requestCounts,
                                Map<String, AtomicInteger> successCounts,
                                Map<String, AtomicInteger> failureCounts,
                                Map<String, AtomicLong> totalDurations,
                                Map<String, AtomicLong> maxDurations) {
            this.requestCounts = requestCounts;
            this.successCounts = successCounts;
            this.failureCounts = failureCounts;
            this.totalDurations = totalDurations;
            this.maxDurations = maxDurations;
        }

        public int getTotalRequests() {
            return requestCounts.values().stream().mapToInt(AtomicInteger::get).sum();
        }

        public int getTotalSuccess() {
            return successCounts.values().stream().mapToInt(AtomicInteger::get).sum();
        }

        public int getTotalFailures() {
            return failureCounts.values().stream().mapToInt(AtomicInteger::get).sum();
        }

        public double getAverageDuration(String category) {
            AtomicInteger count = requestCounts.get(category);
            AtomicLong total = totalDurations.get(category);
            if (count != null && total != null && count.get() > 0) {
                return (double) total.get() / count.get();
            }
            return 0;
        }

        public long getMaxDuration(String category) {
            AtomicLong max = maxDurations.get(category);
            return max != null ? max.get() : 0;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("=== RAGFlow Metrics ===\n");
            sb.append("总请求: ").append(getTotalRequests()).append("\n");
            sb.append("成功: ").append(getTotalSuccess()).append("\n");
            sb.append("失败: ").append(getTotalFailures()).append("\n");

            for (String category : requestCounts.keySet()) {
                int requests = requestCounts.getOrDefault(category, new AtomicInteger(0)).get();
                int success = successCounts.getOrDefault(category, new AtomicInteger(0)).get();
                double avgDuration = getAverageDuration(category);
                long maxDuration = getMaxDuration(category);

                sb.append(String.format("%s: req=%d success=%d avg=%.2fms max=%dms%n",
                        category, requests, success, avgDuration, maxDuration));
            }
            return sb.toString();
        }
    }
}
