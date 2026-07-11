package com.ruoyi.common.core.file.storage;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public abstract class StorageEntity {
    private String filePath;
    private Object inputStream;
    private Long byteCount;
    private String object;
    private String region;
    private String bucket;
    private Object headers;

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public Object getInputStream() { return inputStream; }
    public void setInputStream(Object inputStream) { this.inputStream = inputStream; }
    public Long getByteCount() { return byteCount; }
    public void setByteCount(Long byteCount) { this.byteCount = byteCount; }
    public String getObject() { return object; }
    public void setObject(String object) { this.object = object; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getBucket() { return bucket; }
    public void setBucket(String bucket) { this.bucket = bucket; }
    public Object getHeaders() { return headers; }
    public void setHeaders(Object headers) { this.headers = headers; }
}
