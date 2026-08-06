package com.ruoyi.oss.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package com.ruoyi.oss.config
 * @title:
 * @description: 阿里云配置 * @date: 2018/4/26 9:39
 * @copyright: 2017 www.sunseagear.com Inc. All rights reserved.
 */
@ConfigurationProperties(prefix = "oss")
public class AliyunConfig {
    //阿里云api的内或外网域名
    String endpoint = "";
    //阿里云api的密钥access key id
    String accessKeyId = "";
    //阿里云api的密钥access key secret
    String accessKeySecret = "";
    //阿里云api的bucket名称
    String bucketName = "";
    //外部访问域名
    String domain = "";

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
