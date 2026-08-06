package com.ruoyi.file.aliyun.oss.properties;

import lombok.Data;

@Data
public class AliOssBucketProperties {
    private String permission;
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
}