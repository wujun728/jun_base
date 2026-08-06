package com.ruoyi.file.minio.properties;

import lombok.Data;

@Data
public class MinioBucketProperties {
    private String permission;
    private String url;
    private String accessKey;
    private String secretKey;
    private String bucketName;
}
