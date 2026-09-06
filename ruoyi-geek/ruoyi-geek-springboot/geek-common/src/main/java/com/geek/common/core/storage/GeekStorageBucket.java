package com.geek.common.core.storage;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.web.multipart.MultipartFile;

import com.geek.common.core.storage.base.StorageBucket;
import com.geek.common.core.storage.base.StorageEntity;
import com.geek.common.core.storage.domain.SysFilePartETag;
import com.geek.common.utils.StringUtils;

import lombok.Getter;

@Getter
public class GeekStorageBucket implements StorageBucket {
    private final Map<String, StorageBucket> storageBucketMap = new HashMap<>();
    private final Map<String, String> sbTypeHashMap = new HashMap<>();

    private String defaultSbType;
    private String defaultStorageBucketKey;
    private StorageBucket defaultStorageBucket;

    public GeekStorageBucket(String storageBucketKey, StorageBucket storageBucket, String sbType) {
        this.defaultStorageBucketKey = storageBucketKey;
        this.defaultStorageBucket = storageBucket;
        this.defaultSbType = sbType;
        storageBucketMap.put(storageBucketKey, storageBucket);
        sbTypeHashMap.put(storageBucketKey, sbType);
    }

    public void setDefaultStorageBucket(String storageBucketKey) {
        StorageBucket storageBucket = storageBucketMap.get(storageBucketKey);
        if (Objects.isNull(storageBucket)) {
            throw new IllegalStateException("不存在该存储桶：" + storageBucketKey);
        }
        String sbType = sbTypeHashMap.get(storageBucketKey);
        this.defaultStorageBucketKey = storageBucketKey;
        this.defaultStorageBucket = storageBucket;
        this.defaultSbType = sbType;
    }

    public void addStorageBucket(String storageBucketKey, StorageBucket storageBucket, String sbType) {
        storageBucketMap.put(storageBucketKey, storageBucket);
        sbTypeHashMap.put(storageBucketKey, sbType);
    }

    public void removeDatasource(String storageBucketKey) {
        storageBucketMap.remove(storageBucketKey);
        sbTypeHashMap.remove(storageBucketKey);
    }

    public StorageBucket getStorageBucket(String storageBucketKey) {
        return storageBucketMap.get(storageBucketKey);
    }

    public String getSbType(String storageBucketKey) {
        return sbTypeHashMap.get(storageBucketKey);
    }

    protected StorageBucket getStorageBucket() {
        StorageBucket storageBucket = defaultStorageBucket;
        if (storageBucketMap.size() > 1) {
            String storageBucketKey = StorageBucketKey.get();
            if (StringUtils.isNotBlank(storageBucketKey)) {
                storageBucket = storageBucketMap.get(storageBucketKey);
            }
        }
        return storageBucket;
    }

    @Override
    public StorageEntity get(String filepath) throws Exception {
        return getStorageBucket().get(filepath);
    }

    @Override
    public void put(String filePath, MultipartFile file) throws Exception {
        getStorageBucket().put(filePath, file);
    }

    @Override
    public void remove(String filePath) throws Exception {
        getStorageBucket().remove(filePath);
    }

    @Override
    public URL generatePresignedUrl(String filePath, int expireTime) throws Exception {
        return getStorageBucket().generatePresignedUrl(filePath, expireTime);
    }

    @Override
    public URL generatePublicUrl(String filePath) throws Exception {
        return getStorageBucket().generatePublicUrl(filePath);
    }

    @Override
    public String getPermission() {
        return getStorageBucket().getPermission();
    }

    @Override
    public String initMultipartUpload(String filePath) throws Exception {
        return getStorageBucket().initMultipartUpload(filePath);
    }

    @Override
    public SysFilePartETag uploadPart(String filePath, String uploadId, int partNumber, long partSize,
            InputStream inputStream) throws Exception {
        return getStorageBucket().uploadPart(filePath, uploadId, partNumber, partSize, inputStream);
    }

    @Override
    public String completeMultipartUpload(String filePath, String uploadId, List<SysFilePartETag> partETags)
            throws Exception {
        return getStorageBucket().completeMultipartUpload(filePath, uploadId, partETags);
    }

}
