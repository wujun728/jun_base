package com.aidex.common.core.shiroSecurity;

import org.springframework.dao.DataAccessException;

public interface PasswordEncoder
{

      String encodePassword(String s, Object obj)
        throws DataAccessException;

      boolean isPasswordValid(String s, String s1, Object obj)
        throws DataAccessException;
}
