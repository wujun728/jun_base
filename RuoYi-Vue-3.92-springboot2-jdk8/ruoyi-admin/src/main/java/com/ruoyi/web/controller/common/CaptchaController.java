package com.ruoyi.web.controller.common;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.sign.Base64;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.system.service.ISysConfigService;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.SpecCaptcha;

@RestController
public class CaptchaController
{
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysConfigService configService;

    @GetMapping("/captchaImage")
    public AjaxResult getCode(HttpServletResponse response) throws IOException
    {
        AjaxResult ajax = AjaxResult.success();
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        ajax.put("captchaEnabled", captchaEnabled);
        if (!captchaEnabled)
        {
            return ajax;
        }

        String uuid = IdUtils.simpleUUID();
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;

        String code;
        String img;

        String captchaType = RuoYiConfig.getCaptchaType();
        if ("math".equals(captchaType))
        {
            ArithmeticCaptcha captcha = new ArithmeticCaptcha(160, 60);
            captcha.setLen(2);
            code = captcha.text();
            img = captcha.toBase64().replace("data:image/png;base64,", "");
        }
        else
        {
            SpecCaptcha captcha = new SpecCaptcha(160, 60, 4);
            code = captcha.text().toLowerCase();
            img = captcha.toBase64().replace("data:image/png;base64,", "");
        }

        redisCache.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        ajax.put("uuid", uuid);
        ajax.put("img", img);
        return ajax;
    }
}