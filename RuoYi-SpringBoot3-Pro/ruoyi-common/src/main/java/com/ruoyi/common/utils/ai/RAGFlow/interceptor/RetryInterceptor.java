package com.ruoyi.common.utils.ai.RAGFlow.interceptor;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

import java.util.HashSet;
import java.util.Set;

/**
 * 重试拦截器
 * 在请求失败时自动重试
 *
 * @author RuoYi-SpringBoot3-Pro
 */
public class RetryInterceptor implements Interceptor {

    /**
     * 最大重试次数
     */
    private int maxRetries = 3;

    /**
     * 重试间隔（毫秒）
     */
    private long retryInterval = 1000;

    /**
     * 可重试的HTTP状态码
     */
    private Set<Integer> retryableStatusCodes = new HashSet<>(Set.of(
            500, 502, 503, 504,  // 服务端错误
            429                   // 请求过多
    ));

    /**
     * 可重试的异常类型
     */
    private Set<Class<? extends Exception>> retryableExceptions = new HashSet<>(Set.of(
            java.net.SocketTimeoutException.class,
            java.net.ConnectException.class,
            java.net.UnknownHostException.class,
            java.io.IOException.class,
            cn.hutool.http.HttpException.class
    ));

    public RetryInterceptor() {
        this(3, 1000);
    }

    public RetryInterceptor(int maxRetries, long retryInterval) {
        this.maxRetries = maxRetries;
        this.retryInterval = retryInterval;
    }

    @Override
    public boolean interceptRequest(HttpRequest request, InterceptorContext context) {
        context.setMaxRetries(maxRetries);
        return true;
    }

    @Override
    public HttpResponse interceptResponse(HttpResponse response, InterceptorContext context) {
        return response;
    }

    @Override
    public boolean interceptException(Exception exception, InterceptorContext context) {
        // 检查是否可以重试
        if (!context.canRetry()) {
            logMaxRetriesExceeded(context, exception);
            return true; // 继续抛出异常
        }

        // 检查是否是可重试的异常
        if (isRetryableException(exception)) {
            context.markRetrying();

            logRetry(context, exception);

            // 等待后重试
            try {
                Thread.sleep(retryInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return true;
            }

            // 返回false表示需要重试
            return false;
        }

        return true; // 继续抛出异常
    }

    /**
     * 检查异常是否可重试
     */
    private boolean isRetryableException(Exception exception) {
        // 检查异常类型
        for (Class<? extends Exception> clazz : retryableExceptions) {
            if (clazz.isInstance(exception)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查响应状态码是否可重试
     */
    public static boolean isRetryableStatus(int statusCode) {
        // 子类可以覆盖此方法
        return false;
    }

    private void logRetry(InterceptorContext context, Exception exception) {
        System.out.printf("[RAGFlow] 重试请求: %s %s (第%d/%d次) 原因: %s%n",
                context.getMethod(), context.getUrl(),
                context.getRetryCount(), context.getMaxRetries(),
                exception.getMessage());
    }

    private void logMaxRetriesExceeded(InterceptorContext context, Exception exception) {
        System.err.printf("[RAGFlow] 重试次数耗尽: %s %s 原因: %s%n",
                context.getMethod(), context.getUrl(),
                exception.getMessage());
    }

    @Override
    public int getOrder() {
        return -100; // 优先执行
    }

    @Override
    public String getName() {
        return "RetryInterceptor";
    }

    // Builder模式
    public RetryInterceptor maxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
        return this;
    }

    public RetryInterceptor retryInterval(long retryIntervalMs) {
        this.retryInterval = retryIntervalMs;
        return this;
    }

    public RetryInterceptor addRetryableStatusCode(int statusCode) {
        this.retryableStatusCodes.add(statusCode);
        return this;
    }

    public RetryInterceptor addRetryableException(Class<? extends Exception> exceptionClass) {
        this.retryableExceptions.add(exceptionClass);
        return this;
    }

    // Getters
    public int getMaxRetries() { return maxRetries; }
    public long getRetryInterval() { return retryInterval; }
}
