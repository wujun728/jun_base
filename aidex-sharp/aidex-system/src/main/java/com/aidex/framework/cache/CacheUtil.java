package com.aidex.framework.cache;

import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 缓存接口类
 *
 * @author aidex
 */
@Component
public interface CacheUtil {

    /**
     * 获取缓存名
     * @return
     */
    public String getCacheName();

    /**
     * bean对象
     * @return
     */
    public String getCacheId();

    /**
     * 清除缓存信息
     */
    public String getCacheValue(String key);

    /**
     * 备注
     * @return
     */
    public String getRemark();

    /**
     * 清除缓存信息
     */
    public void clearCache();

    /**
     * 清除缓存信息
     */
    public void clearCacheByKeys(String... keys);

    /**
     * 获取缓存名
     * @return
     */
    public Collection<String> getCacheKeys();

}
