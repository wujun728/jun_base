package com.aidex.framework.web.service;

import javax.annotation.Resource;

import com.aidex.common.constant.CacheConstants;
import com.aidex.common.core.domain.entity.SysUser;
import com.aidex.common.core.domain.entity.SysUserMenu;
import com.aidex.common.exception.ExpireException;
import com.aidex.common.exception.user.UserPasswordRetryLimitExceedException;
import com.aidex.common.utils.DateUtils;
import com.aidex.common.utils.ServletUtils;
import com.aidex.common.utils.StringUtils;
import com.aidex.common.utils.ip.IpUtils;
import com.aidex.framework.cache.ConfigUtils;
import com.aidex.framework.security.context.AuthenticationContextHolder;
import com.aidex.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Value;
import com.aidex.framework.manager.AsyncManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import com.aidex.common.constant.Constants;
import com.aidex.common.core.domain.model.LoginUser;
import com.aidex.common.core.redis.RedisCache;
import com.aidex.common.exception.user.CaptchaException;
import com.aidex.common.exception.user.CaptchaExpireException;
import com.aidex.common.exception.user.UserPasswordNotMatchException;
import com.aidex.common.utils.MessageUtils;
import com.aidex.framework.manager.factory.AsyncFactory;

/**
 * 登录校验方法
 * 
 * @author ruoyi
 */
@Component
public class SysLoginService
{
    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysUserMenu sysDeptVo;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysUserService userService;

    // 是否允许账户多终端同时登录（true允许 false不允许）
    @Value("${token.soloLogin}")
    private boolean soloLogin;
    /**
     * 登录验证
     * 
     * @param username 用户名
     * @param password 密码
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid)
    {
        // 验证码开关
        if (ConfigUtils.getConfigBooleanValueByKey("sys.captcha.onOff",true))
        {
            validateCaptcha(username, code, uuid);
        }
        // 用户验证
        Authentication authentication = null;
        try
        {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            AuthenticationContextHolder.setContext(authenticationToken);
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
        }
        catch (Exception e)
        {
            if (e instanceof BadCredentialsException)
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            }
            else
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ExpireException(e.getMessage());
            }
        }
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        if (!soloLogin)
        {
            // 如果用户不允许多终端同时登录，清除缓存信息
            String userIdKey = Constants.LOGIN_USERID_KEY + loginUser.getUser().getId();
            String userKey = redisCache.getCacheObject(userIdKey);
            if (StringUtils.isNotEmpty(userKey))
            {
                redisCache.deleteObject(userIdKey);
                redisCache.deleteObject(userKey);
            }
        }
        // 生成token
        String token = tokenService.createToken(loginUser);
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        recordLoginInfo(loginUser.getUser().getId());
        return token;
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
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaException();
        }
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(String userId)
    {
        SysUser sysUser = new SysUser();
        sysUser.setId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        sysUser.setLoginDate(DateUtils.getNowDate());
        userService.updateUserLoginInfo(sysUser);
    }

}
