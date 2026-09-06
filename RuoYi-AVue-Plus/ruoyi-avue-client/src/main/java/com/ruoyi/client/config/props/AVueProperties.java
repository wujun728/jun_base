package com.ruoyi.client.config.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * AVue客户端的可配置项
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/8/18 - 16:55
 */
@ConfigurationProperties(prefix = "spring.avue")
public class AVueProperties {
    /**
     * 是否显示默认值
     */
    private boolean showDefaultValue = false;

    /**
     * 是否开启开发模式， 该模式不会对数据进行缓存
     */
    private boolean debug = false;

    /**
     * 授权访问的token
     */
    private String acceptToken;

    public String getAcceptToken() {
        return acceptToken;
    }

    public void setAcceptToken(String acceptToken) {
        this.acceptToken = acceptToken;
    }

    public boolean isShowDefaultValue() {
        return showDefaultValue;
    }

    public void setShowDefaultValue(boolean showDefaultValue) {
        this.showDefaultValue = showDefaultValue;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
