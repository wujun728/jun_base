package com.ruoyi.file.aliyun.oss.domain;

import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.ruoyi.common.core.file.storage.StorageEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AliOssEntityVO extends StorageEntity {
    private String key;
    private String bucketName;
    private ObjectMetadata metadata;

    public AliOssEntityVO(OSSObject object, String filePath) {
        this.setInputStream(object.getObjectContent());
        this.setKey(object.getKey());
        this.setBucketName(object.getBucketName());
        this.setByteCount(object.getObjectMetadata().getContentLength());
        this.setFilePath(filePath);
    }
}