package com.ruoyi.framework.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "alisms")
public class AlismsProperties {
    private static String key;
    private static String secret;
    private static String sign;
    private static List<String> templates;

    public static String getKey() {
        return key;
    }

    public void setKey(String key) {
        AlismsProperties.key = key;
    }

    public static String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        AlismsProperties.secret = secret;
    }

    public static String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        AlismsProperties.sign = sign;
    }

    public static List<String> getTemplates() {
        return templates;
    }

    public void setTemplates(List<String> templates) {
        AlismsProperties.templates = templates;
    }

}
