package com.ruoyi.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import cloud.tianai.captcha.common.constant.CaptchaTypeConstant;
import cloud.tianai.captcha.resource.DefaultBuiltInResources;
import cloud.tianai.captcha.resource.ResourceStore;
import cloud.tianai.captcha.resource.common.model.dto.Resource;
import cloud.tianai.captcha.resource.impl.LocalMemoryResourceStore;

/**
 * 天爱行为验证码资源配置。
 *
 * @author ruoyi
 */
@Configuration
public class TianaiCaptchaConfig
{
    @Bean
    public ResourceStore captchaResourceStore()
    {
        LocalMemoryResourceStore resourceStore = new LocalMemoryResourceStore();
        new DefaultBuiltInResources(DefaultBuiltInResources.PATH_PREFIX).addDefaultTemplate(resourceStore);
        addBackgrounds(resourceStore, CaptchaTypeConstant.SLIDER);
        addBackgrounds(resourceStore, CaptchaTypeConstant.ROTATE);
        addBackgrounds(resourceStore, CaptchaTypeConstant.CONCAT);
        addBackgrounds(resourceStore, CaptchaTypeConstant.WORD_IMAGE_CLICK);
        return resourceStore;
    }

    private void addBackgrounds(LocalMemoryResourceStore resourceStore, String type)
    {
        resourceStore.addResource(type, new Resource("classpath", "captcha/bgimages/slider-1.jpg", "default"));
        resourceStore.addResource(type, new Resource("classpath", "captcha/bgimages/slider-2.jpg", "default"));
        resourceStore.addResource(type, new Resource("classpath", "captcha/bgimages/slider-3.jpg", "default"));
    }
}
