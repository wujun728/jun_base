package com.ruoyi.common.utils.ai.RAGFlow.interceptor;

import com.ruoyi.common.utils.ai.RAGFlow.RAGFlow;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 拦截器上下文
 * 在拦截器链中传递信息和状态
 *
 * @author RuoYi-SpringBoot3-Pro
 */
public class InterceptorContext {

    /**
     * 客户端实例
     */
    private final RAGFlow client;

    /**
     * 请求URL
     */
    private String url;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求开始时间
     */
    private LocalDateTime startTime;

    /**
     * 请求结束时间
     */
    private LocalDateTime endTime;

    /**
     * 请求耗时（毫秒）
     */
    private Long duration;

    /**
     * 请求状态
     */
    private Status status = Status.PENDING;

    /**
     * 额外属性
     */
    private Map<String, Object> attributes = new HashMap<>();

    /**
     * 重试次数
     */
    private int retryCount = 0;

    /**
     * 最大重试次数
     */
    private int maxRetries = 3;

    /**
     * 原始请求体
     */
    private String requestBody;

    /**
     * 原始响应体
     */
    private String responseBody;

    /**
     * 请求状态枚举
     */
    public enum Status {
        PENDING,     // 等待处理
        PROCESSING,  // 处理中
        COMPLETED,   // 完成
        FAILED,      // 失败
        RETRYING     // 重试中
    }

    public InterceptorContext(RAGFlow client) {
        this.client = client;
        this.startTime = LocalDateTime.now();
    }

    // Getters and Setters
    public RAGFlow getClient() { return client; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public Long getDuration() { return duration; }
    public void setDuration(Long duration) { this.duration = duration; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public Map<String, Object> getAttributes() { return attributes; }
    public void setAttributes(Map<String, Object> attributes) { this.attributes = attributes; }

    public int getRetryCount() { return retryCount; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }

    public int getMaxRetries() { return maxRetries; }
    public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }

    public String getRequestBody() { return requestBody; }
    public void setRequestBody(String requestBody) { this.requestBody = requestBody; }

    public String getResponseBody() { return responseBody; }
    public void setResponseBody(String responseBody) { this.responseBody = responseBody; }

    /**
     * 设置属性
     */
    public InterceptorContext setAttribute(String key, Object value) {
        this.attributes.put(key, value);
        return this;
    }

    /**
     * 获取属性
     */
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String key) {
        return (T) this.attributes.get(key);
    }

    /**
     * 移除属性
     */
    public Object removeAttribute(String key) {
        return this.attributes.remove(key);
    }

    /**
     * 检查是否可以重试
     */
    public boolean canRetry() {
        return retryCount < maxRetries;
    }

    /**
     * 增加重试次数
     */
    public int incrementRetryCount() {
        return ++retryCount;
    }

    /**
     * 标记请求开始
     */
    public void markStart() {
        this.startTime = LocalDateTime.now();
        this.status = Status.PROCESSING;
    }

    /**
     * 标记请求完成
     */
    public void markComplete() {
        this.endTime = LocalDateTime.now();
        this.status = Status.COMPLETED;
        if (this.startTime != null) {
            this.duration = java.time.Duration.between(startTime, endTime).toMillis();
        }
    }

    /**
     * 标记请求失败
     */
    public void markFailed() {
        this.endTime = LocalDateTime.now();
        this.status = Status.FAILED;
        if (this.startTime != null) {
            this.duration = java.time.Duration.between(startTime, endTime).toMillis();
        }
    }

    /**
     * 标记重试
     */
    public void markRetrying() {
        this.status = Status.RETRYING;
        incrementRetryCount();
    }

    /**
     * 克隆上下文（用于重试）
     */
    public InterceptorContext clone() {
        InterceptorContext clone = new InterceptorContext(client);
        clone.setUrl(this.url);
        clone.setMethod(this.method);
        clone.setRequestBody(this.requestBody);
        clone.setMaxRetries(this.maxRetries);
        clone.setRetryCount(this.retryCount);
        clone.setAttributes(new HashMap<>(this.attributes));
        return clone;
    }

    @Override
    public String toString() {
        return "InterceptorContext{" +
                "url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", status=" + status +
                ", duration=" + duration + "ms" +
                ", retryCount=" + retryCount +
                '}';
    }
}
