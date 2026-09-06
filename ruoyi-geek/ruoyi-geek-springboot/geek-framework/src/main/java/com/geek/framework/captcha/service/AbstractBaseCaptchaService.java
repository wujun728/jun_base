package com.geek.framework.captcha.service;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anji.captcha.model.common.RepCodeEnum;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaCacheService;
import com.anji.captcha.service.CaptchaService;
import com.anji.captcha.service.impl.CaptchaServiceFactory;
import com.anji.captcha.service.impl.FrequencyLimitHandler;
import com.anji.captcha.util.CacheUtil;
import com.anji.captcha.util.MD5Util;
import com.anji.captcha.util.StringUtils;

public abstract class AbstractBaseCaptchaService implements CaptchaService {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected static String cacheType;
    private static FrequencyLimitHandler limitHandler;

    public void init(Properties config) {
        cacheType = config.getProperty("captcha.cacheType", "local");
        if (cacheType.equals("local")) {
            this.logger.info("初始化local缓存...");
            CacheUtil.init(Integer.parseInt(config.getProperty("captcha.cache.number", "1000")),
                    Long.parseLong(config.getProperty("captcha.timing.clear", "180")));
        }
        if (config.getProperty("captcha.req.frequency.limit.enable", "0").equals("1") && limitHandler == null) {
            this.logger.info("接口分钟内限流开关...开启...");
            limitHandler = new FrequencyLimitHandler.DefaultLimitHandler(config, this.getCacheService(cacheType));
        }
    }

    protected CaptchaCacheService getCacheService(String cacheType) {
        return CaptchaServiceFactory.getCache(cacheType);
    }

    @Override
    public ResponseModel get(CaptchaVO captchaVO) {
        if (limitHandler != null) {
            captchaVO.setClientUid(getValidateClientId(captchaVO));
            return limitHandler.validateGet(captchaVO);
        }
        return null;
    }

    @Override
    public ResponseModel check(CaptchaVO captchaVO) {
        if (limitHandler != null) {
            captchaVO.setClientUid(getValidateClientId(captchaVO));
            // check 阶段应使用 validateCheck，避免与 get 阶段共享同一限流计数
            return limitHandler.validateCheck(captchaVO);
        }
        return null;
    }

    @Override
    public ResponseModel verification(CaptchaVO captchaVO) {
        if (captchaVO == null) {
            return RepCodeEnum.NULL_ERROR.parseError("captchaVO");
        }
        if (StringUtils.isEmpty(captchaVO.getCaptchaVerification())) {
            return RepCodeEnum.NULL_ERROR.parseError("captchaVerification");
        }
        if (limitHandler != null) {
            return limitHandler.validateVerify(captchaVO);
        }
        return null;
    }

    /**
     * 验证请求结果是否通过
     * @param resp
     * @return
     */
    protected boolean validatedReq(ResponseModel resp) {
        return resp == null || resp.isSuccess();
    }

    /**
     * 获取客户端识别标志
     * @param req
     * @return 客户端标志
     */
    protected String getValidateClientId(CaptchaVO req) {
        // 以服务端获取的客户端标识 做识别标志
        if (StringUtils.isNotEmpty(req.getBrowserInfo())) {
            return MD5Util.md5(req.getBrowserInfo());
        }
        // 以客户端Ui组件id做识别标志
        if (StringUtils.isNotEmpty(req.getClientUid())) {
            return req.getClientUid();
        }
        return null;
    }

    /**
     * 验证失败后处理
     * @param data 验证数据
     */
    protected void afterValidateFail(CaptchaVO data) {
        if (limitHandler != null) {
            // 验证失败 分钟内计数
            String fails = String.format(FrequencyLimitHandler.LIMIT_KEY, "FAIL", data.getClientUid());
            CaptchaCacheService cs = getCacheService(cacheType);
            boolean getCountsKeyExists = cs.exists(fails);
            if (!getCountsKeyExists) {
                cs.set(fails, "1", 60L);
            }
            cs.increment(fails, 1L);
        }
    }

}
