package com.ruoyi.tfa.phone.service.Impl;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.auth.common.enums.OauthVerificationUse;
import com.ruoyi.auth.common.utils.RandomCodeUtil;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginBody;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.domain.model.RegisterBody;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.CacheUtils;
import com.ruoyi.common.utils.JSON;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.web.service.SysLoginService;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.framework.web.service.UserDetailsServiceImpl;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.tfa.phone.config.DySmsConfig;
import com.ruoyi.tfa.phone.service.DySmsService;
import com.ruoyi.tfa.phone.utils.DySmsUtil;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 鎵嬫満鍙疯璇丼ervcie
 * 
 * @author zlh
 * @date 2024-04-16
 */
@Service("auth:service:dySms")
public class DySmsServiceImpl implements DySmsService {

    @Autowired
    private ISysUserService userService;
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private SysLoginService sysLoginService;
    @Autowired
    private DySmsConfig dySmsConfig;

    private static final Logger log = LoggerFactory.getLogger(DySmsServiceImpl.class);

    @Override
    public boolean sendCode(String phone, String code, OauthVerificationUse use) {
        if (CacheUtils.hasKey(CacheConstants.PHONE_CODES, use.getValue() + phone)) {
            throw new ServiceException("验证码未失效，请稍后重试");
        }

        try {
            ObjectNode templateParams = JSON.createObjectNode();
            templateParams.put("code", code);
            DySmsUtil.sendSms(phone, dySmsConfig.getTemplate().get("VerificationCode"), templateParams);
            CacheUtils.put(CacheConstants.PHONE_CODES, use.getValue() + phone, code, 1, TimeUnit.MINUTES);
            log.info("鍙戦€佹墜鏈洪獙璇佺爜鎴愬姛:{ phone: " + phone + " code:" + code + "}");
            return true;
        } catch (Exception e) {
            throw new ServiceException("SMS error: " + phone);
        }
    }

    @Override
    public boolean checkCode(String phone, String code, OauthVerificationUse use) {
        if (StringUtils.isEmpty(code)) {
            return false;
        }
        String cachedCode = CacheUtils.get(CacheConstants.PHONE_CODES, use.getValue() + phone, String.class); // 浠庣紦瀛樹腑鑾峰彇楠岃瘉鐮?
        boolean isValid = code.equals(cachedCode);
        if (isValid) {
            CacheUtils.remove(CacheConstants.PHONE_CODES, use.getValue() + phone);
        }
        return isValid;
    }

    @Override
    public void doRegister(RegisterBody registerBody) {
        SysUser sysUser = userService.selectUserByPhone(registerBody.getPhonenumber());
        if (sysUser != null) {
            throw new ServiceException("phone already bound");
        }
        sendCode(registerBody.getPhonenumber(), RandomCodeUtil.numberCode(6), OauthVerificationUse.REGISTER);
    }

    @Override
    public void doRegisterVerify(RegisterBody registerBody) {
        if (!checkCode(registerBody.getPhonenumber(), registerBody.getCode(), OauthVerificationUse.REGISTER)) {
            throw new ServiceException("wrong code");
        }
        SysUser sysUser = new SysUser();
        sysUser.setUserName(registerBody.getPhonenumber());
        sysUser.setNickName(registerBody.getUsername());
        sysUser.setPassword(SecurityUtils.encryptPassword(registerBody.getPassword()));
        sysUser.setPhonenumber(registerBody.getPhonenumber());
        userService.registerUser(sysUser);
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(sysUser.getUserName(), Constants.REGISTER,
                MessageUtils.message("user.register.success")));
    }

    @Override
    public void doLogin(LoginBody loginBody, boolean autoRegister) {
        SysUser sysUser = userService.selectUserByPhone(loginBody.getPhonenumber());
        if (sysUser == null && !autoRegister) {
            throw new ServiceException("phone not bound");
        }
        sendCode(loginBody.getPhonenumber(), RandomCodeUtil.numberCode(6), OauthVerificationUse.LOGIN);
    }

    @Override
    public String doLoginVerify(LoginBody loginBody, boolean autoRegister) {
        if (checkCode(loginBody.getPhonenumber(), loginBody.getCode(), OauthVerificationUse.LOGIN)) {
            SysUser sysUser = userService.selectUserByPhone(loginBody.getPhonenumber());
            if (sysUser == null) {
                if (!autoRegister) {
                    throw new ServiceException("phone not bound");
                }
                sysUser = new SysUser();
                sysUser.setUserName(loginBody.getPhonenumber());
                sysUser.setNickName(loginBody.getPhonenumber());
                sysUser.setPassword(SecurityUtils.encryptPassword(loginBody.getCode()));
                sysUser.setPhonenumber(loginBody.getPhonenumber());
                userService.registerUser(sysUser);
            }
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(sysUser.getUserName(), Constants.LOGIN_SUCCESS,
                    MessageUtils.message("user.login.success")));
            LoginUser loginUser = (LoginUser) userDetailsServiceImpl.createLoginUser(sysUser);
            sysLoginService.recordLoginInfo(loginUser.getUserId());
            return tokenService.createToken(loginUser);
        } else {
            throw new ServiceException("wrong code");
        }
    }

    @Override
    public void doBind(LoginBody loginBody) {
        SysUser sysUser = userService.selectUserByPhone(loginBody.getPhonenumber());
        if (sysUser != null) {
            throw new ServiceException("phone already bound");
        }
        sysUser = userService.selectUserById(SecurityUtils.getUserId());
        sendCode(loginBody.getPhonenumber(), RandomCodeUtil.numberCode(6), OauthVerificationUse.BIND);
    }

    @Override
    public void doBindVerify(LoginBody loginBody) {
        if (checkCode(loginBody.getPhonenumber(), loginBody.getCode(), OauthVerificationUse.BIND)) {
            SysUser sysUser = userService.selectUserById(SecurityUtils.getUserId());
            sysUser.setPhonenumber(loginBody.getPhonenumber());
            userService.updateUser(sysUser);
            LoginUser loginUser = SecurityUtils.getLoginUser();
            loginUser.setUser(sysUser);
            tokenService.refreshToken(loginUser);
        } else {
            throw new ServiceException("wrong code");
        }
    }

    public void doReset(RegisterBody registerBody) {
        SysUser sysUser = userService.selectUserByPhone(registerBody.getPhonenumber());
        if (sysUser == null) {
            throw new ServiceException("phone not bound");
        }
        if (!sysUser.getUserName().equals(SecurityUtils.getUsername())) {
            throw new ServiceException("鍙兘瑙ｇ粦鑷繁鐨勬墜鏈哄彿");
        }
        sendCode(registerBody.getPhonenumber(), RandomCodeUtil.numberCode(6), OauthVerificationUse.RESET);
    }

    public void doResetVerify(RegisterBody registerBody) {
        if (checkCode(registerBody.getPhonenumber(), registerBody.getCode(), OauthVerificationUse.RESET)) {
            SysUser sysUser = userService.selectUserByUserName(SecurityUtils.getUsername());
            sysUser.setPhonenumber("");
            userService.updateUser(sysUser);
            LoginUser loginUser = SecurityUtils.getLoginUser();
            loginUser.setUser(sysUser);
            tokenService.refreshToken(loginUser);
        } else {
            throw new ServiceException("wrong code");
        }
    }

}
