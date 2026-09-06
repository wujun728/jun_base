package com.aidex.framework.cache;

import com.aidex.common.constant.CacheConstants;
import com.aidex.common.constant.Constants;
import com.aidex.common.core.domain.BaseEntity;
import com.aidex.common.core.redis.RedisCache;
import com.aidex.common.utils.StringUtils;
import com.aidex.common.utils.spring.SpringUtils;
import com.aidex.system.domain.SysDictData;
import com.aidex.system.service.SysDictDataService;
import com.alibaba.fastjson2.JSONArray;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 字典工具类
 *
 * @author ruoyi
 */
@Component
public class DictUtils implements CacheUtil {

    @Override
    public String getCacheName() {
        return CACHE_NAME;
    }

    @Override
    public String getRemark() {
        return "数据字典";
    }

    @Override
    public String getCacheId() {
        return "dictUtils";
    }

    @Override
    public Collection<String> getCacheKeys() {
        return REDIS_CACHE.getCacheMapKeys(CACHE_NAME);
    }

    @Override
    public String getCacheValue(String key) {
        return ((JSONArray) REDIS_CACHE.getCacheMapValue(CACHE_NAME, key)).toJSONString();
    }

    @Override
    public void clearCache() {
        clearDictCache();
    }

    @Override
    public void clearCacheByKeys(String... keys) {
        REDIS_CACHE.deleteCacheMapValue(CACHE_NAME, keys);
    }

    /**
     * 分隔符
     */
    public static final String SEPARATOR = ",";

    private static final String CACHE_NAME = "dictCache";

    private static final RedisCache REDIS_CACHE;

    private static final SysDictDataService sysDictDataService;

    static {
        REDIS_CACHE = SpringUtils.getBean(RedisCache.class);
        sysDictDataService = SpringUtils.getBean(SysDictDataService.class);
    }


    /**
     * 清空字典缓存
     */
    public static void clearDictCache() {
        Collection<String> keys = REDIS_CACHE.keys(CACHE_NAME);
        REDIS_CACHE.deleteObject(keys);
    }

    /**
     * 清空字典缓存
     */
    public static void clearDictCache(String dictType) {
        REDIS_CACHE.deleteCacheMapValue(CACHE_NAME, getCacheKey(dictType));
    }

    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    public static String getCacheKey(String configKey) {
        return CacheConstants.SYS_DICT_KEY + configKey;
    }

    /**
     * 根据字典类型和字典值获取字典标签
     *
     * @param dictType  字典类型
     * @param dictValue 字典值
     * @return 字典标签
     */
    public static String getDictLabel(String dictType, String dictValue) {
        return getDictLabel(dictType, dictValue, SEPARATOR);
    }

    /**
     * 根据字典类型和字典值获取字典标签
     *
     * @param dictType  字典类型
     * @param dictValue 字典值
     * @param separator 分隔符
     * @return 字典标签
     */
    public static String getDictLabel(String dictType, String dictValue, String separator) {
        StringBuilder propertyString = new StringBuilder();
        List<SysDictData> sysDictDatas = getAllDictList(dictType);
        if (!CollectionUtils.isEmpty(sysDictDatas)) {
            if (StringUtils.containsAny(separator, dictValue) && StringUtils.isNotEmpty(sysDictDatas)) {
                for (SysDictData dict : sysDictDatas) {
                    for (String value : dictValue.split(separator)) {
                        if (value.equals(dict.getDictValue())) {
                            propertyString.append(dict.getDictLabel() + separator);
                            break;
                        }
                    }
                }
            } else {
                for (SysDictData dict : sysDictDatas) {
                    if (dictValue.equals(dict.getDictValue())) {
                        return dict.getDictLabel();
                    }
                }
            }
        }
        return StringUtils.stripEnd(propertyString.toString(), separator);
    }

    /**
     * 根据字典类型和字典标签获取字典值
     *
     * @param dictType  字典类型
     * @param dictLabel 字典标签
     * @return 字典值
     */
    public static String getDictValue(String dictType, String dictLabel) {
        return getDictValue(dictType, dictLabel, SEPARATOR);
    }

    /**
     * 根据字典类型和字典标签获取字典值
     *
     * @param dictType  字典类型
     * @param dictLabel 字典标签
     * @param separator 分隔符
     * @return 字典值
     */
    public static String getDictValue(String dictType, String dictLabel, String separator) {
        StringBuilder propertyString = new StringBuilder();
        List<SysDictData> sysDictDatas = getAllDictList(dictType);
        if (!CollectionUtils.isEmpty(sysDictDatas)) {
            if (StringUtils.containsAny(separator, dictLabel)) {
                for (SysDictData dict : sysDictDatas) {
                    for (String label : dictLabel.split(separator)) {
                        if (label.equals(dict.getDictLabel())) {
                            propertyString.append(dict.getDictValue() + separator);
                            break;
                        }
                    }
                }
            } else {
                for (SysDictData dict : sysDictDatas) {
                    if (dictLabel.equals(dict.getDictLabel())) {
                        return dict.getDictValue();
                    }
                }
            }
        }
        return StringUtils.stripEnd(propertyString.toString(), separator);
    }

    /**
     * 获取数据字典列表：仅包含有效的
     *
     * @param dictType 字典编码
     * @return
     */
    public static List<SysDictData> getDictList(String dictType) {
        return getAllDictList(dictType).stream().filter(dict -> dict.getStatus().equals(BaseEntity.STATUS_NORMAL)).collect(Collectors.toList());
    }

    /**
     * 获取多个数据字典列表：仅包含有效的
     *
     * @param dictTypes 字典编码
     * @return
     */
    public static Map<String, List<SysDictData>> getMultiDictList(String dictTypes) {
        Map<String, List<SysDictData>> sysDictsMap = new HashMap<String, List<SysDictData>>();
        if (StringUtils.isNotBlank(dictTypes)) {
            for (String dictType : dictTypes.split(SEPARATOR)) {
                sysDictsMap.put(dictType, getAllDictList(dictType).stream().filter(dict -> dict.getStatus().equals(BaseEntity.STATUS_NORMAL)).collect(Collectors.toList()));
            }
        }
        return sysDictsMap;
    }

    /**
     * 获取所有的数据字典：包含有效和无效的，用于主页面展示
     *
     * @param dictType 字典编码
     * @return
     */
    public static List<SysDictData> getAllDictList(String dictType) {
        List<SysDictData> dictDatas = new ArrayList<SysDictData>();
        JSONArray arrayCache  = REDIS_CACHE.getCacheMapValue(CACHE_NAME, getCacheKey(dictType));
        if (StringUtils.isNotNull(arrayCache )) {
            dictDatas = arrayCache .toList(SysDictData.class);
            return dictDatas;
        } else {
            SysDictData sysDictData = new SysDictData();
            sysDictData.setDictType(dictType);
            dictDatas = sysDictDataService.findList(sysDictData);
            setDictCache(dictType,dictDatas);
            return dictDatas;
        }
    }

    /**
     * 设置缓存
     * @param dictType
     */
    public static void setDictCache(String dictType) {
        SysDictData sysDictDataQuery = new SysDictData();
        sysDictDataQuery.setDictType(dictType);
        setDictCache(dictType,sysDictDataService.findList(sysDictDataQuery));
    }

    /**
     * 设置缓存
     * @param dictType
     * @param dictDatas
     */
    public static void setDictCache(String dictType, List<SysDictData> dictDatas) {
        REDIS_CACHE.setCacheMapValue(CACHE_NAME, getCacheKey(dictType), dictDatas);
    }
}
