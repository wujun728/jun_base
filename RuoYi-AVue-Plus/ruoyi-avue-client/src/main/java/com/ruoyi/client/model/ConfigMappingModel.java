package com.ruoyi.client.model;

/**
 * 配置请求映射实体
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/8/20 - 9:53
 */
public class ConfigMappingModel {

    private String group;

    /**
     * 授权的token
     */
    private String acceptToken;

    public String getAcceptToken() {
        return acceptToken;
    }

    public void setAcceptToken(String acceptToken) {
        this.acceptToken = acceptToken;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
