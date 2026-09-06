package com.ruoyi.common.utils.ai.RAGFlow.interceptor;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 拦截器链管理器
 * 管理所有拦截器并按顺序执行
 *
 * @author RuoYi-SpringBoot3-Pro
 */
public class InterceptorChain {

    private final List<Interceptor> interceptors = new ArrayList<>();

    /**
     * 添加拦截器
     */
    public InterceptorChain addInterceptor(Interceptor interceptor) {
        if (interceptor != null) {
            interceptors.add(interceptor);
            // 按优先级排序
            interceptors.sort(Comparator.comparingInt(Interceptor::getOrder));
        }
        return this;
    }

    /**
     * 批量添加拦截器
     */
    public InterceptorChain addInterceptors(List<Interceptor> interceptorList) {
        if (interceptorList != null) {
            for (Interceptor interceptor : interceptorList) {
                if (interceptor != null) {
                    interceptors.add(interceptor);
                }
            }
            // 按优先级排序
            interceptors.sort(Comparator.comparingInt(Interceptor::getOrder));
        }
        return this;
    }

    /**
     * 移除拦截器
     */
    public boolean removeInterceptor(Interceptor interceptor) {
        return interceptors.remove(interceptor);
    }

    /**
     * 清空拦截器
     */
    public void clear() {
        interceptors.clear();
    }

    /**
     * 获取拦截器数量
     */
    public int size() {
        return interceptors.size();
    }

    /**
     * 判断是否为空
     */
    public boolean isEmpty() {
        return interceptors.isEmpty();
    }

    /**
     * 执行请求拦截
     *
     * @param request  HTTP请求
     * @param context  上下文
     * @return true 继续执行，false 跳过请求
     */
    boolean doInterceptRequest(HttpRequest request, InterceptorContext context) {
        for (Interceptor interceptor : interceptors) {
            try {
                boolean result = interceptor.interceptRequest(request, context);
                if (!result) {
                    return false;
                }
            } catch (Exception e) {
                throw new RuntimeException("拦截器执行失败: " + interceptor.getName(), e);
            }
        }
        return true;
    }

    /**
     * 执行响应拦截
     *
     * @param response HTTP响应
     * @param context  上下文
     * @return 处理后的响应
     */
    HttpResponse doInterceptResponse(HttpResponse response, InterceptorContext context) {
        HttpResponse result = response;
        for (Interceptor interceptor : interceptors) {
            try {
                result = interceptor.interceptResponse(result, context);
                if (result == null) {
                    return null;
                }
            } catch (Exception e) {
                throw new RuntimeException("拦截器执行失败: " + interceptor.getName(), e);
            }
        }
        return result;
    }

    /**
     * 执行异常拦截
     *
     * @param exception 异常
     * @param context   上下文
     * @return true 继续抛出，false 吞掉异常
     */
    boolean doInterceptException(Exception exception, InterceptorContext context) {
        for (Interceptor interceptor : interceptors) {
            try {
                boolean result = interceptor.interceptException(exception, context);
                if (!result) {
                    return false;
                }
            } catch (Exception e) {
                throw new RuntimeException("拦截器执行失败: " + interceptor.getName(), e);
            }
        }
        return true;
    }

    /**
     * 获取所有拦截器
     */
    public List<Interceptor> getInterceptors() {
        return new ArrayList<>(interceptors);
    }
}
