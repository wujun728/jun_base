package com.ruoyi.client.enums;

/**
 * 数据属性级别标识
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/8/17 - 11:11
 */
public enum AVueAttrLevel {
    CONFIG("config"),
    PAGE("page"),
    OPTION("option"),
    OPTION_COLUMN("column"),
    OPTION_GROUP("group"),
    CUSTOMIZE("customize"),
    RULES("rules"),
    SEARCH_RULES("searchRules"),
    SEARCH("search");

    private String keyName;

    AVueAttrLevel(String keyName) {
        this.keyName = keyName;
    }

    public String getKeyName() {
        return keyName;
    }
}
