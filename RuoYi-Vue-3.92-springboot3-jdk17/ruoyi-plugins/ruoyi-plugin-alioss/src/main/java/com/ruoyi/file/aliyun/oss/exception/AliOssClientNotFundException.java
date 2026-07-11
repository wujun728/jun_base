package com.ruoyi.file.aliyun.oss.exception;

/**
 * 当尝试获取阿里云OSS客户端实例但未能找到相应的配置或客户端实例时抛出此异常。
 * 此异常表明系统中存在配置问题或者客户端初始化失败的问题。
 */
public class AliOssClientNotFundException extends RuntimeException {

    /**
     * 使用指定的详细信息创建一个新的 {@code AliOssClientNotFundException} 实例。
     *
     * @param msg 描述异常原因的信息。
     */
    public AliOssClientNotFundException(String msg) {
        super(msg);
    }

    /**
     * 使用指定的详细信息和导致此异常的原因创建一个新的 {@code AliOssClientNotFundException} 实例。
     *
     * @param message 描述异常原因的信息。
     * @param cause   导致此异常的根本原因。
     */
    public AliOssClientNotFundException(String message, Throwable cause) {
        super(message, cause);
    }
}
