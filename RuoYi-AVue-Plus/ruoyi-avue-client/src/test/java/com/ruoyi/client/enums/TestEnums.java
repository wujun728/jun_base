package com.ruoyi.client.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/8/19 - 14:33
 */
public enum TestEnums implements DicEnumData {

    NO(-1, "失败"),
    WAIT(0, "处理中"),
    OK(1, "成功");

    private int code;
    private String label;

    TestEnums(int code, String label) {
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

    public static List<TestEnums> toList() {
        TestEnums[] values = values();
        //        Map<Integer, String> dataMap =
        //            Arrays.stream(values).collect(Collectors.toMap(TestEnums::getCode, TestEnums::getLabel));
        return Arrays.stream(values).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
