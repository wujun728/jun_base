package com.ruoyi.client.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/8/19 - 14:33
 */
public enum InterestEnums implements DicEnumData {
    BASKETBALL("basketball", "篮球"),
    FOOTBALL("football", "足球"),
    BADMINTON("badminton", "羽毛球"),
    PINGPONG("pingpong", "乒乓球"),
    BILLIARDS("billiards", "桌球");

    private String code;
    private String label;

    InterestEnums(String code, String label) {
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

    public static List<InterestEnums> toList() {
        InterestEnums[] values = values();
        //        Map<Integer, String> dataMap =
        //            Arrays.stream(values).collect(Collectors.toMap(TestEnums::getCode, TestEnums::getLabel));
        return Arrays.stream(values).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
