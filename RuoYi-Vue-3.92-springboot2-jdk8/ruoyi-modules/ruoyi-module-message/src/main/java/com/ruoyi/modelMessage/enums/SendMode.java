package com.ruoyi.modelMessage.enums;

public enum SendMode {
    PLATFORM("0", "平台"),
    PHONE("1", "手机号"),
    EMAIL("2", "邮箱");

    private final String code;
    private final String description;

    SendMode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static SendMode fromCode(String code) {
        for (SendMode mode : SendMode.values()) {
            if (mode.getCode().equals(code)) {
                return mode;
            }
        }
        throw new IllegalArgumentException("未知的发送方式: " + code);
    }
}
