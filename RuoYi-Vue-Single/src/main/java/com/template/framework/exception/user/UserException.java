package com.template.framework.exception.user;

import com.template.framework.exception.base.BaseException;

/**
 * 用户信息异常类
 *
 * @author template
 */
public class UserException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args)
    {
        super("user", code, args, null);
    }
}
