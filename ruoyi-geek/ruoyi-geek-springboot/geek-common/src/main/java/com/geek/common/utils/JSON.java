package com.geek.common.utils;

import java.util.Map;

import com.geek.common.utils.spring.SpringUtils;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ObjectNode;
import tools.jackson.databind.ser.FilterProvider;

public class JSON {

    private static JsonMapper JSON_MAPPER = null;

    private JSON() {
    }

    public static JsonMapper getJsonMapper() {
        if (JSON_MAPPER == null) {
            JSON_MAPPER = SpringUtils.getBean(JsonMapper.class);
        }
        return JSON_MAPPER;
    }

    public static String toJSONString(Object object) {
        return toJSONString(object, null);
    }

    public static String toJSONString(Object object, FilterProvider filterProvider) {
        try {
            if (filterProvider != null) {
                return getJsonMapper().writer(filterProvider).writeValueAsString(object);
            } else {
                return getJsonMapper().writeValueAsString(object);
            }
        } catch (Exception e) {
            throw new RuntimeException("对象转JSON字符串异常", e);
        }
    }

    public static JsonNode parseObject(String text) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        try {
            return getJsonMapper().readTree(text);
        } catch (Exception e) {
            throw new RuntimeException("JSON字符串转JsonNode异常", e);
        }
    }

    public static <T> T parseObject(String text, Class<T> clazz) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        try {
            return getJsonMapper().readValue(text, clazz);
        } catch (Exception e) {
            throw new RuntimeException("JSON字符串转对象异常", e);
        }
    }

    public static ObjectNode createObjectNode() {
        return getJsonMapper().createObjectNode();
    }

    public static ObjectNode createObjectNode(Map<String, ?> map) {
        return getJsonMapper().convertValue(map, ObjectNode.class);
    }
}
