package com.ruoyi.oauth.justauth.config;

import lombok.Data;

@Data
public class JustAuthProperties {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
}
