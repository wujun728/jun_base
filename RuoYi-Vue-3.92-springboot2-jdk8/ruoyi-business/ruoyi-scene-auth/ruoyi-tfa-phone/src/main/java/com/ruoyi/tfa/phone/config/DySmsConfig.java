package com.ruoyi.tfa.phone.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.ruoyi.tfa.phone.domain.DySmsTemplate;

import lombok.Data;

/**
 * 手机号认证数据
 * 
 * @author Dftre
 * @date 2024-04-16
 */
@Data
@Configuration
@ConfigurationProperties("tfa.phone.dysms")
public class DySmsConfig {
    private String accessKeyId;
    private String accessKeySecret;
    private Map<String, DySmsTemplate> template;

}