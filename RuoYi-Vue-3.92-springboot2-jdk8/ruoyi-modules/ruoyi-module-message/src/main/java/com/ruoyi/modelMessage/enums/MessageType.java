package com.ruoyi.modelMessage.enums;

public enum MessageType {
    INFO("信息", "普通信息记录"),
    WARN("警告", "非致命问题警告"),
    ERROR("错误", "严重错误信息"),
    DEBUG("调试", "调试信息，仅用于开发环境"),
    SUCCESS("成功", "操作成功的提示信息");

    private final String code;
    private final String description;

    MessageType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
