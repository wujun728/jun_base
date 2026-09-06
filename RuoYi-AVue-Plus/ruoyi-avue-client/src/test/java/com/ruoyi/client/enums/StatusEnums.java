package com.ruoyi.client.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/8/19 - 14:33
 */
public enum StatusEnums implements DicEnumData {
    NO(-1, "无效"),
    YES(1, "有效");
    private int code;
    private String label;

    StatusEnums(int code, String label) {
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

    public static List<StatusEnums> toList() {
        StatusEnums[] values = values();
        //        Map<Integer, String> dataMap =
        //            Arrays.stream(values).collect(Collectors.toMap(TestEnums::getCode, TestEnums::getLabel));
        return Arrays.stream(values).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
