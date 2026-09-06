package com.ruoyi.web.controller.monitor;

import com.github.benmanes.caffeine.cache.Cache;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.CaffeineCacheImpl;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 缓存监控
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/monitor/cache")
public class CacheController
{
    @Autowired
    private RedisCache redisTemplate;

    private final static List<SysCache> caches = new ArrayList<SysCache>();
    {
        caches.add(new SysCache(CacheConstants.LOGIN_TOKEN_KEY, "用户信息"));
        caches.add(new SysCache(CacheConstants.SYS_CONFIG_KEY, "配置信息"));
        caches.add(new SysCache(CacheConstants.SYS_DICT_KEY, "数据字典"));
        caches.add(new SysCache(CacheConstants.CAPTCHA_CODE_KEY, "验证码"));
        caches.add(new SysCache(CacheConstants.REPEAT_SUBMIT_KEY, "防重提交"));
        caches.add(new SysCache(CacheConstants.RATE_LIMIT_KEY, "限流处理"));
        caches.add(new SysCache(CacheConstants.PWD_ERR_CNT_KEY, "密码错误次数"));
    }

    @SuppressWarnings("deprecation")
    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @GetMapping()
    public AjaxResult getInfo() throws Exception
    {
        Map<String, Object> result = new HashMap<>(3);
        if (redisTemplate instanceof RedisTemplate<?,?> realRedisTemplate )
        {
            Properties info = (Properties) realRedisTemplate.execute((RedisCallback<Object>) connection -> connection.info());
            Properties commandStats = (Properties) realRedisTemplate.execute((RedisCallback<Object>) connection -> connection.info("commandstats"));
            Object dbSize = realRedisTemplate.execute((RedisCallback<Object>) connection -> connection.dbSize());

            result.put("info", info);
            result.put("dbSize", dbSize);

            List<Map<String, String>> pieList = new ArrayList<>();
            commandStats.stringPropertyNames().forEach(key -> {
                Map<String, String> data = new HashMap<>(2);
                String property = commandStats.getProperty(key);
                data.put("name", StringUtils.removeStart(key, "cmdstat_"));
                data.put("value", StringUtils.substringBetween(property, "calls=", ",usec"));
                pieList.add(data);
            });
            result.put("commandStats", pieList);
            return AjaxResult.success(result);

        } else if (redisTemplate instanceof CaffeineCacheImpl caffeine) {
            Cache<?,?> caffeineCache = caffeine.getCaffeineCache();
            long size = caffeineCache.estimatedSize();
            Map<String, String> info = new HashMap<>();
            info.put("redis_version", "caffeine 内存缓存");
            info.put("redis_mode", "standalone");
            info.put("maxmemory_human", caffeine.getMaxSize() + "");
            info.put("used_memory_human", size + "");
            info.put("aof_enabled", "0");
            info.put("tcp_port", "无");
            info.put("instantaneous_input_kbps", "0");
            info.put("instantaneous_output_kbps", "0");

            result.put("dbSize", size);
            result.put("info", info);
            return AjaxResult.success(result);
        }


        return AjaxResult.success();
    }

    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @GetMapping("/getNames")
    public AjaxResult cache()
    {
        return AjaxResult.success(caches);
    }

    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @GetMapping("/getKeys/{cacheName}")
    public AjaxResult getCacheKeys(@PathVariable String cacheName)
    {
        Set<String> cacheKeys = (Set<String>) redisTemplate.keys(cacheName + "*");
        return AjaxResult.success(new TreeSet<>(cacheKeys));
    }

    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @GetMapping("/getValue/{cacheName}/{cacheKey}")
    public AjaxResult getCacheValue(@PathVariable String cacheName, @PathVariable String cacheKey)
    {
        Object cacheValue = redisTemplate.getCacheObject(cacheKey);
        SysCache sysCache = new SysCache(cacheName, cacheKey, Objects.toString(cacheValue));
        return AjaxResult.success(sysCache);
    }

    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @DeleteMapping("/clearCacheName/{cacheName}")
    public AjaxResult clearCacheName(@PathVariable String cacheName)
    {
        Collection<String> cacheKeys = redisTemplate.keys(cacheName + "*");
        redisTemplate.deleteObject(cacheKeys);
        return AjaxResult.success();
    }

    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @DeleteMapping("/clearCacheKey/{cacheKey}")
    public AjaxResult clearCacheKey(@PathVariable String cacheKey)
    {
        redisTemplate.deleteObject(cacheKey);
        return AjaxResult.success();
    }

    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @DeleteMapping("/clearCacheAll")
    public AjaxResult clearCacheAll()
    {
        Collection<String> cacheKeys = redisTemplate.keys("*");
        redisTemplate.deleteObject(cacheKeys);
        return AjaxResult.success();
    }
}
