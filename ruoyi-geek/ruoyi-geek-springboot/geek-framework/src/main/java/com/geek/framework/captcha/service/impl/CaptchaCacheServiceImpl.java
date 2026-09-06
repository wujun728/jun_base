package com.geek.framework.captcha.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.aop.framework.AopInfrastructureBean;

import com.anji.captcha.service.CaptchaCacheService;
import com.geek.common.constant.CacheConstants;
import com.geek.common.utils.CacheUtils;

public class CaptchaCacheServiceImpl implements CaptchaCacheService, AopInfrastructureBean {

    @Override
    public void delete(String arg0) {
        CacheUtils.remove(CacheConstants.CAPTCHA_CODE_KEY, arg0);
    }

    @Override
    public boolean exists(String arg0) {
        return CacheUtils.get(CacheConstants.CAPTCHA_CODE_KEY, arg0) != null;
    }

    @Override
    public String get(String arg0) {
        return CacheUtils.get(CacheConstants.CAPTCHA_CODE_KEY, arg0).get().toString();
    }

    @Override
    public void set(String arg0, String arg1, long arg2) {
        CacheUtils.put(CacheConstants.CAPTCHA_CODE_KEY, arg0, arg1, arg2, TimeUnit.SECONDS);
    }

    @Override
    public String type() {
        return "other";
    }

}
