package com.geek.common.exception.user;

/**
 * IP 登录重试次数超限异常类
 *
 */
public class IpRetryLimitExceedException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public IpRetryLimitExceedException(int retryLimitCount, int lockTime)
    {
        super("失败次数过多,你的ip暂时被限制登录.");
    }
}
