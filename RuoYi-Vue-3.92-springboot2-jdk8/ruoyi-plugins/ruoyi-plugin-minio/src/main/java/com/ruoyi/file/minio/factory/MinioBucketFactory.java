package com.ruoyi.file.minio.factory;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.file.storage.StorageFactory;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.file.minio.domain.MinioBucket;
import com.ruoyi.file.minio.properties.MinioBucketProperties;

import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;

@Configuration("minio")
@ConfigurationProperties("minio")
@ConditionalOnProperty(prefix = "minio", name = { "enable" }, havingValue = "true", matchIfMissing = false)
public class MinioBucketFactory extends StorageFactory<MinioBucketProperties, MinioBucket> {
    private static final Logger logger = LoggerFactory.getLogger(MinioBucketFactory.class);

    @Override
    public MinioBucket createBucket(String name, MinioBucketProperties props) {
        MinioClient.Builder builder = MinioClient.builder();
        builder.endpoint(props.getUrl());
        if (!StringUtils.isEmpty(props.getAccessKey())) {
            builder.credentials(props.getAccessKey(), props.getSecretKey());
        }
        MinioClient client = builder.build();
        MinioBucket minioBucket = MinioBucket.builder()
                .client(client)
                .bucketName(props.getBucketName())
                .permission(props.getPermission())
                .url(props.getUrl())
                .build();
        logger.info("Minio 数据桶：{}  - 创建成功", name);
        return minioBucket;
    }

    public void validateBucket(MinioBucket minioBucket) {
        BucketExistsArgs bucketExistArgs = BucketExistsArgs.builder().bucket(minioBucket.getBucketName()).build();
        try {
            if (!minioBucket.getClient().bucketExists(bucketExistArgs)) {
                throw new RuntimeException("Bucket " + minioBucket.getBucketName() + " does not exist");
            }
            InputStream empty = InputStream.nullInputStream();
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .object(getRelativePath(RuoYiConfig.getProfile()) + "/")
                    .stream(empty, 0, -1).bucket(minioBucket.getBucketName()).build();
            minioBucket.getClient().putObject(putObjectArgs);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
