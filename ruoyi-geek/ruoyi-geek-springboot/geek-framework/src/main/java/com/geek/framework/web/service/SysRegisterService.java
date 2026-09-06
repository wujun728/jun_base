package com.geek.framework.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.service.CaptchaService;
import com.geek.common.constant.CacheConstants;
import com.geek.common.constant.Constants;
import com.geek.common.constant.UserConstants;
import com.geek.common.core.domain.entity.SysUser;
import com.geek.common.core.domain.model.RegisterBody;
import com.geek.common.exception.user.CaptchaException;
import com.geek.common.exception.user.CaptchaExpireException;
import com.geek.common.utils.CacheUtils;
import com.geek.common.utils.MessageUtils;
import com.geek.common.utils.SecurityUtils;
import com.geek.common.utils.StringUtils;
import com.geek.framework.manager.AsyncManager;
import com.geek.framework.manager.factory.AsyncFactory;
import com.geek.system.service.ISysConfigService;
import com.geek.system.service.ISysUserService;

/**
 * 注册校验方法
 * 
 * @author geek
 */
@Component
public class SysRegisterService
{
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private CaptchaService captchaService;

    /**
     * 注册
     */
    public String register(RegisterBody registerBody)
    {
        String msg = "", username = registerBody.getUsername(), password = registerBody.getPassword();
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);
        // 验证码开关
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled)
        {
            ResponseModel rm = captchaService.check(registerBody.getCaptcha());
            if(!rm.isSuccess()){
                throw new CaptchaException();
            }
        }
        if (StringUtils.isEmpty(username))
        {
            msg = "用户名不能为空";
        }
        else if (StringUtils.isEmpty(password))
        {
            msg = "用户密码不能为空";
        }
        else if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            msg = "账户长度必须在2到20个字符之间";
        }
        else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            msg = "密码长度必须在5到20个字符之间";
        }
        else if (!userService.checkUserNameUnique(sysUser))
        {
            msg = "保存用户'" + username + "'失败，注册账号已存在";
        }
        else
        {
            sysUser.setNickName(username);
            sysUser.setPassword(SecurityUtils.encryptPassword(password));
            boolean regFlag = userService.registerUser(sysUser);
            if (!regFlag)
            {
                msg = "注册失败,请联系系统管理人员";
            }
            else
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.REGISTER, MessageUtils.message("user.register.success")));
            }
        }
        return msg;
    }

    /**
     * 校验验证码
     * 
     * @param username 用户名
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid)
    {
        String captcha = CacheUtils.get(CacheConstants.CAPTCHA_CODE_KEY, StringUtils.nvl(uuid, ""), String.class);
        CacheUtils.removeIfPresent(CacheConstants.CAPTCHA_CODE_KEY, StringUtils.nvl(uuid, ""));
        if (captcha == null)
        {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha))
        {
            throw new CaptchaException();
        }
    }
}
