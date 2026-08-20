package com.ruoyi.common.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * /dev-api 前缀剥离过滤器
 *
 * 用于 scrm_layui 静态前端直接部署到后端 static 目录的场景：
 * 前端所有接口统一携带 /dev-api 前缀，此过滤器将前缀剥离后转发到对应接口，
 * 使 <code>/dev-api/login</code> 等价于 <code>/login</code>。
 *
 * 通过配置 <code>ruoyi.dev-api-strip=true</code> 启用（默认关闭，
 * Nginx 部署时由 Nginx rewrite 处理前缀）。
 *
 * @author ruoyi
 */
public class DevApiPrefixFilter implements Filter
{
    private static final String PREFIX = "/dev-api";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException
    {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String uri = httpRequest.getRequestURI();
        if (uri.equals(PREFIX) || uri.startsWith(PREFIX + "/"))
        {
            String newUri = uri.substring(PREFIX.length());
            if (newUri.isEmpty())
            {
                newUri = "/";
            }
            RequestDispatcher dispatcher = httpRequest.getRequestDispatcher(newUri);
            if (dispatcher != null)
            {
                dispatcher.forward(request, response);
                return;
            }
        }
        chain.doFilter(request, response);
    }
}