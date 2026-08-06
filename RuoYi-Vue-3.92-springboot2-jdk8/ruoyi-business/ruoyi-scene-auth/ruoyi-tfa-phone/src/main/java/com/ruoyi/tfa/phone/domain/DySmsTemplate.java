package com.ruoyi.tfa.phone.domain;

import lombok.Data;

/**
 * 手机号认证短信模板
 * 
 * @author Dftre
 * @date 2024-04-16
 */
@Data
public class DySmsTemplate {
    /**
     * 短信模板编码
     */
    private String templateCode;
    /**
     * 签名
     */
    private String signName;
    /**
     * 短信模板必需的数据名称，多个key以逗号分隔，此处配置作为校验
     */
    private String keys;

}
