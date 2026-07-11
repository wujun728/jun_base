package com.ruoyi.common.utils;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;

public class JSON {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ObjectNode parseObject(String text) {
        if (text == null || text.isEmpty()) {
            return objectMapper.createObjectNode();
        }
        try {
            return (ObjectNode) objectMapper.readTree(text);
        } catch (Exception e) {
            return null;
        }
    }

    public static String toJSONString(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return null;
        }
    }

    public static ObjectNode createObjectNode() {
        return objectMapper.createObjectNode();
    }
}