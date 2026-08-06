package com.ruoyi.file.minio.exception;

/**
 * 当与MinIO客户端交互过程中发生错误时抛出此异常。
 */
public class MinioClientErrorException extends RuntimeException {
    
    public MinioClientErrorException(String msg) {
        super(msg);
    }

    public MinioClientErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}

