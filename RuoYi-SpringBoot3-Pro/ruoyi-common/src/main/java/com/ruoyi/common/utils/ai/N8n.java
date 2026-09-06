package com.ruoyi.common.utils.ai;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

/**
 * N8n Webhook 工具类
 */
public class N8n {

    /**
     * 通用 Webhook 调用接口
     *
     * @param baseUrl   n8n 服务地址 (如: https://n8n.example.com)
     * @param webhookId Webhook ID
     * @param token     认证 Token (可选，如果配置了则添加到请求头 n-token)
     * @param payload   请求体 (JSON 对象)
     * @return 响应内容 (文本)
     */
    public static String webhook(String baseUrl, String webhookId, String token, JSONObject payload) {
        // 构建请求 URL
        String url = baseUrl + "/webhook/" + webhookId;

        // 构建请求
        HttpRequest request = HttpRequest.post(url)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(JSONUtil.toJsonStr(payload));

        // 如果配置了 token，添加到请求头
        if (StrUtil.isNotBlank(token)) {
            request.header("n-token", token);
        }

        // 发送请求
        try (HttpResponse response = request.execute()) {
            if (!response.isOk()) {
                throw new RuntimeException("请求失败: " + response.getStatus() + " " + response.body());
            }
            return response.body();
        }
    }

    /**
     * 带文件上传的 Webhook 调用接口
     *
     * @param baseUrl   n8n 服务地址
     * @param webhookId Webhook ID
     * @param token     认证 Token (可选)
     * @param payload   附加参数 (可选)
     * @param file      要上传的文件
     * @return 响应内容
     */
    public static String webhookWithFile(String baseUrl, String webhookId, String token, JSONObject payload, java.io.File file) {
        return webhook(baseUrl, webhookId, token, payload, java.util.Collections.singletonList(new Attachment("file", file, file.getName())));
    }

    /**
     * 通用 Webhook 调用接口 (支持多文件)
     *
     * @param baseUrl     n8n 服务地址
     * @param webhookId   Webhook ID
     * @param token       认证 Token (可选)
     * @param payload     附加参数 (可选)
     * @param attachments 附件列表
     * @return 响应内容
     */
    public static String webhook(String baseUrl, String webhookId, String token, JSONObject payload, java.util.List<Attachment> attachments) {
        String url = baseUrl + "/webhook/" + webhookId;

        HttpRequest request = HttpRequest.post(url);

        // 添加附件
        if (attachments != null) {
            for (Attachment attachment : attachments) {
                if (attachment.getFile() != null && attachment.getFile().exists()) {
                    // Hutool 的 form 方法支持传入 File 对象，并且可以指定文件名（如果 API 支持的话，Hutool 5.x 某些版本支持）
                    // 这里为了兼容性，如果指定了文件名，可以使用 InputStream
                    if (StrUtil.isNotBlank(attachment.getFileName()) && !attachment.getFileName().equals(attachment.getFile().getName())) {
                         request.form(attachment.getFieldName(), attachment.getFile(), attachment.getFileName());
                    } else {
                        request.form(attachment.getFieldName(), attachment.getFile());
                    }
                }
            }
        }

        if (payload != null && !payload.isEmpty()) {
            for (java.util.Map.Entry<String, Object> entry : payload.entrySet()) {
                request.form(entry.getKey(), entry.getValue());
            }
        }

        if (StrUtil.isNotBlank(token)) {
            request.header("n-token", token);
        }

        try (HttpResponse response = request.execute()) {
            if (!response.isOk()) {
                throw new RuntimeException("请求失败: " + response.getStatus() + " " + response.body());
            }
            return response.body();
        }
    }

    /**
     * 附件封装类
     */
    public static class Attachment {
        private String fieldName;
        private java.io.File file;
        private String fileName;

        public Attachment(String fieldName, java.io.File file) {
            this(fieldName, file, file.getName());
        }

        public Attachment(String fieldName, java.io.File file, String fileName) {
            this.fieldName = fieldName;
            this.file = file;
            this.fileName = fileName;
        }

        public String getFieldName() {
            return fieldName;
        }

        public java.io.File getFile() {
            return file;
        }

        public String getFileName() {
            return fileName;
        }
    }
}
