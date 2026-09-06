package com.aidex.web.controller.monitor;

import com.aidex.common.constant.CacheConstants;
import com.aidex.common.core.domain.AjaxResult;
import com.aidex.common.core.domain.R;
import com.aidex.common.utils.StringUtils;
import com.aidex.common.utils.spring.SpringUtils;
import com.aidex.framework.cache.CacheUtil;
import com.aidex.system.domain.SysCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * 缓存监控
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/monitor/cache")
public class CacheController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ValueOperations valueOperations;

    @Autowired
    private HashOperations hashOperations;

    @Autowired
    private ListOperations listOperations;

    @Autowired
    private SetOperations setOperations;

    @Autowired
    private ZSetOperations zSetOperations;

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

    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @GetMapping()
    public AjaxResult getInfo() throws Exception {
        Properties info = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info());
        Properties commandStats = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info("commandstats"));
        Object dbSize = redisTemplate.execute((RedisCallback<Object>) connection -> connection.dbSize());

        Map<String, Object> result = new HashMap<>(3);
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
    }

    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @GetMapping("/getCacheName")
    public AjaxResult getCacheName() throws Exception {
        Set<String> keys = redisTemplate.keys("*");
        for (String key : keys) {
            DataType dataType = redisTemplate.type(key);
            if (dataType.name().equals(DataType.STRING.name())) {

            } else if (dataType.name().equals(DataType.HASH.name())) {
                System.out.println("key:" + key + ";keys:" + hashOperations.keys(key));
                Set<String> hashKeys = hashOperations.keys(key);
                for (String hashKey : hashKeys) {
                    System.out.println("hashKey:" + hashKey + ";value：" + hashOperations.get(key, hashKey).toString());
                }
            }
            System.out.println(dataType.name());
        }
        return AjaxResult.success();
    }

    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @GetMapping("/listCacheName")
    public R<List> cacheNameList(HttpServletRequest request, HttpServletResponse response) {
        List<Map<String, String>> cacheNameList = new ArrayList<Map<String, String>>();
        SpringUtils.getApplicationContext().getBeansOfType(CacheUtil.class).entrySet().forEach(entry -> {
            Map<String, String> cacheName = new HashMap<String, String>(3);
            cacheName.put("cacheName", entry.getValue().getCacheName());
            cacheName.put("cacheId", entry.getValue().getCacheId());
            cacheName.put("remarks", entry.getValue().getRemark());
            cacheNameList.add(cacheName);
        });
        return R.data(cacheNameList);
    }

    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @DeleteMapping("/clearCache/{cacheId}")
    public R clearCache(@PathVariable @NotNull String cacheId) {
        CacheUtil cacheUtil = SpringUtils.getBean(cacheId);
        cacheUtil.clearCache();
        return R.status(Boolean.TRUE);
    }

    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @GetMapping("/listCacheKey/{cacheId}")
    public R<List> listCacheKey(@PathVariable @NotNull String cacheId) {
        List<Map<String, String>> cacheNameList = new ArrayList<Map<String, String>>();
        CacheUtil cacheUtil = SpringUtils.getBean(cacheId);
        cacheUtil.getCacheKeys().stream().forEach(cacheKey -> {
            Map<String, String> cacheKeyMap = new HashMap<String, String>(2);
            cacheKeyMap.put("cacheKey", cacheKey);
            cacheKeyMap.put("cacheId", cacheId);
            cacheNameList.add(cacheKeyMap);
        });
        return R.data(cacheNameList);
    }

    /**
     * 清空缓存信息
     * @param cacheId
     * @param cacheKey
     * @return
     */
    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @DeleteMapping("/clearCacheByKey/{cacheId}/{cacheKey}")
    public R clearCacheByKey(@PathVariable @NotNull String cacheId, @PathVariable @NotNull String cacheKey) {
        CacheUtil cacheUtil = SpringUtils.getBean(cacheId);
        cacheUtil.clearCacheByKeys(cacheKey);
        return R.status(Boolean.TRUE);
    }

    /**
     * 获取缓存的详细信息
     */
    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @GetMapping(value = "/getCacheValue//{cacheId}/{cacheKey}")
    public R<Map<String, String>> getCacheValue(@PathVariable @NotNull String cacheId, @PathVariable @NotNull String cacheKey) {
        CacheUtil cacheUtil = SpringUtils.getBean(cacheId);
        String cacheValue = cacheUtil.getCacheValue(cacheKey);
        Map<String, String> cacheMap = new HashMap<String, String>(3);
        cacheMap.put("cacheName",cacheUtil.getCacheName());
        cacheMap.put("cacheKey",cacheKey);
        cacheMap.put("cacheValue",cacheValue);
        return R.data(cacheMap);
    }
}
