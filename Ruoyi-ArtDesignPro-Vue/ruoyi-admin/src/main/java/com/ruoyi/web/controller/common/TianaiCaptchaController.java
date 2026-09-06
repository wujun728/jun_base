package com.ruoyi.web.controller.common;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cloud.tianai.captcha.application.ImageCaptchaApplication;
import cloud.tianai.captcha.application.vo.ImageCaptchaVO;
import cloud.tianai.captcha.common.constant.CaptchaTypeConstant;
import cloud.tianai.captcha.common.response.ApiResponse;
import cloud.tianai.captcha.validator.common.model.dto.ImageCaptchaTrack;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.system.service.ISysConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

/**
 * 天爱行为验证码。
 *
 * @author ruoyi
 */
@Tag(name = "行为验证码")
@RestController
@RequestMapping("/captcha")
public class TianaiCaptchaController
{
    private static final Integer CAPTCHA_TOKEN_EXPIRATION = 2;

    private static final List<String> CAPTCHA_TYPES = List.of(
            CaptchaTypeConstant.SLIDER,
            CaptchaTypeConstant.ROTATE,
            CaptchaTypeConstant.CONCAT,
            CaptchaTypeConstant.WORD_IMAGE_CLICK);

    @Autowired
    private ImageCaptchaApplication imageCaptchaApplication;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysConfigService configService;

    /**
     * 生成滑块验证码。
     */
    @Operation(summary = "获取行为验证码")
    @PostMapping("/get")
    public ApiResponse<ImageCaptchaVO> get()
    {
        return imageCaptchaApplication.generateCaptcha(getCaptchaType());
    }

    /**
     * 校验滑块验证码，成功后签发一次性登录 token。
     */
    @Operation(summary = "校验行为验证码并签发一次性登录凭证")
    @PostMapping("/check")
    public ApiResponse<?> check(@RequestBody CaptchaCheckBody body)
    {
        if (StringUtils.isEmpty(body.getId()) || body.getData() == null)
        {
            return ApiResponse.ofCheckError("验证码参数不能为空");
        }
        ApiResponse<?> response = imageCaptchaApplication.matching(body.getId(), body.getData());
        if (!response.isSuccess())
        {
            return response;
        }
        String token = IdUtils.fastSimpleUUID();
        redisCache.setCacheObject(CacheConstants.CAPTCHA_TOKEN_KEY + token, body.getId(), CAPTCHA_TOKEN_EXPIRATION, TimeUnit.MINUTES);
        return ApiResponse.ofSuccess(Collections.singletonMap("token", token));
    }

    private String getCaptchaType()
    {
        String type = configService.selectConfigByKey("sys.account.behaviorCaptchaType");
        if ("RANDOM".equalsIgnoreCase(type))
        {
            return CAPTCHA_TYPES.get(ThreadLocalRandom.current().nextInt(CAPTCHA_TYPES.size()));
        }
        return CAPTCHA_TYPES.contains(type) ? type : CaptchaTypeConstant.SLIDER;
    }

    public static class CaptchaCheckBody
    {
        private String id;

        private ImageCaptchaTrack data;

        public String getId()
        {
            return id;
        }

        public void setId(String id)
        {
            this.id = id;
        }

        public ImageCaptchaTrack getData()
        {
            return data;
        }

        public void setData(ImageCaptchaTrack data)
        {
            this.data = data;
        }
    }
}
