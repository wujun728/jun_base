package com.ruoyi.common.utils.ai.RAGFlow.exception;

import com.ruoyi.common.utils.ai.RAGFlow.constant.RAGFlowConstants;

/**
 * RAGFlow API 异常
 */
public class RAGFlowException extends RuntimeException {

    private final int code;
    private final String message;

    public RAGFlowException(int code, String message) {
        super("[" + code + "] " + message);
        this.code = code;
        this.message = message;
    }

    public RAGFlowException(String message) {
        super(message);
        this.code = RAGFlowConstants.CODE_INTERNAL_ERROR;
        this.message = message;
    }

    /**
     * 获取错误码
     */
    public int getCode() {
        return code;
    }

    /**
     * 获取错误信息
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * 判断是否为认证错误
     */
    public boolean isUnauthorized() {
        return code == RAGFlowConstants.CODE_UNAUTHORIZED;
    }

    /**
     * 判断是否为权限错误
     */
    public boolean isForbidden() {
        return code == RAGFlowConstants.CODE_FORBIDDEN;
    }

    /**
     * 判断是否为资源不存在错误
     */
    public boolean isNotFound() {
        return code == RAGFlowConstants.CODE_NOT_FOUND;
    }

    /**
     * 判断是否为请求参数错误
     */
    public boolean isBadRequest() {
        return code == RAGFlowConstants.CODE_BAD_REQUEST;
    }

    /**
     * 判断是否为服务器内部错误
     */
    public boolean isServerError() {
        return code == RAGFlowConstants.CODE_INTERNAL_ERROR;
    }

    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return code == RAGFlowConstants.CODE_SUCCESS;
    }

    /**
     * 认证异常
     */
    public static class RAGFlowUnauthorizedException extends RAGFlowException {
        public RAGFlowUnauthorizedException(String message) {
            super(RAGFlowConstants.CODE_UNAUTHORIZED, message);
        }
    }

    /**
     * 权限不足异常
     */
    public static class RAGFlowForbiddenException extends RAGFlowException {
        public RAGFlowForbiddenException(String message) {
            super(RAGFlowConstants.CODE_FORBIDDEN, message);
        }
    }

    /**
     * 资源不存在异常
     */
    public static class RAGFlowNotFoundException extends RAGFlowException {
        public RAGFlowNotFoundException(String message) {
            super(RAGFlowConstants.CODE_NOT_FOUND, message);
        }
    }

    /**
     * 请求参数错误异常
     */
    public static class RAGFlowBadRequestException extends RAGFlowException {
        public RAGFlowBadRequestException(String message) {
            super(RAGFlowConstants.CODE_BAD_REQUEST, message);
        }
    }

    /**
     * 服务器内部错误异常
     */
    public static class RAGFlowServerException extends RAGFlowException {
        public RAGFlowServerException(String message) {
            super(RAGFlowConstants.CODE_INTERNAL_ERROR, message);
        }
    }
}
