package com.ruoyi.web.controller.monitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.CacheUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysCache;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 缓存监控
 * 
 * @author ruoyi
 */
@Tag(name = "缓存监控")
@RestController
@RequestMapping("/monitor/cache")
public class CacheController {

    private static String tmpCacheName = "";

    private final static List<SysCache> caches = new ArrayList<SysCache>();
    {
        caches.add(new SysCache(CacheConstants.LOGIN_TOKEN_KEY, "用户信息"));
        caches.add(new SysCache(CacheConstants.SYS_CONFIG_KEY, "配置信息"));
        caches.add(new SysCache(CacheConstants.SYS_DICT_KEY, "数据字典"));
        caches.add(new SysCache(CacheConstants.CAPTCHA_CODE_KEY, "验证码"));
        caches.add(new SysCache(CacheConstants.PHONE_CODES, "短信验证码"));
        caches.add(new SysCache(CacheConstants.EMAIL_CODES, "邮箱验证码"));
        caches.add(new SysCache(CacheConstants.REPEAT_SUBMIT_KEY, "防重提交"));
        caches.add(new SysCache(CacheConstants.RATE_LIMIT_KEY, "限流处理"));
        caches.add(new SysCache(CacheConstants.PWD_ERR_CNT_KEY, "密码错误次数"));
        caches.add(new SysCache(CacheConstants.IP_ERR_CNT_KEY, "IP错误次数"));
        caches.add(new SysCache(CacheConstants.FILE_MD5_PATH_KEY, "path-md5"));
        caches.add(new SysCache(CacheConstants.FILE_PATH_MD5_KEY, "md5-path"));
    }

    @Operation(summary = "获取缓存名列表")
    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @GetMapping("/getNames")
    public AjaxResult cache() {
        return AjaxResult.success(caches);
    }

    @Operation(summary = "获取缓存键列表")
    @Parameters({
            @Parameter(name = "cacheName", description = "缓存名称", required = true),
    })
    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @GetMapping("/getKeys/{cacheName}")
    public AjaxResult getCacheKeys(@PathVariable String cacheName) {
        tmpCacheName = cacheName;
        Set<String> keyset = CacheUtils.getkeys(cacheName);
        return AjaxResult.success(keyset);
    }

    @Operation(summary = "获取缓存值列表")
    @Parameters({
            @Parameter(name = "cacheName", description = "缓存名称", required = true),
            @Parameter(name = "cacheKey", description = "缓存键名", required = true)
    })
    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @GetMapping("/getValue/{cacheName}/{cacheKey}")
    public AjaxResult getCacheValue(@PathVariable String cacheName, @PathVariable String cacheKey) {
        ValueWrapper valueWrapper = CacheUtils.get(cacheName, cacheKey);
        SysCache sysCache = new SysCache();
        sysCache.setCacheName(cacheName);
        sysCache.setCacheKey(cacheKey);
        if (StringUtils.isNotNull(valueWrapper)) {
            sysCache.setCacheValue(Convert.toStr(valueWrapper.get(), ""));
        }
        return AjaxResult.success(sysCache);
    }

    @Operation(summary = "清除缓存")
    @Parameters({
            @Parameter(name = "cacheName", description = "缓存名称", required = true)
    })
    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @DeleteMapping("/clearCacheName/{cacheName}")
    public AjaxResult clearCacheName(@PathVariable String cacheName) {
        CacheUtils.clear(cacheName);
        return AjaxResult.success();
    }

    @Operation(summary = "清除缓存值")
    @Parameters({
            @Parameter(name = "cacheKey", description = "缓存键名", required = true)
    })
    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @DeleteMapping("/clearCacheKey/{cacheKey}")
    public AjaxResult clearCacheKey(@PathVariable String cacheKey) {
        CacheUtils.removeIfPresent(tmpCacheName, cacheKey);
        return AjaxResult.success();
    }

    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @DeleteMapping("/clearCacheAll")
    public AjaxResult clearCacheAll() {
        for (String cacheName : CacheUtils.getCacheManager().getCacheNames()) {
            CacheUtils.clear(cacheName);
        }
        return AjaxResult.success();
    }
}
