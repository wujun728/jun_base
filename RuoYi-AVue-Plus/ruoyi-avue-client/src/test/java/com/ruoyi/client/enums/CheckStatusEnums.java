package com.ruoyi.client.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/8/19 - 14:33
 */
public enum CheckStatusEnums implements DicEnumData {
    BASKETBALL("1", "已认证"),
    FOOTBALL("0", "未认证"),
    BADMINTON("-99", "已失效"),
    BILLIARDS("-1", "已拒绝");

    private String code;
    private String label;

    CheckStatusEnums(String code, String label) {
        this.code = code;
        this.label = label;
    }

    @Override
    public Object getCode() {
        return code;
    }

    @Override
    public String getLabel() {
        return label;
    }

}
