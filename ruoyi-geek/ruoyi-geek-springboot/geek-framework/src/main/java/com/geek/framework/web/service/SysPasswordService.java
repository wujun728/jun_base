package com.geek.framework.web.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.geek.common.constant.CacheConstants;
import com.geek.common.constant.Constants;
import com.geek.common.core.domain.entity.SysUser;
import com.geek.common.exception.user.IpRetryLimitExceedException;
import com.geek.common.exception.user.UserPasswordNotMatchException;
import com.geek.common.exception.user.UserPasswordRetryLimitExceedException;
import com.geek.common.utils.CacheUtils;
import com.geek.common.utils.MessageUtils;
import com.geek.common.utils.SecurityUtils;
import com.geek.common.utils.ip.IpUtils;
import com.geek.framework.manager.AsyncManager;
import com.geek.framework.manager.factory.AsyncFactory;
import com.geek.framework.security.context.AuthenticationContextHolder;

/**
 * 登录密码方法
 *
 * @author geek
 */
@Component
public class SysPasswordService
{

    @Value(value = "${user.password.maxRetryCount}")
    private int maxRetryCount;

    @Value(value = "${user.password.lockTime}")
    private int lockTime;

    @Value(value = "${user.ip.maxRetryCount:15}")
    public int maxIpRetryCount;

    @Value(value = "${user.ip.lockTime:15}")
    public int ipLockTime;
    /**
     * 登录账户密码错误次数缓存键名
     *
     * @return 缓存Cache
     */
    private Cache getCache()
    {
        return CacheUtils.getCache(CacheConstants.PWD_ERR_CNT_KEY);
    }

    private Cache getIpCache() {
        return CacheUtils.getCache(CacheConstants.IP_ERR_CNT_KEY);
    }

    public void validate(SysUser user)
    {
        Authentication usernamePasswordAuthenticationToken = AuthenticationContextHolder.getContext();
        String username = usernamePasswordAuthenticationToken.getName();
        String password = usernamePasswordAuthenticationToken.getCredentials().toString();

        String ip = IpUtils.getIpAddr();
        validateIp(ip);
        Integer retryCount = getCache().get(username, Integer.class);
        if (retryCount == null)
        {
            retryCount = 0;
        }
        if (retryCount >= maxRetryCount)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL,
                    MessageUtils.message("user.password.retry.limit.exceed", maxRetryCount, lockTime)));
            throw new UserPasswordRetryLimitExceedException(maxRetryCount, lockTime);
        }
        if (!matches(user, password))
        {
            retryCount = retryCount + 1;
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL,
                    MessageUtils.message("user.password.retry.limit.count", retryCount)));
            CacheUtils.put(CacheConstants.PWD_ERR_CNT_KEY,username,retryCount,lockTime,TimeUnit.MINUTES);
            throw new UserPasswordNotMatchException();
        }
        else
        {
            clearLoginRecordCache(username);
        }
    }

    public void validateIp(String ip)
    {
        Integer ipRetryCount = getIpCache().get(ip, Integer.class);
        if (ipRetryCount == null)
        {
            ipRetryCount = 0;
        }

        if (ipRetryCount >= maxIpRetryCount)
        {
            throw new IpRetryLimitExceedException(maxIpRetryCount, ipLockTime);
        }
    }

    public void incrementIpFailCount(String ip)
    {
        Integer ipRetryCount = getIpCache().get(ip, Integer.class);
        if (ipRetryCount == null)
        {
            ipRetryCount = 0;
        }
        ipRetryCount += 1;
        CacheUtils.put(CacheConstants.IP_ERR_CNT_KEY,ip,ipRetryCount,ipLockTime,TimeUnit.MINUTES);
    }

    public boolean matches(SysUser user, String rawPassword)
    {
        return SecurityUtils.matchesPassword(rawPassword, user.getPassword());
    }

    public void clearLoginRecordCache(String loginName)
    {
        getCache().evictIfPresent(loginName);
    }
}
