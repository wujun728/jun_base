package com.ruoyi.common.utils.ai.RAGFlow.dto;

import java.util.List;
import java.util.Map;

/**
 * RAGFlow API 统一响应封装
 */
public class RAGFlowResponse<T> {

    private Integer code;
    private T data;
    private String message;
    private Map<String, Object> meta;

    public RAGFlowResponse() {}

    public RAGFlowResponse(Integer code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return code != null && code == 0;
    }

    /**
     * 获取数据，如果失败则返回null
     */
    public T getDataOrNull() {
        return isSuccess() ? data : null;
    }

    /**
     * 获取数据，如果失败则抛出异常
     */
    public T getDataOrThrow() {
        if (!isSuccess()) {
            throw new RAGFlowException(code, message);
        }
        return data;
    }

    // Getters and Setters
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, Object> meta) {
        this.meta = meta;
    }

    /**
     * RAGFlow 异常
     */
    public static class RAGFlowException extends RuntimeException {
        private final Integer code;

        public RAGFlowException(Integer code, String message) {
            super("[" + code + "] " + message);
            this.code = code;
        }

        public Integer getCode() {
            return code;
        }
    }
}
