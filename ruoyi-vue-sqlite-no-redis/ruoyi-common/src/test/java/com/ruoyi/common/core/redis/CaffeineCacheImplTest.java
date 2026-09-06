package com.ruoyi.common.core.redis;

import org.junit.jupiter.api.Assertions;

import java.util.*;
import java.util.concurrent.TimeUnit;

class CaffeineCacheImplTest {


    private static CaffeineCacheImpl cache = new CaffeineCacheImpl();


    private record CacheData (String msg) {

    }

    void print(String prefix) {
        System.out.println(prefix+":当前缓存情况：");
        cache.getCaffeineCache().asMap().forEach((key, value) -> {
            System.out.println(key + ":" + value);
        });
    }


    void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    @org.junit.jupiter.api.Test
    void setCacheObject() {
        cache.setCacheObject("key1", "value");
        Assertions.assertEquals("value", cache.getCacheObject("key1"));
        print("setCacheObject 后");
    }


    @org.junit.jupiter.api.Test
    void testSetCacheObject() {
        cache.setCacheObject("key2", "value2");
        Assertions.assertEquals("value2", cache.getCacheObject("key2"));
        Assertions.assertNull(cache.getCacheObject("key_none"));

        print("testSetCacheObject 后");

    }

    @org.junit.jupiter.api.Test
    void expire() {
        cache.setCacheObject("key3", "value3");
        cache.expire("key3", 2);
        Assertions.assertEquals("value3", cache.getCacheObject("key3"));
        sleep(3);
        Assertions.assertNull(cache.getCacheObject("key3"));

        print("expire 后");
    }

    @org.junit.jupiter.api.Test
    void testExpire() {
        CacheData v4 = new CacheData("value4");
        String key = "key4";
        cache.setCacheObject(key, v4);
        cache.expire(key,2, TimeUnit.SECONDS);
        Assertions.assertEquals(v4, cache.getCacheObject(key));
        sleep(3);
        Assertions.assertNull(cache.getCacheObject(key));

        print("testExpire 后");
    }

    @org.junit.jupiter.api.Test
    void getExpire() {
        CacheData v4 = new CacheData("value5");
        String key = "key5";
        cache.setCacheObject(key, v4);
        cache.expire(key,3, TimeUnit.SECONDS);
        sleep(1);
        Assertions.assertEquals(2, cache.getExpire(key));

        print("getExpire 后");
    }

    @org.junit.jupiter.api.Test
    void hasKey() {
        CacheData v = new CacheData("value6");
        String key = "key6";
        cache.setCacheObject(key, v);
        Assertions.assertTrue(cache.hasKey(key));

        print("hasKey 后");
    }

    @org.junit.jupiter.api.Test
    void getCacheObject() {

        CacheData v = new CacheData("value7");
        String key = "key7";
        cache.setCacheObject(key, v);
        Assertions.assertEquals(v, cache.getCacheObject(key));

        print("getCacheObject 后");
    }

    @org.junit.jupiter.api.Test
    void deleteObject() {
        CacheData v = new CacheData("value8");
        String key = "key8";
        cache.setCacheObject(key, v);
        cache.deleteObject(key);
        Assertions.assertNull(cache.getCacheObject(key));

        print("deleteObject 后");
    }

    @org.junit.jupiter.api.Test
    void testDeleteObject() {
        Set<String> keys = Set.of("key9", "key10");
        for (String key : keys) {
            cache.setCacheObject(key, "value");
        }
        print("testDeleteObject 前");
        cache.deleteObject(keys);
        print("testDeleteObject 后");
    }

    @org.junit.jupiter.api.Test
    void setCacheList() {
        String key = "key11";
        List<String> list = List.of("v1", "v2", "v3", "v4");
        cache.setCacheList(key, list);
        List<String> v = cache.getCacheList(key);
        Assertions.assertEquals(list, v);
        print("setCacheList 后");
    }


    @org.junit.jupiter.api.Test
    void setCacheMap() {
        String key = "key12";
        Map<String,String> map = Map.of("k1", "v1", "k2", "v2", "k3", "v3", "k4", "v4");
        cache.setCacheMap(key, map);
        print("setCacheMap 前");
        Map<String,String> v = cache.getCacheMap(key);
        Assertions.assertEquals(map, v);
        print("setCacheMap 后");
    }


    @org.junit.jupiter.api.Test
    void setCacheMapValue() {
        String key = "key13";
        Map<String,String> map = new HashMap<>();
        map.put("k1", "v1");
        map.put("k2", "v2");
        map.put("k3", "v3");
        map.put("k4", "v4");

        cache.setCacheMap(key, map);
        print("setCacheMapValue 前");
        cache.setCacheMapValue(key,"k1", "v_new");
        Assertions.assertEquals("v_new", cache.getCacheMapValue(key,"k1"));
        print("setCacheMapValue 后");
    }


    @org.junit.jupiter.api.Test
    void getMultiCacheMapValue() {
        String key = "key14";
        Map<String,String> map = Map.of("k1", "v1", "k2", "v2", "k3", "v3", "k4", "v4");
        cache.setCacheMap(key, map);
        List<String> v = cache.getMultiCacheMapValue(key,List.of("k1","k2"));
        Assertions.assertEquals(List.of("v1","v2"), v);
        print("getMultiCacheMapValue 后");
    }

    @org.junit.jupiter.api.Test
    void deleteCacheMapValue() {
        String key = "key15";
        Map<String,String> map = new HashMap<>();
        map.put("k1", "v1");
        map.put("k2", "v2");
        map.put("k3", "v3");
        map.put("k4", "v4");

        cache.setCacheMap(key, map);
        print("deleteCacheMapValue 前");
        cache.deleteCacheMapValue(key,"k1");
        Assertions.assertNull(cache.getCacheMapValue(key,"k1"));
        print("setCacheMapValue 后");
    }

    @org.junit.jupiter.api.Test
    void keys() {
        cache.setCacheObject("key16", "value16");
        Collection<String> keys = cache.keys("key*");
        Assertions.assertFalse(keys.isEmpty());
        System.out.println(keys);
        print("keys 后");

    }

    @org.junit.jupiter.api.Test
    void testSize() {
        cache.setCacheObject("key17", "value17");
        Assertions.assertEquals(cache.getCaffeineCache().estimatedSize(), cache.getCaffeineCache().asMap().size());
        System.out.println(cache.getCaffeineCache().estimatedSize());
        print("testSize 后");
    }
}
