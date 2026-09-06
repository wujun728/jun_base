package com.geek.framework.processor.aspectj;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;

import com.geek.common.annotation.UseStorageBucket;
import com.geek.common.core.storage.StorageBucketKey;
import com.geek.common.utils.StringUtils;

@Aspect
@Component
public class UseStorageBucketAspect {

    @Around("@annotation(com.geek.common.annotation.UseStorageBucket) || @within(com.geek.common.annotation.UseStorageBucket)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        UseStorageBucket annotation = resolveAnnotation(joinPoint);
        if (annotation == null || StringUtils.isBlank(annotation.value())) {
            return joinPoint.proceed();
        }
        try {
            StorageBucketKey.use(annotation.value());
            return joinPoint.proceed();
        } finally {
            StorageBucketKey.clear();
        }
    }

    private UseStorageBucket resolveAnnotation(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Class<?> targetClass = joinPoint.getTarget() != null ? joinPoint.getTarget().getClass()
                : signature.getDeclaringType();
        Method method = signature.getMethod();
        Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);
        UseStorageBucket annotation = AnnotatedElementUtils.findMergedAnnotation(specificMethod,
                UseStorageBucket.class);
        if (annotation != null) {
            return annotation;
        }
        return AnnotatedElementUtils.findMergedAnnotation(targetClass, UseStorageBucket.class);
    }
}
