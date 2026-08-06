package com.ruoyi.tfa.email.service.impl;

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
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.web.service.SysLoginService;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.framework.web.service.UserDetailsServiceImpl;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.tfa.email.service.IMailService;
import com.ruoyi.tfa.email.utils.EmailUtil;

@Service("auth:service:mail")
public class MailServiceImpl implements IMailService {

    @Autowired
    private ISysUserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @Autowired
    private SysLoginService sysLoginService;

    private static final Logger log = LoggerFactory.getLogger(MailServiceImpl.class);

    @Override
    public boolean sendCode(String email, String code, OauthVerificationUse use) {
        if (CacheUtils.hasKey(CacheConstants.EMAIL_CODES, use.getValue() + email)) {
            throw new ServiceException("当前验证码未失效，请在1分钟后再发送");
        }

        try {
            EmailUtil.sendMessage(email, "验证码邮件", "您收到的验证码是：" + code);
            CacheUtils.put(CacheConstants.EMAIL_CODES, use.getValue() + email, code, 1, TimeUnit.MINUTES);
            log.info("发送邮箱验证码成功:{ email: " + email + " code:" + code + "}");
            return true;
        } catch (Exception e) {
            throw new ServiceException("发送邮箱验证码异常：" + email);
        }
    }

    @Override
    public boolean checkCode(String email, String code, OauthVerificationUse use) {
        if (StringUtils.isEmpty(code)) {
            return false;
        }
        String cachedCode = CacheUtils.get(CacheConstants.EMAIL_CODES, use.getValue() + email, String.class); // 从缓存中获取验证码
        boolean isValid = code.equals(cachedCode);
        if (isValid) {
            CacheUtils.remove(CacheConstants.EMAIL_CODES, use.getValue() + email);
        }
        return isValid;
    }

    @Override
    public void doRegister(RegisterBody registerBody) {
        SysUser sysUser = userService.selectUserByEmail(registerBody.getEmail());
        if (sysUser != null) {
            throw new ServiceException("该邮箱已绑定用户");
        }
        sendCode(registerBody.getEmail(), RandomCodeUtil.numberCode(6), OauthVerificationUse.REGISTER);
    }

    @Override
    public void doRegisterVerify(RegisterBody registerBody) {
        if (!checkCode(registerBody.getEmail(), registerBody.getCode(), OauthVerificationUse.REGISTER)) {
            throw new ServiceException("验证码错误");
        }
        SysUser sysUser = new SysUser();
        sysUser.setUserName(registerBody.getEmail());
        sysUser.setNickName(registerBody.getEmail());
        sysUser.setPassword(SecurityUtils.encryptPassword(registerBody.getPassword()));
        sysUser.setEmail(registerBody.getEmail());
        userService.registerUser(sysUser);
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(sysUser.getUserName(), Constants.REGISTER,
                MessageUtils.message("user.register.success")));
    }

    @Override
    public void doLogin(LoginBody loginBody, boolean autoRegister) {
        SysUser sysUser = userService.selectUserByEmail(loginBody.getEmail());
        if (sysUser == null && !autoRegister) {
            throw new ServiceException("该邮箱未绑定用户");
        }
        sendCode(loginBody.getEmail(), RandomCodeUtil.numberCode(6), OauthVerificationUse.LOGIN);
    }

    @Override
    public String doLoginVerify(LoginBody loginBody, boolean autoRegister) {
        if (checkCode(loginBody.getEmail(), loginBody.getCode(), OauthVerificationUse.LOGIN)) {
            SysUser sysUser = userService.selectUserByEmail(loginBody.getEmail());
            if (sysUser == null) {
                if (!autoRegister) {
                    throw new ServiceException("该邮箱未绑定用户");
                }
                sysUser = new SysUser();
                sysUser.setUserName(loginBody.getEmail());
                sysUser.setNickName(loginBody.getEmail());
                sysUser.setPassword(SecurityUtils.encryptPassword(loginBody.getCode()));
                sysUser.setEmail(loginBody.getEmail());
                userService.registerUser(sysUser);
            }
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(sysUser.getUserName(), Constants.LOGIN_SUCCESS,
                    MessageUtils.message("user.login.success")));
            LoginUser loginUser = (LoginUser) userDetailsServiceImpl.createLoginUser(sysUser);
            sysLoginService.recordLoginInfo(loginUser.getUserId());
            return tokenService.createToken(loginUser);
        } else {
            throw new ServiceException("验证码错误");
        }
    }

    @Override
    public void doBind(LoginBody loginBody) {
        SysUser sysUser = userService.selectUserByEmail(loginBody.getEmail());
        if (sysUser != null) {
            throw new ServiceException("该邮箱已绑定用户");
        }
        sysUser = userService.selectUserById(SecurityUtils.getUserId());
        sendCode(loginBody.getEmail(), RandomCodeUtil.numberCode(6), OauthVerificationUse.BIND);
    }

    @Override
    public void doBindVerify(LoginBody loginBody) {
        if (checkCode(loginBody.getEmail(), loginBody.getCode(), OauthVerificationUse.BIND)) {
            SysUser sysUser = userService.selectUserById(SecurityUtils.getUserId());
            sysUser.setEmail(loginBody.getEmail());
            userService.updateUser(sysUser);
            LoginUser loginUser = SecurityUtils.getLoginUser();
            loginUser.setUser(sysUser);
            tokenService.refreshToken(loginUser);
        } else {
            throw new ServiceException("验证码错误");
        }
    }

    public void doReset(RegisterBody registerBody) {
        SysUser sysUser = userService.selectUserByEmail(registerBody.getEmail());
        if (sysUser == null) {
            throw new ServiceException("该邮箱未绑定用户");
        }
        if (!sysUser.getUserName().equals(SecurityUtils.getUsername())) {
            throw new ServiceException("只能解绑自己的邮箱");
        }
        sendCode(registerBody.getEmail(), RandomCodeUtil.numberCode(6), OauthVerificationUse.RESET);
    }

    public void doResetVerify(RegisterBody registerBody) {
        if (checkCode(registerBody.getEmail(), registerBody.getCode(), OauthVerificationUse.RESET)) {
            SysUser sysUser = userService.selectUserByUserName(SecurityUtils.getUsername());
            sysUser.setEmail("");
            userService.updateUser(sysUser);
            LoginUser loginUser = SecurityUtils.getLoginUser();
            loginUser.setUser(sysUser);
            tokenService.refreshToken(loginUser);
        } else {
            throw new ServiceException("验证码错误");
        }
    }

}
