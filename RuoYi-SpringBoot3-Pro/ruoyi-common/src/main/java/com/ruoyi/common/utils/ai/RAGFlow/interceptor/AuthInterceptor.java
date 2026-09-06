package com.ruoyi.common.utils.ai.RAGFlow.interceptor;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

/**
 * 认证拦截器
 * 自动处理认证失败的情况
 *
 * @author RuoYi-SpringBoot3-Pro
 */
public class AuthInterceptor implements Interceptor {

    /**
     * 认证失败回调
     */
    private AuthenticationFailureHandler authFailureHandler;

    public AuthInterceptor() {}

    public AuthInterceptor(AuthenticationFailureHandler authFailureHandler) {
        this.authFailureHandler = authFailureHandler;
    }

    @Override
    public boolean interceptRequest(HttpRequest request, InterceptorContext context) {
        return true;
    }

    @Override
    public HttpResponse interceptResponse(HttpResponse response, InterceptorContext context) {
        // 检查是否是401未授权
        if (response.getStatus() == 401) {
            System.err.println("[RAGFlow] 认证失败: " + context.getUrl());

            if (authFailureHandler != null) {
                authFailureHandler.onAuthenticationFailure(response, context);
            }
        }
        return response;
    }

    @Override
    public boolean interceptException(Exception exception, InterceptorContext context) {
        return true;
    }

    @Override
    public int getOrder() {
        return -50;
    }

    @Override
    public String getName() {
        return "AuthInterceptor";
    }

    public AuthInterceptor setAuthFailureHandler(AuthenticationFailureHandler handler) {
        this.authFailureHandler = handler;
        return this;
    }

    /**
     * 认证失败处理器接口
     */
    public interface AuthenticationFailureHandler {
        void onAuthenticationFailure(HttpResponse response, InterceptorContext context);
    }
}
