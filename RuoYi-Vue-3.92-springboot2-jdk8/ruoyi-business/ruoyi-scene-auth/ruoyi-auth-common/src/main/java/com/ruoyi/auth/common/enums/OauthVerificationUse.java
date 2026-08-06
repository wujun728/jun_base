package com.ruoyi.auth.common.enums;

public enum OauthVerificationUse {

    /** 用于登录 */
    LOGIN("登录", "login"),
    /** 用于注册 */
    REGISTER("注册", "register"),
    /** 用于禁用 */
    DISABLE("禁用", "disable"),
    /** 用于重置信息 */
    RESET("重置", "reset"),
    /** 用于绑定信息 */
    BIND("绑定", "bind"),
    /** 其他用途 */
    OTHER("其他", "other");

    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static OauthVerificationUse getByValue(String value) {
        for (OauthVerificationUse use : OauthVerificationUse.values()) {
            if (use.getValue().equals(value)) {
                return use;
            }
        }
        return null;
    }

    public static OauthVerificationUse getByName(String name) {
        for (OauthVerificationUse use : OauthVerificationUse.values()) {
            if (use.getName().equals(name)) {
                return use;
            }
        }
        return null;
    }

    private OauthVerificationUse(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
