package com.ruoyi.common.utils.ai.RAGFlow.interceptor;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志拦截器
 * 记录请求和响应的详细信息
 *
 * @author RuoYi-SpringBoot3-Pro
 */
public class LoggingInterceptor implements Interceptor {

    private static final Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);

    /**
     * 是否记录响应体
     */
    private boolean logResponseBody = false;

    /**
     * 是否记录慢请求（阈值毫秒）
     */
    private long slowRequestThreshold = 1000;

    public LoggingInterceptor() {
    }

    public LoggingInterceptor(boolean logResponseBody) {
        this.logResponseBody = logResponseBody;
    }

    @Override
    public boolean interceptRequest(HttpRequest request, InterceptorContext context) {
        String url = request.getUrl();
        String method = request.getMethod().name();

        context.setUrl(url);
        context.setMethod(method);
        // HttpRequest.body() 是设置方法，无法直接获取请求体
        context.setRequestBody(null);

        log.info("[RAGFlow] 请求: {} {}", method, url);

        return true;
    }

    @Override
    public HttpResponse interceptResponse(HttpResponse response, InterceptorContext context) {
        context.markComplete();

        String url = context.getUrl();
        long duration = context.getDuration() != null ? context.getDuration() : 0;
        int status = response.getStatus();

        // 记录请求耗时
        if (duration >= slowRequestThreshold) {
            log.warn("[RAGFlow] 慢请求: {} {} 耗时: {}ms 状态: {}",
                    context.getMethod(), url, duration, status);
        } else {
            log.info("[RAGFlow] 响应: {} {} 耗时: {}ms 状态: {}",
                    context.getMethod(), url, duration, status);
        }

        // 记录响应体（如果需要）
        if (logResponseBody && response != null && response.getStatus() == 200) {
            String body = response.body();
            if (body != null && body.length() > 1000) {
                log.debug("[RAGFlow] 响应体: {}... (省略{}字符)",
                        body.substring(0, 1000), body.length() - 1000);
            } else if (body != null) {
                log.debug("[RAGFlow] 响应体: {}", body);
            }
        }

        return response;
    }

    @Override
    public boolean interceptException(Exception exception, InterceptorContext context) {
        context.markFailed();

        log.error("[RAGFlow] 请求异常: {} {} 错误: {}",
                context.getMethod(), context.getUrl(), exception.getMessage(), exception);

        // 继续抛出异常
        return true;
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE; // 最后执行
    }

    @Override
    public String getName() {
        return "LoggingInterceptor";
    }

    // Builder模式
    public LoggingInterceptor logResponseBody(boolean logResponseBody) {
        this.logResponseBody = logResponseBody;
        return this;
    }

    public LoggingInterceptor slowRequestThreshold(long thresholdMs) {
        this.slowRequestThreshold = thresholdMs;
        return this;
    }
}
