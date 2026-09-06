package com.aidex.framework.cache;

import com.aidex.common.constant.CacheConstants;
import com.aidex.common.constant.Constants;
import com.aidex.common.core.redis.RedisCache;
import com.aidex.common.core.text.Convert;
import com.aidex.common.utils.spring.SpringUtils;
import com.aidex.system.domain.SysConfig;
import com.aidex.system.service.SysConfigService;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;

/**
 * 配置工具类
 *
 * @author ruoyi
 */
@Component
public class ConfigUtils implements CacheUtil {

    @Override
    public String getCacheName() {
        return CACHE_NAME;
    }

    @Override
    public String getRemark() {
        return "配置信息";
    }

    @Override
    public String getCacheId() {
        return "configUtils";
    }

    @Override
    public String getCacheValue(String key) {
        return REDIS_CACHE.getCacheMapValue(CACHE_NAME, key).toString();
    }

    @Override
    public Collection<String> getCacheKeys() {
        return REDIS_CACHE.getCacheMapKeys(CACHE_NAME);
    }

    @Override
    public void clearCacheByKeys(String... keys) {
        REDIS_CACHE.deleteCacheMapValue(CACHE_NAME, keys);
    }

    @Override
    public void clearCache() {
        clearConfigCache();
    }

    private static final String CACHE_NAME = "configCache";

    private static final RedisCache REDIS_CACHE;

    private static final SysConfigService sysConfigService;

    static {
        REDIS_CACHE = SpringUtils.getBean(RedisCache.class);
        sysConfigService = SpringUtils.getBean(SysConfigService.class);
    }


    /**
     * 清空字典缓存
     */
    public static void clearConfigCache() {
        Collection<String> keys = REDIS_CACHE.keys(CACHE_NAME);
        REDIS_CACHE.deleteObject(keys);
    }

    /**
     * 清空字典缓存
     */
    public static void clearConfigCache(String configKey) {
        REDIS_CACHE.deleteCacheMapValue(CACHE_NAME, getCacheKey(configKey));
    }

    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    public static String getCacheKey(String configKey) {
        return CacheConstants.SYS_CONFIG_KEY + configKey;
    }

    /**
     * 根据Key值获取配置信息
     *
     * @param configKey
     * @return
     */
    public static SysConfig getConfigByKey(String configKey) {
        SysConfig sysConfig = REDIS_CACHE.getCacheMapValue(CACHE_NAME, getCacheKey(configKey));
        if (null == sysConfig) {
            SysConfig sysConfigQuery = new SysConfig();
            sysConfigQuery.setConfigKey(configKey);
            List<SysConfig> configDatas = sysConfigService.findList(sysConfigQuery);
            if (CollectionUtils.isEmpty(configDatas)) {
                return null;
            } else {
                sysConfig = configDatas.get(0);
                REDIS_CACHE.setCacheMapValue(CACHE_NAME, getCacheKey(configKey), sysConfig);
            }
        }
        return sysConfig;
    }

    /**
     * 根据Key值获取配置值
     *
     * @param configKey
     * @return
     */
    public static String getConfigValueByKey(String configKey,String defaultValue) {
        SysConfig sysConfig = getConfigByKey(configKey);
        if (null == sysConfig) {
            return defaultValue;
        }
        return sysConfig.getConfigValue();
    }

    /**
     * 根据Key值获取配置值
     *
     * @param configKey
     * @return
     */
    public static String getConfigValueByKey(String configKey) {
        return getConfigValueByKey(configKey,"");
    }

    /**
     * 根据Key值获取配置的boolean值
     *
     * @param configKey
     * @return
     */
    public static boolean getConfigBooleanValueByKey(String configKey, Boolean defaultValue) {
        SysConfig sysConfig = getConfigByKey(configKey);
        if (null == sysConfig) {
            return defaultValue;
        }
        return Convert.toBool(sysConfig.getConfigValue(), defaultValue);
    }

    public static void setConfigCache(SysConfig sysConfig) {
        REDIS_CACHE.setCacheMapValue(CACHE_NAME, getCacheKey(sysConfig.getConfigKey()), sysConfig);
    }
}
