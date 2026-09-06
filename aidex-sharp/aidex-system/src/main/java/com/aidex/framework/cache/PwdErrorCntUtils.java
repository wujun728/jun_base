package com.aidex.framework.cache;

import com.aidex.common.constant.CacheConstants;
import com.aidex.common.core.redis.RedisCache;
import com.aidex.common.utils.spring.SpringUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 验证码缓存
 *
 * @author ruoyi
 */
@Component
public class PwdErrorCntUtils implements CacheUtil {

    private static final String CACHE_NAME = "pwdErrCnt";

    private static final RedisCache REDIS_CACHE;

    static {
        REDIS_CACHE = SpringUtils.getBean(RedisCache.class);
    }


    @Override
    public String getCacheName() {
        return CACHE_NAME;
    }

    @Override
    public String getCacheId() {
        return "pwdErrorCntUtils";
    }

    @Override
    public String getCacheValue(String key) {
        Integer retryCount = REDIS_CACHE.getCacheObject(key);
        if (null != retryCount) {
            return retryCount.toString();
        } else {
            return "";
        }
    }

    @Override
    public String getRemark() {
        return "密码错误次数";
    }

    @Override
    public void clearCache() {
        REDIS_CACHE.redisTemplate.delete(getCacheKeys());
    }

    @Override
    public void clearCacheByKeys(String... keys) {
        REDIS_CACHE.deleteKey(keys);
    }

    @Override
    public Collection<String> getCacheKeys() {
        return REDIS_CACHE.keys(CacheConstants.PWD_ERR_CNT_KEY + "*");
    }
}
