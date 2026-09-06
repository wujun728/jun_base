package com.ruoyi.common.utils.ai.RAGFlow.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.utils.ai.RAGFlow.RAGFlow;
import com.ruoyi.common.utils.ai.RAGFlow.constant.RAGFlowConstants;
import com.ruoyi.common.utils.ai.RAGFlow.exception.RAGFlowException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * RAGFlow 工具类
 */
public class RAGFlowUtils {

    private RAGFlowUtils() {
        // 私有构造函数
    }

    /**
     * 检查API响应是否成功
     *
     * @param response API响应
     * @return 是否成功
     */
    public static boolean isSuccess(JSONObject response) {
        if (response == null) {
            return false;
        }
        Integer code = response.getInt("code");
        return code != null && code == RAGFlowConstants.CODE_SUCCESS;
    }

    /**
     * 从响应中获取数据
     *
     * @param response API响应
     * @param <T>      数据类型
     * @return 数据对象，失败返回null
     */
    public static <T> T getData(JSONObject response, Class<T> clazz) {
        if (!isSuccess(response)) {
            return null;
        }
        return JSONUtil.toBean(response.getJSONObject("data"), clazz);
    }

    /**
     * 从响应中获取数据列表
     *
     * @param response API响应
     * @param <T>      数据类型
     * @return 数据列表
     */
    public static <T> List<T> getDataList(JSONObject response, Class<T> clazz) {
        if (!isSuccess(response)) {
            return null;
        }
        return JSONUtil.toList(response.getJSONArray("data"), clazz);
    }

    /**
     * 从响应中获取数据，如果失败则抛出异常
     *
     * @param response API响应
     * @param <T>      数据类型
     * @return 数据对象
     * @throws RAGFlowException API调用失败
     */
    public static <T> T getDataOrThrow(JSONObject response, Class<T> clazz) {
        if (response == null) {
            throw new RAGFlowException("响应为空");
        }
        Integer code = response.getInt("code");
        String message = response.getStr("message");

        if (code == null || code != RAGFlowConstants.CODE_SUCCESS) {
            throw new RAGFlowException(code != null ? code : RAGFlowConstants.CODE_INTERNAL_ERROR,
                    message != null ? message : "未知错误");
        }

        return JSONUtil.toBean(response.getJSONObject("data"), clazz);
    }

    /**
     * 获取响应消息
     *
     * @param response API响应
     * @return 消息内容
     */
    public static String getMessage(JSONObject response) {
        if (response == null) {
            return null;
        }
        return response.getStr("message");
    }

    /**
     * 获取响应码
     *
     * @param response API响应
     * @return 响应码
     */
    public static Integer getCode(JSONObject response) {
        if (response == null) {
            return null;
        }
        return response.getInt("code");
    }

    /**
     * 获取响应总数
     *
     * @param response API响应
     * @return 总数
     */
    public static Integer getTotal(JSONObject response) {
        if (response == null) {
            return null;
        }
        return response.getInt("total");
    }

    /**
     * 构建分页参数
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @return 参数字符串
     */
    public static String buildPageParams(Integer page, Integer pageSize) {
        StringBuilder sb = new StringBuilder();
        if (page != null) {
            sb.append("page=").append(page).append("&");
        }
        if (pageSize != null) {
            sb.append("page_size=").append(pageSize).append("&");
        }
        return sb.toString().replaceAll("&$", "");
    }

    /**
     * 构建排序参数
     *
     * @param orderBy 排序字段
     * @param desc    是否降序
     * @return 参数字符串
     */
    public static String buildOrderParams(String orderBy, Boolean desc) {
        StringBuilder sb = new StringBuilder();
        if (orderBy != null) {
            sb.append("orderby=").append(orderBy).append("&");
        }
        if (desc != null) {
            sb.append("desc=").append(desc).append("&");
        }
        return sb.toString().replaceAll("&$", "");
    }

    /**
     * 构建列表转逗号分隔字符串
     *
     * @param list 字符串列表
     * @return 逗号分隔字符串
     */
    public static String joinList(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        return list.stream().filter(s -> s != null && !s.isEmpty())
                .collect(Collectors.joining(","));
    }

    /**
     * 构建元数据条件
     *
     * @param conditions 条件列表
     * @param logic      逻辑关系 (and/or)
     * @return 条件Map
     */
    public static Map<String, Object> buildMetadataCondition(
            List<Map<String, Object>> conditions, String logic) {
        Map<String, Object> condition = new java.util.HashMap<>();
        condition.put("logic", logic != null ? logic : RAGFlowConstants.LOGIC_AND);
        condition.put("conditions", conditions);
        return condition;
    }

    /**
     * 构建单个元数据条件
     *
     * @param name             字段名
     * @param comparisonOperator 比较运算符
     * @param value            值
     * @return 条件Map
     */
    public static Map<String, Object> buildCondition(String name, String comparisonOperator, Object value) {
        Map<String, Object> condition = new java.util.HashMap<>();
        condition.put("name", name);
        condition.put("comparison_operator", comparisonOperator);
        condition.put("value", value);
        return condition;
    }

    /**
     * 创建RAGFlow客户端
     *
     * @param baseUrl 服务器地址
     * @param apiKey  API密钥
     * @return RAGFlow实例
     */
    public static RAGFlow createClient(String baseUrl, String apiKey) {
        return new RAGFlow(baseUrl, apiKey);
    }

    /**
     * 创建RAGFlow客户端（带超时）
     *
     * @param baseUrl 服务器地址
     * @param apiKey  API密钥
     * @param timeout 超时时间（秒）
     * @return RAGFlow实例
     */
    public static RAGFlow createClient(String baseUrl, String apiKey, int timeout) {
        RAGFlow client = new RAGFlow(baseUrl, apiKey);
        client.setTimeout(timeout);
        return client;
    }
}
