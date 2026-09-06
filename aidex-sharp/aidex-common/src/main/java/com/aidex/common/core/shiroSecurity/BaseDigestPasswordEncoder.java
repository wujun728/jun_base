package com.aidex.common.core.shiroSecurity;


public abstract class BaseDigestPasswordEncoder extends BasePasswordEncoder
{

    public BaseDigestPasswordEncoder()
    {
        encodeHashAsBase64 = false;
    }

    public boolean getEncodeHashAsBase64()
    {
        return encodeHashAsBase64;
    }

    public void setEncodeHashAsBase64(boolean encodeHashAsBase64)
    {
        this.encodeHashAsBase64 = encodeHashAsBase64;
    }

    private boolean encodeHashAsBase64;
}
