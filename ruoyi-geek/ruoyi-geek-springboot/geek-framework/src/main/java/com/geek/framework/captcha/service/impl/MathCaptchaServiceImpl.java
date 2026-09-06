package com.geek.framework.captcha.service.impl;

import static com.google.code.kaptcha.Constants.*;

import java.util.AbstractMap.SimpleEntry;
import java.util.Properties;

import com.geek.framework.captcha.config.KaptchaTextCreator;
import com.geek.framework.captcha.service.AbstractKaptchaService;
import com.google.code.kaptcha.impl.NoNoise;
import com.google.code.kaptcha.impl.ShadowGimpy;

public class MathCaptchaServiceImpl extends AbstractKaptchaService {

    @Override
    public void init(Properties config) {
        // 是否有边框 默认为true 我们可以自己设置yes，no
        config.setProperty(KAPTCHA_BORDER, "yes");
        // 边框颜色 默认为Color.BLACK
        config.setProperty(KAPTCHA_BORDER_COLOR, "105,179,90");
        // 验证码文本字符颜色 默认为Color.BLACK
        config.setProperty(KAPTCHA_TEXTPRODUCER_FONT_COLOR, "blue");
        // 验证码图片宽度 默认为200
        config.setProperty(KAPTCHA_IMAGE_WIDTH, "160");
        // 验证码图片高度 默认为50
        config.setProperty(KAPTCHA_IMAGE_HEIGHT, "60");
        // 验证码文本字符大小 默认为40
        config.setProperty(KAPTCHA_TEXTPRODUCER_FONT_SIZE, "35");
        // KAPTCHA_SESSION_KEY
        config.setProperty(KAPTCHA_SESSION_CONFIG_KEY, "kaptchaCodeMath");
        // 验证码文本生成器
        config.setProperty(KAPTCHA_TEXTPRODUCER_IMPL, KaptchaTextCreator.class.getName());
        // 验证码文本字符间距 默认为2
        config.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_SPACE, "3");
        // 验证码文本字符长度 默认为5
        config.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "6");
        // 验证码文本字体样式 默认为new Font("Arial", 1, fontSize), new Font("Courier", 1, fontSize)
        config.setProperty(KAPTCHA_TEXTPRODUCER_FONT_NAMES, "Arial,Courier");
        // 验证码噪点颜色 默认为Color.BLACK
        config.setProperty(KAPTCHA_NOISE_COLOR, "white");
        // 干扰实现类
        config.setProperty(KAPTCHA_NOISE_IMPL, NoNoise.class.getName());
        // 图片样式 水纹com.google.code.kaptcha.impl.WaterRipple
        // 鱼眼com.google.code.kaptcha.impl.FishEyeGimpy
        // 阴影com.google.code.kaptcha.impl.ShadowGimpy
        config.setProperty(KAPTCHA_OBSCURIFICATOR_IMPL, ShadowGimpy.class.getName());
        super.init(config);
    }

    @Override
    public void destroy(Properties config) {
        logger.info("start-clear-history-data:{}", captchaType());
    }

    @Override
    public String captchaType() {
        return "math";
    }

    @Override
    public SimpleEntry<String, String> getTextAndCode() {
        String capStr = null, code = null;
        String capText = defaultKaptcha.createText();
        capStr = capText.substring(0, capText.lastIndexOf("@"));
        code = capText.substring(capText.lastIndexOf("@") + 1);
        return new SimpleEntry<>(capStr, code);
    }
}
