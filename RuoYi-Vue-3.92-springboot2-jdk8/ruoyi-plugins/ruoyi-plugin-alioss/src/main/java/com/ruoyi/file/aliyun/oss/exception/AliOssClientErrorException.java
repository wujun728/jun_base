package com.ruoyi.file.aliyun.oss.exception;

public class AliOssClientErrorException extends RuntimeException{
    public AliOssClientErrorException(String msg){
        super(msg);
    }

    public AliOssClientErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
