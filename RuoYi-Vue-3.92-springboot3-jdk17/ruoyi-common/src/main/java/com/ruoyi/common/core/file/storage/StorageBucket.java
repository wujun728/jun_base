package com.ruoyi.common.core.file.storage;

import java.io.InputStream;
import java.net.URL;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public class StorageBucket {
    private String url;
    private String accessKey;
    private String secretKey;
    private String bucketName;
    private String permission;
    private String region;

    public void put(String filePath, MultipartFile file) throws Exception {}
    public void remove(String filePath) throws Exception {}
    public StorageEntity get(String filePath) throws Exception { return null; }
    public URL generatePresignedUrl(String filePath, int expireTime) throws Exception { return null; }
    public URL generatePublicUrl(String filePath) throws Exception { return null; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getAccessKey() { return accessKey; }
    public void setAccessKey(String accessKey) { this.accessKey = accessKey; }
    public String getSecretKey() { return secretKey; }
    public void setSecretKey(String secretKey) { this.secretKey = secretKey; }
    public String getBucketName() { return bucketName; }
    public void setBucketName(String bucketName) { this.bucketName = bucketName; }
    public String getPermission() { return permission; }
    public void setPermission(String permission) { this.permission = permission; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
}
