package com.aidex.framework.cache;

import com.aidex.common.constant.CacheConstants;
import com.aidex.common.constant.Constants;
import com.aidex.common.core.redis.RedisCache;
import com.aidex.common.utils.spring.SpringUtils;
import com.aidex.system.domain.SysTableConfig;
import com.aidex.system.service.SysTableConfigService;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 用户列配置工具类
 * @author aidex
 * @email aidex@qq.com
 */
@Component
public class TableConfigUtils {

    public String getCacheName() {
        return CACHE_NAME;
    }

    public String getRemark() {
        return "列配置信息";
    }

    public void clearCache() {
        clearConfigCache();
    }

    public String getCacheValue(String key) {
        return REDIS_CACHE.getCacheMapValue(CACHE_NAME, key).toString();
    }

    public Collection<String> getCacheKeys() {
        return REDIS_CACHE.getCacheMapKeys(CACHE_NAME);
    }

    public String getCacheId() {
        return "tableConfigUtils";
    }

    public void clearCacheByKeys(String... keys) {
        REDIS_CACHE.deleteCacheMapValue(CACHE_NAME, keys);
    }

    /**
     * 分隔符
     */
    public static final String SEPARATOR = ",";

    private static final String CACHE_NAME = "sysTableConfigCache";

    private static final RedisCache REDIS_CACHE;

    private static final SysTableConfigService sysTableConfigService;

    static {
        REDIS_CACHE = SpringUtils.getBean(RedisCache.class);
        sysTableConfigService = SpringUtils.getBean(SysTableConfigService.class);
    }


    /**
     * 清空缓存
     */
    public static void clearConfigCache() {
        Collection<String> keys = REDIS_CACHE.keys(CACHE_NAME);
        REDIS_CACHE.deleteObject(keys);
    }
    public static void  setTableConfig(SysTableConfig sysTableConfig){
        String userId = sysTableConfig.getUserId();
        String tableKey = sysTableConfig.getTableKey();
        clearCacheByKey(sysTableConfig);
        sysTableConfig.setConfigType("U");//添加编辑的列配置，都为用户自定义,由于使用了缓存，如果不设置该类型导致保存后没有类型，再次保存数据异常
        REDIS_CACHE.setCacheMapValue(CACHE_NAME, CacheConstants.SYS_TABLE_CONFIG_KEY + userId + tableKey, sysTableConfig);
    }
    public static void clearCacheByKey(SysTableConfig sysTableConfig) {
        String userId = sysTableConfig.getUserId();
        String tableKey = sysTableConfig.getTableKey();
        REDIS_CACHE.deleteCacheMapValue(CACHE_NAME, CacheConstants.SYS_TABLE_CONFIG_KEY + userId + tableKey);
    }
    /**
     * 根据用户userId和tableKey获取列配置
     * @param userId
     * @param tableKey
     * @return
     */
    public static SysTableConfig getTableConfig(String userId, String tableKey) {
        SysTableConfig sysTableConfig  = REDIS_CACHE.getCacheMapValue(CACHE_NAME, CacheConstants.SYS_TABLE_CONFIG_KEY + userId + tableKey);
        if(sysTableConfig == null){
                sysTableConfig = sysTableConfigService.getInfoByTableKeyFormDb(tableKey);
                if(sysTableConfig != null ){
                    REDIS_CACHE.setCacheMapValue(CACHE_NAME, CacheConstants.SYS_TABLE_CONFIG_KEY + userId + tableKey, sysTableConfig);
                }
        }
        return sysTableConfig;
    }
}
