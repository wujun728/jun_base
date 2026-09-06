package com.aidex.common.core.shiroSecurity;

public class ShaPasswordEncoder extends MessageDigestPasswordEncoder
{

    public ShaPasswordEncoder()
    {
        this(1);
    }

    public ShaPasswordEncoder(int strength)
    {
        super((new StringBuilder()).append("SHA-").append(strength).toString());
    }
}
