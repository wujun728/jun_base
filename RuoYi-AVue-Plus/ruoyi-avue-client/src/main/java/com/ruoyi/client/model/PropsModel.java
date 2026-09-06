package com.ruoyi.client.model;

import java.util.function.Function;

/**
 * 自定义属性名称
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/8/19 - 10:23
 */
public class PropsModel {

    public PropsModel() {
    }

    public PropsModel(String label, String value) {
        this.label = label;
        this.value = value;
    }

    private String label = "label";

    private Object value = "value";

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
