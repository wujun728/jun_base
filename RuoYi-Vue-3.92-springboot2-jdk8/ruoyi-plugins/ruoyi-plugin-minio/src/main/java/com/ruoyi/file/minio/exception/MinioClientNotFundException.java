package com.ruoyi.file.minio.exception;

/**
 * 当尝试获取MinIO客户端实例但未能找到相应的配置或客户端实例时抛出此异常。
 */
public class MinioClientNotFundException extends RuntimeException {
    
    public MinioClientNotFundException(String msg) {
        super(msg);
    }

    public MinioClientNotFundException(String message, Throwable cause) {
        super(message, cause);
    }
}
