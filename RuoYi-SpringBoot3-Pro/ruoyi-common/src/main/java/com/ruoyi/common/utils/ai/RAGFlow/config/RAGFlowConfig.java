package com.ruoyi.common.utils.ai.RAGFlow.config;

import com.ruoyi.common.utils.ai.RAGFlow.RAGFlow;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RAGFlow 配置类
 * 支持通过 application.yml 配置 RAGFlow 客户端
 *
 * 配置示例：
 * ragflow:
 *   base-url: http://localhost:9222
 *   api-key: your-api-key
 *   timeout: 30
 */
@Configuration
@ConfigurationProperties(prefix = "ragflow")
@ConditionalOnProperty(prefix = "ragflow", name = "base-url")
public class RAGFlowConfig {

    /**
     * RAGFlow 服务器地址
     */
    private String baseUrl;

    /**
     * RAGFlow API密钥
     */
    private String apiKey;

    /**
     * 请求超时时间（秒）
     */
    private int timeout = 30;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * 创建 RAGFlow 客户端 Bean
     *
     * @return RAGFlow 实例
     */
    @Bean
    public RAGFlow ragFlowClient() {
        RAGFlow ragFlow = new RAGFlow(baseUrl, apiKey);
        ragFlow.setTimeout(timeout);
        return ragFlow;
    }

    /**
     * 静态创建 RAGFlow 客户端（不通过Spring）
     *
     * @param baseUrl 服务器地址
     * @param apiKey  API密钥
     * @return RAGFlow 实例
     */
    public static RAGFlow createClient(String baseUrl, String apiKey) {
        return new RAGFlow(baseUrl, apiKey);
    }

    /**
     * 静态创建 RAGFlow 客户端（带超时配置）
     *
     * @param baseUrl 服务器地址
     * @param apiKey  API密钥
     * @param timeout 超时时间（秒）
     * @return RAGFlow 实例
     */
    public static RAGFlow createClient(String baseUrl, String apiKey, int timeout) {
        RAGFlow ragFlow = new RAGFlow(baseUrl, apiKey);
        ragFlow.setTimeout(timeout);
        return ragFlow;
    }
}
