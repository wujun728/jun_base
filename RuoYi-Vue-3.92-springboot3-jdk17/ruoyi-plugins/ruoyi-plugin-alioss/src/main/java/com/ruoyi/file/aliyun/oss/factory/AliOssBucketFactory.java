package com.ruoyi.file.aliyun.oss.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.ruoyi.common.core.file.storage.StorageFactory;
import com.ruoyi.file.aliyun.oss.domain.AliOssBucket;
import com.ruoyi.file.aliyun.oss.properties.AliOssBucketProperties;

/**
 * 配置类用于管理阿里云OSS客户端实例及其相关属性。
 */
@Configuration("oss")
@ConfigurationProperties(prefix = "oss")
@ConditionalOnProperty(prefix = "oss", name = "enable", havingValue = "true", matchIfMissing = false)
public class AliOssBucketFactory extends StorageFactory<AliOssBucketProperties, AliOssBucket> {
    private static final Logger logger = LoggerFactory.getLogger(AliOssBucketFactory.class);

    @Override
    public AliOssBucket createBucket(String name, AliOssBucketProperties props) {
        if (props == null || props.getEndpoint() == null || props.getAccessKeyId() == null ||
                props.getAccessKeySecret() == null || props.getBucketName() == null) {
            throw new IllegalArgumentException("AliOssProperties or its required fields cannot be null");
        }

        OSS client = new OSSClientBuilder().build(props.getEndpoint(), props.getAccessKeyId(),
                props.getAccessKeySecret());
        AliOssBucket ossBucket = AliOssBucket.builder()
                .client(client)
                .bucketName(props.getBucketName())
                .endpoint(props.getEndpoint())
                .build();
        logger.info("AliOSS 数据桶：{} - 创建成功", name);
        return ossBucket;
    }

    public void validateBucket(AliOssBucket aliOssBucket) {
        OSS ossClient = aliOssBucket.getClient();
        String bucketName = aliOssBucket.getBucketName();
        try {
            if (!ossClient.doesBucketExist(bucketName)) {
                throw new RuntimeException("Bucket " + bucketName + " does not exist");
            }
        } catch (OSSException oe) {
            logger.error("OSSException: " + oe.getMessage(), oe);
            throw new RuntimeException("OSS error: " + oe.getMessage());
        } catch (ClientException ce) {
            logger.error("ClientException: " + ce.getMessage(), ce);
            throw new RuntimeException("Client error: " + ce.getMessage());
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage(), e);
            throw new RuntimeException("Error validating OSS bucket: " + e.getMessage());
        }
    }
}
