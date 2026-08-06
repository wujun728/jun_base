package com.ruoyi.oauth.wx.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.auth.common.domain.OauthUser;
import com.ruoyi.auth.common.service.IOauthUserService;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.web.service.SysLoginService;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.framework.web.service.UserDetailsServiceImpl;
import com.ruoyi.oauth.wx.constant.WxPubConstant;
import com.ruoyi.oauth.wx.service.WxLoginService;
import com.ruoyi.system.service.ISysUserService;

import com.fasterxml.jackson.databind.JsonNode;

@Service
public class WxPubLoginServiceImpl implements WxLoginService {

    @Autowired
    private WxPubConstant wxH5Constant;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private IOauthUserService oauthUserService;

    @Autowired
    private SysLoginService sysLoginService;

    @Override
    public String doLogin(String code, boolean autoRegister) {
        JsonNode doAuth = doAuth(
                wxH5Constant.getUrl(),
                wxH5Constant.getAppId(),
                wxH5Constant.getAppSecret(),
                code);
        String openid = doAuth.get("openid").asText();
        OauthUser selectOauthUser = oauthUserService.selectOauthUserByUUID(openid);
        SysUser sysUser = null;
        if (selectOauthUser == null) {
            if (autoRegister) {
                sysUser = new SysUser();
                sysUser.setUserName(openid);
                sysUser.setNickName(openid);
                sysUser.setPassword(SecurityUtils.encryptPassword(code));
                userService.registerUser(sysUser);
                OauthUser oauthUser = new OauthUser();
                oauthUser.setUserId(sysUser.getUserId());
                oauthUser.setOpenId(doAuth.get("openid").asText());
                oauthUser.setUuid(doAuth.get("openid").asText());
                oauthUser.setSource("WXMiniApp");
                oauthUser.setAccessToken(doAuth.get("session_key").asText());
                oauthUserService.insertOauthUser(oauthUser);
            }
        } else {
            sysUser = userService.selectUserById(selectOauthUser.getUserId());
        }
        if (sysUser == null) {
            throw new ServiceException("该微信未绑定用户");
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(sysUser.getUserName(), Constants.LOGIN_SUCCESS,
                MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) userDetailsServiceImpl.createLoginUser(sysUser);
        sysLoginService.recordLoginInfo(loginUser.getUserId());
        return tokenService.createToken(loginUser);
    }

    @Override
    public String doRegister(OauthUser oauthUser) {
        if (StringUtils.isEmpty(oauthUser.getCode())) {
            return "没有凭证";
        }
        if (oauthUser.getUserId() == null) {
            return "请先注册账号";
        }
        JsonNode doAuth = doAuth(
                wxH5Constant.getUrl(),
                wxH5Constant.getAppId(),
                wxH5Constant.getAppSecret(),
                oauthUser.getCode());
        oauthUser.setOpenId(doAuth.get("openid").asText());
        oauthUser.setUuid(doAuth.get("openid").asText());
        oauthUser.setSource("WXPub");
        oauthUser.setAccessToken(doAuth.get("session_key").asText());
        oauthUserService.insertOauthUser(oauthUser);
        return "";
    }

}
