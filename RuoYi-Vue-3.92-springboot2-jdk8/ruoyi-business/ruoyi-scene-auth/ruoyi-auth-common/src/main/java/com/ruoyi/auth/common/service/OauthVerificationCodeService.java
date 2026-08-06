package com.ruoyi.auth.common.service;

import com.ruoyi.auth.common.enums.OauthVerificationUse;

/**
 * code认证方式接口
 * 
 * @author zlh
 * @date 2024-04-16
 */
public interface OauthVerificationCodeService {
    public boolean sendCode(String o, String code,OauthVerificationUse use) throws Exception;
    public boolean checkCode(String o, String code,OauthVerificationUse use) throws Exception;

}
