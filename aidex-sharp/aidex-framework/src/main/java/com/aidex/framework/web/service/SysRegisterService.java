package com.aidex.framework.web.service;

import com.aidex.common.constant.CacheConstants;
import com.aidex.common.constant.Constants;
import com.aidex.common.constant.UserConstants;
import com.aidex.common.core.domain.entity.RegisterBody;
import com.aidex.common.core.domain.entity.SysUser;
import com.aidex.common.core.redis.RedisCache;
import com.aidex.common.exception.user.CaptchaException;
import com.aidex.common.exception.user.CaptchaExpireException;
import com.aidex.common.utils.MessageUtils;
import com.aidex.common.utils.SecurityUtils;
import com.aidex.common.utils.StringUtils;
import com.aidex.framework.cache.ConfigUtils;
import com.aidex.framework.manager.AsyncManager;
import com.aidex.framework.manager.factory.AsyncFactory;
import com.aidex.system.service.ISysUserService;
import com.aidex.system.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 注册校验方法
 *
 * @author ruoyi
 */
@Component
public class SysRegisterService {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysConfigService configService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 注册用户
     *
     * @param registerBody
     * @return
     */
    public String register(RegisterBody registerBody) {
        String msg = "", username = registerBody.getUsername(), password = registerBody.getPassword();
        // 验证码开关
        if (ConfigUtils.getConfigBooleanValueByKey("sys.account.captchaOnOff", Boolean.TRUE)) {
            validateCaptcha(username, registerBody.getCode(), registerBody.getUuid());
        }
        if (StringUtils.isEmpty(username)) {
            msg = "用户名不能为空";
        } else if (StringUtils.isEmpty(password)) {
            msg = "用户密码不能为空";
        } else if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            msg = "账户长度必须在2到20个字符之间";
        } else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            msg = "密码长度必须在5到20个字符之间";
        } else {
            SysUser userQuery = new SysUser();
            userQuery.setUserName(username);
            try {
                userService.checkUserNameUnique(userQuery);
            } catch (Exception e) {
                msg = "保存用户'" + username + "'失败，注册账号已存在";
                e.printStackTrace();
            }
            if (StringUtils.isEmpty(msg)) {
                SysUser sysUser = new SysUser();
                sysUser.setUserName(username);
                sysUser.setNickName(username);
                sysUser.setPassword(SecurityUtils.encryptPassword(registerBody.getPassword()));
                boolean regFlag = userService.registerUser(sysUser);
                if (!regFlag) {
                    msg = "注册失败,请联系系统管理人员";
                } else {
                    AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.REGISTER,
                            MessageUtils.message("user.register.success")));
                }
            }
        }
        return msg;
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid) {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null) {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            throw new CaptchaException();
        }
    }
}
