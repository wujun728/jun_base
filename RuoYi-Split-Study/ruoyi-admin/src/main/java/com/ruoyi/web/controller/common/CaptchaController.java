package com.ruoyi.web.controller.common;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.code.kaptcha.Producer;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.sign.Base64;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.system.service.ISysConfigService;

/**
 * 验证码操作处理
 *
 * @author ruoyi
 */
@RestController
public class CaptchaController
{
    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysConfigService configService;
    /**
     * 生成验证码
     */
    @GetMapping("/captchaImage")
    public AjaxResult getCode(HttpServletResponse response) throws IOException
    {
        AjaxResult ajax = AjaxResult.success();
        // 从redis中读取缓存的配置，是否开启验证码
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        ajax.put("captchaEnabled", captchaEnabled);
        if (!captchaEnabled)
        {
            // 不开启直接返回
            return ajax;
        }

        // 保存验证码信息，存入redis中的Key值
        String uuid = IdUtils.simpleUUID();
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;

        String capStr = null, code = null;
        BufferedImage image = null;

        // 生成验证码，读取yml配置文件中的验证码类型：math
        String captchaType = RuoYiConfig.getCaptchaType();
        if ("math".equals(captchaType))
        {
            // 核心方法 --> 返回一个数学表达式类似于： 5-1=?@4
            // 这里的验证码生成使用了google kaptcha的验证码组件，没有重复造轮子，具体的生成逻辑作者重写了
            // 这里生成表达式的方法（重写）在 com.ruoyi.framework.config包下的KaptchaTextCreator验证码文本生成器类
            String capText = captchaProducerMath.createText();

            // 截取转化为图片的字符，比如：5-1=？
            capStr = capText.substring(0, capText.lastIndexOf("@"));

            // 截取生成的数学表达式结果
            code = capText.substring(capText.lastIndexOf("@") + 1);

            // 将截取的表达式转为图片
            image = captchaProducerMath.createImage(capStr);
        }

        // 如果是yml配置文件中的验证码类型为char，则生成的验证码类似于：4nm7
        else if ("char".equals(captchaType))
        {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }

        // 将结果存入Redis中存入形式:  verifyKey : code (KV形式)
        redisCache.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try
        {
            ImageIO.write(image, "jpg", os);
        }
        catch (IOException e)
        {
            return AjaxResult.error(e.getMessage());
        }

        // 将UUID返回前端，当用户提交登录时，便可带上这个UUID与后端进行验证码校验
        ajax.put("uuid", uuid);

        // 以Base64的方式返回给前端
        ajax.put("img", Base64.encode(os.toByteArray()));
        return ajax;
    }
}
