package com.ruoyi.client.model;

/**
 * 规则校验实体
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/8/23 - 15:18
 */
public class RuleValidatorModel {

    private String type;

    private boolean required;

    private String pattern;

    private Integer len;

    private Integer min;

    private Integer max;

    private String message;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Integer getLen() {
        return len;
    }

    public void setLen(Integer len) {
        this.len = len;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
