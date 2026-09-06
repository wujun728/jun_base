package com.ruoyi.common.utils.ai.RAGFlow.interceptor;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.ruoyi.common.utils.ai.RAGFlow.RAGFlow;

/**
 * 请求/响应拦截器接口
 * 用于在请求发送前和响应接收后进行自定义处理
 *
 * @author RuoYi-SpringBoot3-Pro
 */
public interface Interceptor {

    /**
     * 请求拦截
     * 在请求发送前调用，可用于修改请求头、参数等
     *
     * @param request  HTTP请求
     * @param context  拦截器上下文
     * @return true 继续执行请求，false 跳过请求
     */
    boolean interceptRequest(HttpRequest request, InterceptorContext context);

    /**
     * 响应拦截
     * 在响应接收后调用，可用于日志记录、错误处理等
     *
     * @param response HTTP响应
     * @param context  拦截器上下文
     * @return 处理后的响应，如果返回null则中断后续处理
     */
    HttpResponse interceptResponse(HttpResponse response, InterceptorContext context);

    /**
     * 异常拦截
     * 当请求发生异常时调用
     *
     * @param exception 异常
     * @param context   拦截器上下文
     * @return true 继续抛出异常，false 吞掉异常
     */
    boolean interceptException(Exception exception, InterceptorContext context);

    /**
     * 获取拦截器优先级（数值越小优先级越高）
     */
    default int getOrder() {
        return 0;
    }

    /**
     * 获取拦截器名称
     */
    default String getName() {
        return getClass().getSimpleName();
    }
}
