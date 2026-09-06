package com.ruoyi.system.domain.vo;

/**
 * 路由按钮权限信息
 */
public class AuthVo
{
    private String title;

    private String authMark;

    public AuthVo(String title, String authMark)
    {
        this.title = title;
        this.authMark = authMark;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getAuthMark()
    {
        return authMark;
    }

    public void setAuthMark(String authMark)
    {
        this.authMark = authMark;
    }
}
