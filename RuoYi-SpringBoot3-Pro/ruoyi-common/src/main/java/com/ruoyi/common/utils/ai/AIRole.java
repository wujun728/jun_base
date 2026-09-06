package com.ruoyi.common.utils.ai;

public enum AIRole {
    USER("user"),
    SYSTEM("system"),
    ASSISTANT("assistant");

    private final String role;

    // 构造方法
    private AIRole(String role) {
        this.role = role;
    }

    // 获取role值
    public String getRole() {
        return role;
    }

    // 通过 role 字符串获取枚举实例
    public static AIRole valueOfRole(String role) {
        for (AIRole air : values()) {
            if (air.getRole().equals(role)) {
                return air;
            }
        }
        throw new IllegalArgumentException("No matching AIRole for role: " + role);
    }
}
