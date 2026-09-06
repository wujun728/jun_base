package com.ruoyi.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * OpenAI 配置
 *
 * @author ruoyi
 */
@Component
@ConfigurationProperties(prefix = "openai")
public class OpenAIConfig {

    /** API密钥 */
    private static String apiKey;

    /** 基础URL */
    private static String baseUrl;

    /** 默认模型 */
    private static String model;

    /** 代理配置 */
    private static Proxy proxy;

    public static String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        OpenAIConfig.apiKey = apiKey;
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        OpenAIConfig.baseUrl = baseUrl;
    }

    public static String getModel() {
        return model;
    }

    public void setModel(String model) {
        OpenAIConfig.model = model;
    }

    public static Proxy getProxy() {
        return proxy;
    }

    public void setProxy(Proxy proxy) {
        OpenAIConfig.proxy = proxy;
    }

    /**
     * 代理配置类
     */
    public static class Proxy {
        /** 是否启用代理 */
        private boolean enabled;

        /** 代理类型 (HTTP/SOCKS) */
        private String type;

        /** 代理主机 */
        private String host;

        /** 代理端口 */
        private int port;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        /**
         * 获取 Java Proxy 对象
         */
        public java.net.Proxy toJavaProxy() {
            if (!enabled || host == null || host.isEmpty()) {
                return null;
            }
            java.net.Proxy.Type proxyType = "SOCKS".equalsIgnoreCase(type)
                    ? java.net.Proxy.Type.SOCKS
                    : java.net.Proxy.Type.HTTP;
            return new java.net.Proxy(proxyType, new java.net.InetSocketAddress(host, port));
        }
    }
}
