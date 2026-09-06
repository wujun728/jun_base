package ginyi.framework.security.service;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.extra.servlet.ServletUtil;
import ginyi.common.constant.CacheConstants;
import ginyi.common.exception.CommonException;
import ginyi.common.redis.cache.RedisCache;
import ginyi.common.constant.CommonMessageConstants;
import ginyi.common.result.StateCode;
import ginyi.common.utils.Constants;
import ginyi.common.utils.StringUtils;
import ginyi.system.service.ISysConfigService;
import ginyi.system.service.IVerifyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@Service
public class SysVerifyServiceImpl implements IVerifyService {


    @Resource
    private HttpServletRequest request;
    @Resource
    private RedisCache redisCache;
    @Resource
    private ISysConfigService configService;

    /**
     * 图片验证码
     */
    @Override
    public String captcha() {

        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (!captchaEnabled) {
            return null;
        }

        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(200, 100, 4, 15);
        String img = captcha.getImageBase64();
        String code = captcha.getCode();

        String clientIP = ServletUtil.getClientIP(request);
        String userAgent = ServletUtil.getHeader(request, "User-Agent", "utf-8");

        // 缓存key
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + SecureUtil.md5(clientIP + userAgent);
        redisCache.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);

        return img;
    }

    /**
     * 校验图片验证码
     *
     * @param code
     */
    @Override
    public void checkImgCode(String code) {
        String clientIP = ServletUtil.getClientIP(request);
        String userAgent = ServletUtil.getHeader(request, "User-Agent", "utf-8");

        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + SecureUtil.md5(clientIP + userAgent);
        String captcha = redisCache.getCacheObject(verifyKey, String.class);
        if(StringUtils.isNull(code)){
            throw new CommonException(StateCode.ERROR_NOT_EXIST, CommonMessageConstants.SYS_CAPTCHA_NOT_EXIST);
        }
        // 验证码失效
        if (captcha == null) {
            throw new CommonException(StateCode.ERROR_PARAMS_SERVICE, CommonMessageConstants.VERIFY_EXPIRE);
        }
        if (!captcha.equalsIgnoreCase(code)) {
            throw new CommonException(StateCode.ERROR_PARAMS_SERVICE, CommonMessageConstants.VERiFY_INCORRECT);
        } else {
            // 输入正确，删除该验证码
            redisCache.removeCacheObject(verifyKey);
        }
    }


}
