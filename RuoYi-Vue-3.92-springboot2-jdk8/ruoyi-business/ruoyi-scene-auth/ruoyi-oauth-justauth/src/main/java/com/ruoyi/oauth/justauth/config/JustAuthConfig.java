package com.ruoyi.oauth.justauth.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.ruoyi.oauth.justauth.utils.JustAuthUtils;

import jakarta.annotation.PostConstruct;
import me.zhyd.oauth.cache.AuthDefaultStateCache;
import me.zhyd.oauth.cache.AuthStateCache;
import me.zhyd.oauth.request.AuthRequest;

@Configuration
@ConfigurationProperties("justauth")
public class JustAuthConfig {
    private Map<String, JustAuthProperties> sources;
    private AuthStateCache authStateCache;

    @PostConstruct
    public void init() {
        authStateCache = AuthDefaultStateCache.INSTANCE;
    }

    public AuthRequest getAuthRequest(String source) {
        JustAuthProperties properties = sources.get(source);
        if (properties == null) {
            throw new IllegalArgumentException("No JustAuthProperties found for source: " + source);
        }
        return JustAuthUtils.getAuthRequest(source, properties.getClientId(), properties.getClientSecret(),
                properties.getRedirectUri(), authStateCache);
    }

    public boolean isEnabled(String source) {
        return sources.containsKey(source);
    }

    public Map<String, JustAuthProperties> getSources() {
        return sources;
    }

    public void setSources(Map<String, JustAuthProperties> sources) {
        this.sources = sources;
    }
}
