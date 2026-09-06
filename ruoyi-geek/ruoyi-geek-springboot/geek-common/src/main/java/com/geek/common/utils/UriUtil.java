package com.geek.common.utils;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class UriUtil {
    /**
     * 解析URI中的查询参数为键值对映射
     * 
     * @param uri 完整的URI对象
     * @return 解码后的参数映射，无查询参数返回空映射
     * @throws IllegalArgumentException 当URI格式无效时
     */
    public static Map<String, String> parseQueryParameters(URI uri) {
        if (uri == null) {
            throw new IllegalArgumentException("URI must not be null");
        }

        String query = uri.getRawQuery();
        if (query == null || query.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, String> params = new HashMap<>();
        try {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=", 2);
                String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8.name());
                String value = (keyValue.length > 1)
                        ? URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8.name())
                        : null;
                params.put(key, value);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid query string in URI: " + uri, e);
        }
        return Collections.unmodifiableMap(params);
    }

    /**
     * 解析URI中的路径参数值，遵循RFC 6570模板规范
     * 
     * @param uri      实际URI对象
     * @param template URI模板（如"/users/{id}/orders/{orderId}"）
     * @return 参数名到值的映射
     * @throws IllegalArgumentException 当URI与模板不匹配时
     */
    public static Map<String, String> parsePathParameters(URI uri, String template) {
        if (uri == null || template == null) {
            throw new IllegalArgumentException("URI and template must not be null");
        }

        String path = uri.getPath();
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("URI path must not be null or empty");
        }

        // 移除首尾的/
        String cleanUri = path.startsWith("/") ? path.substring(1) : path;
        String cleanTemplate = template.startsWith("/") ? template.substring(1) : template;

        String[] uriParts = cleanUri.split("/");
        String[] templateParts = cleanTemplate.split("/");

        if (uriParts.length != templateParts.length) {
            throw new IllegalArgumentException(
                    String.format("URI path length mismatch: expected %d segments, got %d",
                            templateParts.length, uriParts.length));
        }

        Map<String, String> params = new HashMap<>();
        for (int i = 0; i < templateParts.length; i++) {
            String templatePart = templateParts[i];
            String uriPart = uriParts[i];

            if (templatePart.startsWith("{") && templatePart.endsWith("}")) {
                String paramName = templatePart.substring(1, templatePart.length() - 1);
                params.put(paramName, uriPart);
            } else if (!templatePart.equals(uriPart)) {
                throw new IllegalArgumentException(
                        String.format("URI mismatch at segment %d: expected '%s', got '%s'",
                                i + 1, templatePart, uriPart));
            }
        }
        return Collections.unmodifiableMap(params);
    }

    /**
     * 安全获取集合中的最大值
     * 
     * @param set 整数集合
     * @return 最大值的Optional，空集合返回empty Optional
     */
    public static Optional<Integer> getMax(Set<Integer> set) {
        if (set == null || set.isEmpty()) {
            return Optional.empty();
        }
        return set.stream().max(Integer::compare);
    }

    public static int calculateMatchLevel(URI uri, String template) {
        if (uri == null || template == null) {
            throw new IllegalArgumentException("URI and template must not be null");
        }

        String path = uri.getPath();
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("URI path must not be null or empty");
        }

        // 清理路径首尾的/
        String cleanUri = path.startsWith("/") ? path.substring(1) : path;
        String cleanTemplate = template.startsWith("/") ? template.substring(1) : template;

        String[] uriParts = cleanUri.split("/");
        String[] templateParts = cleanTemplate.split("/");

        if (uriParts.length != templateParts.length) {
            return -1;
        }

        int matchLevel = 0;
        for (int i = 0; i < templateParts.length; i++) {
            String templatePart = templateParts[i];
            String uriPart = uriParts[i];

            // 参数段处理：使用标准{param}语法
            if (templatePart.startsWith("{") && templatePart.endsWith("}")) {
                matchLevel += (i + 1); // 参数段加分：段索引+1
            }
            // 固定路径段处理
            else if (!templatePart.equals(uriPart)) {
                return -1; // 固定路径段不匹配
            } else {
                matchLevel += templateParts.length + 1; // 固定路径段加分：模板总段数+1
            }
        }
        return matchLevel;
    }

    private UriUtil() {
    }
}
