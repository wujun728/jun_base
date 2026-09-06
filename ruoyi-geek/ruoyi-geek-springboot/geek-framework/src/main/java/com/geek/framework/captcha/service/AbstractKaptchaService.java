package com.geek.framework.captcha.service;

import java.awt.image.BufferedImage;
import java.util.AbstractMap.SimpleEntry;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anji.captcha.model.common.RepCodeEnum;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.impl.CaptchaServiceFactory;
import com.anji.captcha.util.AESUtil;
import com.anji.captcha.util.ImageUtils;
import com.anji.captcha.util.RandomUtils;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

public abstract class AbstractKaptchaService extends AbstractBaseCaptchaService {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected DefaultKaptcha defaultKaptcha;
    protected static String REDIS_CAPTCHA_KEY = "RUNNING:CAPTCHA:%s";
    protected static String REDIS_SECOND_CAPTCHA_KEY = "RUNNING:CAPTCHA:second-%s";
    protected static Long EXPIRESIN_SECONDS = 2 * 60L;
    protected static Long EXPIRESIN_THREE = 3 * 60L;

    @Override
    public void init(Properties config) {
        this.defaultKaptcha = new DefaultKaptcha();
        // 防御性复制，避免不同实现共享同一个Properties实例导致配置“串味”
        Properties kaptchaProps = new Properties();
        kaptchaProps.putAll(config);
        defaultKaptcha.setConfig(new Config(kaptchaProps));
        super.init(config);
    }

    @Override
    public ResponseModel get(CaptchaVO captchaVO) {
        ResponseModel r = super.get(captchaVO);
        if (!validatedReq(r)) {
            return r;
        }

        SimpleEntry<String, String> textAndCode = getTextAndCode();
        String capStr = textAndCode.getKey(), code = textAndCode.getValue(), secretKey = AESUtil.getKey();
        BufferedImage image = defaultKaptcha.createImage(capStr);
        CaptchaVO dataVO = new CaptchaVO();
        dataVO.setOriginalImageBase64(ImageUtils.getImageToBase64Str(image).replaceAll("\r|\n", ""));
        dataVO.setToken(RandomUtils.getUUID());
        dataVO.setSecretKey(secretKey);
        String codeKey = String.format(REDIS_CAPTCHA_KEY, dataVO.getToken());
        CaptchaServiceFactory.getCache(cacheType).set(codeKey, code, EXPIRESIN_SECONDS);
        return ResponseModel.successData(dataVO);
    }

    @Override
    public ResponseModel check(CaptchaVO captchaVO) {
        ResponseModel r = super.check(captchaVO);
        if (!validatedReq(r)) {
            return r;
        }
        String codeKey = String.format(REDIS_CAPTCHA_KEY, captchaVO.getToken());
        if (!CaptchaServiceFactory.getCache(cacheType).exists(codeKey)) {
            return ResponseModel.errorMsg(RepCodeEnum.API_CAPTCHA_INVALID);
        }
        String s = CaptchaServiceFactory.getCache(cacheType).get(codeKey);
        StringBuilder sb = new StringBuilder();
        String v = "";
        try {
            captchaVO.getWordList().forEach(word -> sb.append(word));
            v = sb.toString();
            if (!s.equalsIgnoreCase(v)) {
                return ResponseModel.errorMsg(RepCodeEnum.API_CAPTCHA_COORDINATE_ERROR);
            }
        } catch (Exception e) {
            return ResponseModel.errorMsg(RepCodeEnum.API_CAPTCHA_COORDINATE_ERROR);
        }

        CaptchaServiceFactory.getCache(cacheType).delete(codeKey);
        String secretKey = captchaVO.getSecretKey();
        try {
            String value = AESUtil.aesEncrypt(captchaVO.getToken().concat("---").concat(v), secretKey);
            String secondKey = String.format(REDIS_SECOND_CAPTCHA_KEY, value);
            CaptchaServiceFactory.getCache(cacheType).set(secondKey, captchaVO.getToken(), EXPIRESIN_THREE);
        } catch (Exception e) {
            logger.error("AES加密失败", e);
            afterValidateFail(captchaVO);
            return ResponseModel.errorMsg(e.getMessage());
        }

        return ResponseModel.successData(captchaVO);
    }

    @Override
    public ResponseModel verification(CaptchaVO captchaVO) {
        ResponseModel r = super.verification(captchaVO);
        if (!validatedReq(r)) {
            return r;
        }
        String codeKey = String.format(REDIS_SECOND_CAPTCHA_KEY, captchaVO.getCaptchaVerification());
        if (!CaptchaServiceFactory.getCache(cacheType).exists(codeKey)) {
            return ResponseModel.errorMsg(RepCodeEnum.API_CAPTCHA_INVALID);
        }
        // 二次校验取值后，即刻失效
        CaptchaServiceFactory.getCache(cacheType).delete(codeKey);
        return ResponseModel.success();
    }

    public void destroy(Properties config) {
    }

    abstract protected SimpleEntry<String, String> getTextAndCode();

}
