package com.ruoyi.common.utils.ai.RAGFlow;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.utils.ai.RAGFlow.stream.StreamEvent;
import com.ruoyi.common.utils.ai.RAGFlow.stream.StreamListener;
import com.ruoyi.common.utils.ai.RAGFlow.stream.StreamResponseHandler;
import com.ruoyi.common.utils.ai.RAGFlow.stream.StreamResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * RAGFlow API 客户端
 * 基于 http_api_reference.md 文档实现所有API接口
 *
 * @author RuoYi-SpringBoot3-Pro
 */
public class RAGFlow {

    private String baseUrl;
    private String apiKey;
    private int timeout = 30;

    /**
     * 创建 RAGFlow 客户端
     *
     * @param baseUrl RAGFlow 服务器地址，如 http://localhost:9222
     * @param apiKey  API密钥
     */
    public RAGFlow(String baseUrl, String apiKey) {
        this.baseUrl = baseUrl.replaceAll("/+$", "");
        this.apiKey = apiKey;
    }

    /**
     * 设置请求超时时间
     *
     * @param timeout 超时时间（秒）
     * @return this
     */
    public RAGFlow setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * 获取超时毫秒数
     */
    private int getTimeoutMs() {
        return timeout * 1000;
    }

    /**
     * 获取请求头
     */
    private Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + apiKey);
        headers.put("Content-Type", "application/json");
        return headers;
    }

    /**
     * 获取 multipart 请求头
     */
    private Map<String, String> getMultipartHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + apiKey);
        return headers;
    }

    /**
     * 解析响应结果
     */
    private JSONObject parseResponse(HttpResponse response) {
        String body = response.body();
        if (response.getStatus() == 200) {
            return JSONUtil.parseObj(body);
        }
        throw new RuntimeException("请求失败: " + response.getStatus() + " - " + body);
    }

    // ==================== OpenAI-Compatible API ====================

    /**
     * 创建聊天完成 (OpenAI兼容)
     *
     * @param chatId  聊天ID
     * @param model   模型名称
     * @param messages 消息列表
     * @param stream  是否流式输出
     * @return 响应结果
     */
    public JSONObject createChatCompletion(String chatId, String model, List<Map<String, String>> messages, boolean stream) {
        String url = baseUrl + "/api/v1/chats_openai/" + chatId + "/chat/completions";

        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("messages", messages);
        body.put("stream", stream);

        HttpRequest request = HttpRequest.post(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs())
                .body(JSONUtil.toJsonStr(body));

        return parseResponse(request.execute());
    }

    /**
     * 创建聊天完成 (OpenAI兼容) - 带额外参数
     *
     * @param chatId      聊天ID
     * @param model       模型名称
     * @param messages    消息列表
     * @param stream      是否流式输出
     * @param reference   是否包含引用
     * @param metadataCondition 元数据过滤条件
     * @return 响应结果
     */
    public JSONObject createChatCompletion(String chatId, String model, List<Map<String, String>> messages,
                                           boolean stream, Boolean reference, Map<String, Object> metadataCondition) {
        String url = baseUrl + "/api/v1/chats_openai/" + chatId + "/chat/completions";

        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("messages", messages);
        body.put("stream", stream);

        Map<String, Object> extraBody = new HashMap<>();
        if (reference != null) {
            extraBody.put("reference", reference);
        }
        if (metadataCondition != null) {
            extraBody.put("metadata_condition", metadataCondition);
        }
        if (!extraBody.isEmpty()) {
            body.put("extra_body", extraBody);
        }

        HttpRequest request = HttpRequest.post(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs())
                .body(JSONUtil.toJsonStr(body));

        return parseResponse(request.execute());
    }

    /**
     * 创建代理完成 (OpenAI兼容)
     *
     * @param agentId 代理ID
     * @param model   模型名称
     * @param messages 消息列表
     * @param stream  是否流式输出
     * @return 响应结果
     */
    public JSONObject createAgentCompletion(String agentId, String model, List<Map<String, String>> messages, boolean stream) {
        String url = baseUrl + "/api/v1/agents_openai/" + agentId + "/chat/completions";

        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("messages", messages);
        body.put("stream", stream);

        HttpRequest request = HttpRequest.post(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs())
                .body(JSONUtil.toJsonStr(body));

        return parseResponse(request.execute());
    }

    // ==================== Dataset Management ====================

    /**
     * 创建数据集
     *
     * @param name 数据集名称
     * @return 响应结果
     */
    public JSONObject createDataset(String name) {
        String url = baseUrl + "/api/v1/datasets";

        Map<String, Object> body = new HashMap<>();
        body.put("name", name);

        HttpRequest request = HttpRequest.post(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs())
                .body(JSONUtil.toJsonStr(body));

        return parseResponse(request.execute());
    }

    /**
     * 创建数据集 - 完整参数
     *
     * @param name           数据集名称
     * @param avatar         头像(Base64)
     * @param description    描述
     * @param embeddingModel 嵌入模型
     * @param permission     权限 me/team
     * @param chunkMethod    分块方法
     * @param parserConfig   解析配置
     * @return 响应结果
     */
    public JSONObject createDataset(String name, String avatar, String description, String embeddingModel,
                                    String permission, String chunkMethod, Map<String, Object> parserConfig) {
        String url = baseUrl + "/api/v1/datasets";

        Map<String, Object> body = new HashMap<>();
        body.put("name", name);
        if (avatar != null) body.put("avatar", avatar);
        if (description != null) body.put("description", description);
        if (embeddingModel != null) body.put("embedding_model", embeddingModel);
        if (permission != null) body.put("permission", permission);
        if (chunkMethod != null) body.put("chunk_method", chunkMethod);
        if (parserConfig != null) body.put("parser_config", parserConfig);

        HttpRequest request = HttpRequest.post(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs())
                .body(JSONUtil.toJsonStr(body));

        return parseResponse(request.execute());
    }

    /**
     * 删除数据集
     *
     * @param ids 数据集ID列表，null表示删除所有
     * @return 响应结果
     */
    public JSONObject deleteDatasets(List<String> ids) {
        String url = baseUrl + "/api/v1/datasets";

        Map<String, Object> body = new HashMap<>();
        body.put("ids", ids);

        HttpRequest request = HttpRequest.delete(url)
                .headerMap(getHeaders(), true)
                .body(JSONUtil.toJsonStr(body));

        return parseResponse(request.execute());
    }

    /**
     * 删除单个数据集
     *
     * @param datasetId 数据集ID
     * @return 响应结果
     */
    public JSONObject deleteDataset(String datasetId) {
        return deleteDatasets(List.of(datasetId));
    }

    /**
     * 更新数据集
     *
     * @param datasetId 数据集ID
     * @param params    更新参数
     * @return 响应结果
     */
    public JSONObject updateDataset(String datasetId, Map<String, Object> params) {
        String url = baseUrl + "/api/v1/datasets/" + datasetId;

        HttpRequest request = HttpRequest.put(url)
                .headerMap(getHeaders(), true)
                .body(JSONUtil.toJsonStr(params));

        return parseResponse(request.execute());
    }

    /**
     * 更新数据集名称
     *
     * @param datasetId 数据集ID
     * @param name      新名称
     * @return 响应结果
     */
    public JSONObject updateDatasetName(String datasetId, String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        return updateDataset(datasetId, params);
    }

    /**
     * 列出数据集
     *
     * @param page      页码
     * @param pageSize  每页数量
     * @param orderBy   排序字段
     * @param desc      是否降序
     * @param name      名称过滤
     * @param id        ID过滤
     * @return 响应结果
     */
    public JSONObject listDatasets(Integer page, Integer pageSize, String orderBy, Boolean desc, String name, String id) {
        StringBuilder url = new StringBuilder(baseUrl + "/api/v1/datasets?");

        if (page != null) url.append("page=").append(page).append("&");
        if (pageSize != null) url.append("page_size=").append(pageSize).append("&");
        if (orderBy != null) url.append("orderby=").append(orderBy).append("&");
        if (desc != null) url.append("desc=").append(desc).append("&");
        if (name != null) url.append("name=").append(name).append("&");
        if (id != null) url.append("id=").append(id).append("&");

        String urlStr = url.toString().replaceAll("&$", "");

        HttpRequest request = HttpRequest.get(urlStr)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs());

        return parseResponse(request.execute());
    }

    /**
     * 列出数据集 - 简化版
     *
     * @return 响应结果
     */
    public JSONObject listDatasets() {
        return listDatasets(1, 30, "create_time", true, null, null);
    }

    /**
     * 获取知识图谱
     *
     * @param datasetId 数据集ID
     * @return 响应结果
     */
    public JSONObject getKnowledgeGraph(String datasetId) {
        String url = baseUrl + "/api/v1/datasets/" + datasetId + "/knowledge_graph";

        HttpRequest request = HttpRequest.get(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs());

        return parseResponse(request.execute());
    }

    /**
     * 删除知识图谱
     *
     * @param datasetId 数据集ID
     * @return 响应结果
     */
    public JSONObject deleteKnowledgeGraph(String datasetId) {
        String url = baseUrl + "/api/v1/datasets/" + datasetId + "/knowledge_graph";

        HttpRequest request = HttpRequest.delete(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs());

        return parseResponse(request.execute());
    }

    /**
     * 构建知识图谱
     *
     * @param datasetId 数据集ID
     * @return 响应结果
     */
    public JSONObject constructKnowledgeGraph(String datasetId) {
        String url = baseUrl + "/api/v1/datasets/" + datasetId + "/run_graphrag";

        HttpRequest request = HttpRequest.post(url)
                .headerMap(getHeaders(), true);

        return parseResponse(request.execute());
    }

    /**
     * 获取知识图谱构建状态
     *
     * @param datasetId 数据集ID
     * @return 响应结果
     */
    public JSONObject getKnowledgeGraphStatus(String datasetId) {
        String url = baseUrl + "/api/v1/datasets/" + datasetId + "/trace_graphrag";

        HttpRequest request = HttpRequest.get(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs());

        return parseResponse(request.execute());
    }

    /**
     * 构建RAPTOR
     *
     * @param datasetId 数据集ID
     * @return 响应结果
     */
    public JSONObject constructRaptor(String datasetId) {
        String url = baseUrl + "/api/v1/datasets/" + datasetId + "/run_raptor";

        HttpRequest request = HttpRequest.post(url)
                .headerMap(getHeaders(), true);

        return parseResponse(request.execute());
    }

    /**
     * 获取RAPTOR构建状态
     *
     * @param datasetId 数据集ID
     * @return 响应结果
     */
    public JSONObject getRaptorStatus(String datasetId) {
        String url = baseUrl + "/api/v1/datasets/" + datasetId + "/trace_raptor";

        HttpRequest request = HttpRequest.get(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs());

        return parseResponse(request.execute());
    }

    // ==================== File Management Within Dataset ====================

    /**
     * 上传文档到数据集
     *
     * @param datasetId 数据集ID
     * @param files     文件列表
     * @return 响应结果
     */
    public JSONObject uploadDocuments(String datasetId, List<File> files) {
        String url = baseUrl + "/api/v1/datasets/" + datasetId + "/documents";

        HttpRequest request = HttpRequest.post(url)
                .headerMap(getMultipartHeaders(), false)
                .timeout(getTimeoutMs());

        for (File file : files) {
            request.form("file", file);
        }

        return parseResponse(request.execute());
    }

    /**
     * 上传文档到数据集 - 使用InputStream
     *
     * @param datasetId   数据集ID
     * @param fileNames   文件名列表
     * @param inputStreams 文件输入流列表
     * @return 响应结果
     */
    public JSONObject uploadDocuments(String datasetId, List<String> fileNames, List<InputStream> inputStreams) {
        String url = baseUrl + "/api/v1/datasets/" + datasetId + "/documents";

        HttpRequest request = HttpRequest.post(url)
                .headerMap(getMultipartHeaders(), false)
                .timeout(getTimeoutMs());

        for (int i = 0; i < fileNames.size(); i++) {
            request.form("file", inputStreams.get(i), fileNames.get(i));
        }

        return parseResponse(request.execute());
    }

    /**
     * 更新文档
     *
     * @param datasetId  数据集ID
     * @param documentId 文档ID
     * @param params     更新参数
     * @return 响应结果
     */
    public JSONObject updateDocument(String datasetId, String documentId, Map<String, Object> params) {
        String url = baseUrl + "/api/v1/datasets/" + datasetId + "/documents/" + documentId;

        HttpRequest request = HttpRequest.put(url)
                .headerMap(getHeaders(), true)
                .body(JSONUtil.toJsonStr(params));

        return parseResponse(request.execute());
    }

    /**
     * 更新文档名称
     *
     * @param datasetId  数据集ID
     * @param documentId 文档ID
     * @param name       新名称
     * @return 响应结果
     */
    public JSONObject updateDocumentName(String datasetId, String documentId, String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        return updateDocument(datasetId, documentId, params);
    }

    /**
     * 下载文档
     *
     * @param datasetId  数据集ID
     * @param documentId 文档ID
     * @param outputPath 输出文件路径
     * @return 是否成功
     */
    public boolean downloadDocument(String datasetId, String documentId, String outputPath) {
        String url = baseUrl + "/api/v1/datasets/" + datasetId + "/documents/" + documentId;

        HttpRequest request = HttpRequest.get(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs())
                .setFollowRedirects(true);

        try (HttpResponse response = request.execute()) {
            if (response.getStatus() == 200) {
                java.nio.file.Files.copy(
                    response.bodyStream(),
                    java.nio.file.Paths.get(outputPath),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING
                );
                return true;
            }
            return false;
        } catch (IOException e) {
            throw new RuntimeException("下载文档失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取文档内容
     *
     * @param datasetId  数据集ID
     * @param documentId 文档ID
     * @return 文档内容
     */
    public String getDocumentContent(String datasetId, String documentId) {
        String url = baseUrl + "/api/v1/datasets/" + datasetId + "/documents/" + documentId;

        HttpRequest request = HttpRequest.get(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs());

        try (HttpResponse response = request.execute()) {
            if (response.getStatus() == 200) {
                return response.body();
            }
            throw new RuntimeException("获取文档失败: " + response.getStatus());
        }
    }

    /**
     * 列出文档
     *
     * @param datasetId 数据集ID
     * @param params    查询参数
     * @return 响应结果
     */
    public JSONObject listDocuments(String datasetId, Map<String, Object> params) {
        StringBuilder url = new StringBuilder(baseUrl + "/api/v1/datasets/" + datasetId + "/documents?");

        if (params != null) {
            params.forEach((key, value) -> url.append(key).append("=").append(value).append("&"));
        }

        String urlStr = url.toString().replaceAll("&$", "");

        HttpRequest request = HttpRequest.get(urlStr)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs());

        return parseResponse(request.execute());
    }

    /**
     * 列出文档 - 简化版
     *
     * @param datasetId 数据集ID
     * @param page      页码
     * @param pageSize  每页数量
     * @return 响应结果
     */
    public JSONObject listDocuments(String datasetId, Integer page, Integer pageSize) {
        Map<String, Object> params = new HashMap<>();
        if (page != null) params.put("page", page);
        if (pageSize != null) params.put("page_size", pageSize);
        return listDocuments(datasetId, params);
    }

    /**
     * 删除文档
     *
     * @param datasetId 数据集ID
     * @param documentIds 文档ID列表，null表示删除所有
     * @return 响应结果
     */
    public JSONObject deleteDocuments(String datasetId, List<String> documentIds) {
        String url = baseUrl + "/api/v1/datasets/" + datasetId + "/documents";

        Map<String, Object> body = new HashMap<>();
        body.put("ids", documentIds);

        HttpRequest request = HttpRequest.delete(url)
                .headerMap(getHeaders(), true)
                .body(JSONUtil.toJsonStr(body));

        return parseResponse(request.execute());
    }

    /**
     * 删除单个文档
     *
     * @param datasetId  数据集ID
     * @param documentId 文档ID
     * @return 响应结果
     */
    public JSONObject deleteDocument(String datasetId, String documentId) {
        return deleteDocuments(datasetId, List.of(documentId));
    }

    /**
     * 解析文档
     *
     * @param datasetId   数据集ID
     * @param documentIds 文档ID列表
     * @return 响应结果
     */
    public JSONObject parseDocuments(String datasetId, List<String> documentIds) {
        String url = baseUrl + "/api/v1/datasets/" + datasetId + "/chunks";

        Map<String, Object> body = new HashMap<>();
        body.put("document_ids", documentIds);

        HttpRequest request = HttpRequest.post(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs())
                .body(JSONUtil.toJsonStr(body));

        return parseResponse(request.execute());
    }

    /**
     * 停止解析文档
     *
     * @param datasetId   数据集ID
     * @param documentIds 文档ID列表
     * @return 响应结果
     */
    public JSONObject stopParsingDocuments(String datasetId, List<String> documentIds) {
        String url = baseUrl + "/api/v1/datasets/" + datasetId + "/chunks";

        Map<String, Object> body = new HashMap<>();
        body.put("document_ids", documentIds);

        HttpRequest request = HttpRequest.delete(url)
                .headerMap(getHeaders(), true)
                .body(JSONUtil.toJsonStr(body));

        return parseResponse(request.execute());
    }

    // ==================== Chunk Management ====================

    /**
     * 添加块
     *
     * @param datasetId  数据集ID
     * @param documentId 文档ID
     * @param content    块内容
     * @return 响应结果
     */
    public JSONObject addChunk(String datasetId, String documentId, String content) {
        return addChunk(datasetId, documentId, content, new ArrayList<>(), new ArrayList<>());
    }

    /**
     * 添加块 - 完整参数
     *
     * @param datasetId         数据集ID
     * @param documentId        文档ID
     * @param content           块内容
     * @param importantKeywords 重要关键词
     * @param questions         相关问题
     * @return 响应结果
     */
    public JSONObject addChunk(String datasetId, String documentId, String content,
                               List<String> importantKeywords, List<String> questions) {
        String url = baseUrl + "/api/v1/datasets/" + datasetId + "/documents/" + documentId + "/chunks";

        Map<String, Object> body = new HashMap<>();
        body.put("content", content);
        if (!importantKeywords.isEmpty()) body.put("important_keywords", importantKeywords);
        if (!questions.isEmpty()) body.put("questions", questions);

        HttpRequest request = HttpRequest.post(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs())
                .body(JSONUtil.toJsonStr(body));

        return parseResponse(request.execute());
    }

    /**
     * 列出块
     *
     * @param datasetId  数据集ID
     * @param documentId 文档ID
     * @param params     查询参数
     * @return 响应结果
     */
    public JSONObject listChunks(String datasetId, String documentId, Map<String, Object> params) {
        StringBuilder url = new StringBuilder(baseUrl + "/api/v1/datasets/" + datasetId
                + "/documents/" + documentId + "/chunks?");

        if (params != null) {
            params.forEach((key, value) -> url.append(key).append("=").append(value).append("&"));
        }

        String urlStr = url.toString().replaceAll("&$", "");

        HttpRequest request = HttpRequest.get(urlStr)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs());

        return parseResponse(request.execute());
    }

    /**
     * 列出块 - 简化版
     *
     * @param datasetId  数据集ID
     * @param documentId 文档ID
     * @param page       页码
     * @param pageSize   每页数量
     * @param keywords   关键词过滤
     * @return 响应结果
     */
    public JSONObject listChunks(String datasetId, String documentId, Integer page, Integer pageSize, String keywords) {
        Map<String, Object> params = new HashMap<>();
        if (page != null) params.put("page", page);
        if (pageSize != null) params.put("page_size", pageSize);
        if (keywords != null) params.put("keywords", keywords);
        return listChunks(datasetId, documentId, params);
    }

    /**
     * 删除块
     *
     * @param datasetId  数据集ID
     * @param documentId 文档ID
     * @param chunkIds   块ID列表，null表示删除所有
     * @return 响应结果
     */
    public JSONObject deleteChunks(String datasetId, String documentId, List<String> chunkIds) {
        String url = baseUrl + "/api/v1/datasets/" + datasetId + "/documents/" + documentId + "/chunks";

        Map<String, Object> body = new HashMap<>();
        body.put("chunk_ids", chunkIds);

        HttpRequest request = HttpRequest.delete(url)
                .headerMap(getHeaders(), true)
                .body(JSONUtil.toJsonStr(body));

        return parseResponse(request.execute());
    }

    /**
     * 删除单个块
     *
     * @param datasetId  数据集ID
     * @param documentId 文档ID
     * @param chunkId    块ID
     * @return 响应结果
     */
    public JSONObject deleteChunk(String datasetId, String documentId, String chunkId) {
        return deleteChunks(datasetId, documentId, List.of(chunkId));
    }

    /**
     * 更新块
     *
     * @param datasetId  数据集ID
     * @param documentId 文档ID
     * @param chunkId    块ID
     * @param params     更新参数
     * @return 响应结果
     */
    public JSONObject updateChunk(String datasetId, String documentId, String chunkId, Map<String, Object> params) {
        String url = baseUrl + "/api/v1/datasets/" + datasetId + "/documents/" + documentId + "/chunks/" + chunkId;

        HttpRequest request = HttpRequest.put(url)
                .headerMap(getHeaders(), true)
                .body(JSONUtil.toJsonStr(params));

        return parseResponse(request.execute());
    }

    /**
     * 更新块内容
     *
     * @param datasetId  数据集ID
     * @param documentId 文档ID
     * @param chunkId    块ID
     * @param content    新内容
     * @return 响应结果
     */
    public JSONObject updateChunkContent(String datasetId, String documentId, String chunkId, String content) {
        Map<String, Object> params = new HashMap<>();
        params.put("content", content);
        return updateChunk(datasetId, documentId, chunkId, params);
    }

    /**
     * 获取数据集元数据摘要
     *
     * @param datasetId 数据集ID
     * @return 响应结果
     */
    public JSONObject getMetadataSummary(String datasetId) {
        String url = baseUrl + "/api/v1/datasets/" + datasetId + "/metadata/summary";

        HttpRequest request = HttpRequest.get(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs());

        return parseResponse(request.execute());
    }

    /**
     * 更新或删除元数据
     *
     * @param datasetId 数据集ID
     * @param selector  选择器
     * @param updates   更新列表
     * @param deletes   删除列表
     * @return 响应结果
     */
    public JSONObject updateOrDeleteMetadata(String datasetId, Map<String, Object> selector,
                                              List<Map<String, String>> updates, List<Map<String, String>> deletes) {
        String url = baseUrl + "/api/v1/datasets/" + datasetId + "/metadata/update";

        Map<String, Object> body = new HashMap<>();
        if (selector != null) body.put("selector", selector);
        if (updates != null) body.put("updates", updates);
        if (deletes != null) body.put("deletes", deletes);

        HttpRequest request = HttpRequest.post(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs())
                .body(JSONUtil.toJsonStr(body));

        return parseResponse(request.execute());
    }

    // ==================== Retrieval ====================

    /**
     * 检索块
     *
     * @param question  问题
     * @param datasetIds 数据集ID列表
     * @return 响应结果
     */
    public JSONObject retrieveChunks(String question, List<String> datasetIds) {
        String url = baseUrl + "/api/v1/retrieval";

        Map<String, Object> body = new HashMap<>();
        body.put("question", question);
        body.put("dataset_ids", datasetIds);

        HttpRequest request = HttpRequest.post(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs())
                .body(JSONUtil.toJsonStr(body));

        return parseResponse(request.execute());
    }

    /**
     * 检索块 - 完整参数
     *
     * @param question              问题
     * @param datasetIds            数据集ID列表
     * @param documentIds           文档ID列表
     * @param page                  页码
     * @param pageSize              每页数量
     * @param similarityThreshold   相似度阈值
     * @param vectorSimilarityWeight 向量相似度权重
     * @param topK                  TopK
     * @param keyword               是否使用关键词
     * @param highlight             是否高亮
     * @param metadataCondition     元数据条件
     * @return 响应结果
     */
    public JSONObject retrieveChunks(String question, List<String> datasetIds, List<String> documentIds,
                                      Integer page, Integer pageSize, Float similarityThreshold,
                                      Float vectorSimilarityWeight, Integer topK, Boolean keyword,
                                      Boolean highlight, Map<String, Object> metadataCondition) {
        String url = baseUrl + "/api/v1/retrieval";

        Map<String, Object> body = new HashMap<>();
        body.put("question", question);
        if (datasetIds != null) body.put("dataset_ids", datasetIds);
        if (documentIds != null) body.put("document_ids", documentIds);
        if (page != null) body.put("page", page);
        if (pageSize != null) body.put("page_size", pageSize);
        if (similarityThreshold != null) body.put("similarity_threshold", similarityThreshold);
        if (vectorSimilarityWeight != null) body.put("vector_similarity_weight", vectorSimilarityWeight);
        if (topK != null) body.put("top_k", topK);
        if (keyword != null) body.put("keyword", keyword);
        if (highlight != null) body.put("highlight", highlight);
        if (metadataCondition != null) body.put("metadata_condition", metadataCondition);

        HttpRequest request = HttpRequest.post(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs())
                .body(JSONUtil.toJsonStr(body));

        return parseResponse(request.execute());
    }

    // ==================== Chat Assistant Management ====================

    /**
     * 创建聊天助手
     *
     * @param name       名称
     * @param datasetIds 关联数据集ID列表
     * @return 响应结果
     */
    public JSONObject createChatAssistant(String name, List<String> datasetIds) {
        String url = baseUrl + "/api/v1/chats";

        Map<String, Object> body = new HashMap<>();
        body.put("name", name);
        body.put("dataset_ids", datasetIds);

        HttpRequest request = HttpRequest.post(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs())
                .body(JSONUtil.toJsonStr(body));

        return parseResponse(request.execute());
    }

    /**
     * 创建聊天助手 - 完整参数
     *
     * @param name       名称
     * @param datasetIds 关联数据集ID列表
     * @param avatar     头像
     * @param llm        LLM配置
     * @param prompt     提示配置
     * @return 响应结果
     */
    public JSONObject createChatAssistant(String name, List<String> datasetIds, String avatar,
                                           Map<String, Object> llm, Map<String, Object> prompt) {
        String url = baseUrl + "/api/v1/chats";

        Map<String, Object> body = new HashMap<>();
        body.put("name", name);
        body.put("dataset_ids", datasetIds);
        if (avatar != null) body.put("avatar", avatar);
        if (llm != null) body.put("llm", llm);
        if (prompt != null) body.put("prompt", prompt);

        HttpRequest request = HttpRequest.post(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs())
                .body(JSONUtil.toJsonStr(body));

        return parseResponse(request.execute());
    }

    /**
     * 更新聊天助手
     *
     * @param chatId 聊天ID
     * @param params 更新参数
     * @return 响应结果
     */
    public JSONObject updateChatAssistant(String chatId, Map<String, Object> params) {
        String url = baseUrl + "/api/v1/chats/" + chatId;

        HttpRequest request = HttpRequest.put(url)
                .headerMap(getHeaders(), true)
                .body(JSONUtil.toJsonStr(params));

        return parseResponse(request.execute());
    }

    /**
     * 更新聊天助手名称
     *
     * @param chatId 聊天ID
     * @param name   新名称
     * @return 响应结果
     */
    public JSONObject updateChatAssistantName(String chatId, String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        return updateChatAssistant(chatId, params);
    }

    /**
     * 删除聊天助手
     *
     * @param ids 聊天ID列表，null表示删除所有
     * @return 响应结果
     */
    public JSONObject deleteChatAssistants(List<String> ids) {
        String url = baseUrl + "/api/v1/chats";

        Map<String, Object> body = new HashMap<>();
        body.put("ids", ids);

        HttpRequest request = HttpRequest.delete(url)
                .headerMap(getHeaders(), true)
                .body(JSONUtil.toJsonStr(body));

        return parseResponse(request.execute());
    }

    /**
     * 删除单个聊天助手
     *
     * @param chatId 聊天ID
     * @return 响应结果
     */
    public JSONObject deleteChatAssistant(String chatId) {
        return deleteChatAssistants(List.of(chatId));
    }

    /**
     * 列出聊天助手
     *
     * @param params 查询参数
     * @return 响应结果
     */
    public JSONObject listChatAssistants(Map<String, Object> params) {
        StringBuilder url = new StringBuilder(baseUrl + "/api/v1/chats?");

        if (params != null) {
            params.forEach((key, value) -> url.append(key).append("=").append(value).append("&"));
        }

        String urlStr = url.toString().replaceAll("&$", "");

        HttpRequest request = HttpRequest.get(urlStr)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs());

        return parseResponse(request.execute());
    }

    /**
     * 列出聊天助手 - 简化版
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @return 响应结果
     */
    public JSONObject listChatAssistants(Integer page, Integer pageSize) {
        Map<String, Object> params = new HashMap<>();
        if (page != null) params.put("page", page);
        if (pageSize != null) params.put("page_size", pageSize);
        return listChatAssistants(params);
    }

    // ==================== Session Management ====================

    /**
     * 创建聊天会话
     *
     * @param chatId 聊天ID
     * @param name   会话名称
     * @return 响应结果
     */
    public JSONObject createSession(String chatId, String name) {
        return createSession(chatId, name, null);
    }

    /**
     * 创建聊天会话 - 带用户ID
     *
     * @param chatId  聊天ID
     * @param name    会话名称
     * @param userId  用户ID
     * @return 响应结果
     */
    public JSONObject createSession(String chatId, String name, String userId) {
        String url = baseUrl + "/api/v1/chats/" + chatId + "/sessions";

        Map<String, Object> body = new HashMap<>();
        body.put("name", name);
        if (userId != null) body.put("user_id", userId);

        HttpRequest request = HttpRequest.post(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs())
                .body(JSONUtil.toJsonStr(body));

        return parseResponse(request.execute());
    }

    /**
     * 更新会话
     *
     * @param chatId    聊天ID
     * @param sessionId 会话ID
     * @param name      新名称
     * @param userId    用户ID
     * @return 响应结果
     */
    public JSONObject updateSession(String chatId, String sessionId, String name, String userId) {
        String url = baseUrl + "/api/v1/chats/" + chatId + "/sessions/" + sessionId;

        Map<String, Object> body = new HashMap<>();
        body.put("name", name);
        if (userId != null) body.put("user_id", userId);

        HttpRequest request = HttpRequest.put(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs())
                .body(JSONUtil.toJsonStr(body));

        return parseResponse(request.execute());
    }

    /**
     * 列出聊天会话
     *
     * @param chatId 聊天ID
     * @param params 查询参数
     * @return 响应结果
     */
    public JSONObject listChatSessions(String chatId, Map<String, Object> params) {
        StringBuilder url = new StringBuilder(baseUrl + "/api/v1/chats/" + chatId + "/sessions?");

        if (params != null) {
            params.forEach((key, value) -> url.append(key).append("=").append(value).append("&"));
        }

        String urlStr = url.toString().replaceAll("&$", "");

        HttpRequest request = HttpRequest.get(urlStr)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs());

        return parseResponse(request.execute());
    }

    /**
     * 列出聊天会话 - 简化版
     *
     * @param chatId  聊天ID
     * @param page    页码
     * @param pageSize 每页数量
     * @return 响应结果
     */
    public JSONObject listChatSessions(String chatId, Integer page, Integer pageSize) {
        Map<String, Object> params = new HashMap<>();
        if (page != null) params.put("page", page);
        if (pageSize != null) params.put("page_size", pageSize);
        return listChatSessions(chatId, params);
    }

    /**
     * 删除聊天会话
     *
     * @param chatId 聊天ID
     * @param ids    会话ID列表，null表示删除所有
     * @return 响应结果
     */
    public JSONObject deleteChatSessions(String chatId, List<String> ids) {
        String url = baseUrl + "/api/v1/chats/" + chatId + "/sessions";

        Map<String, Object> body = new HashMap<>();
        body.put("ids", ids);

        HttpRequest request = HttpRequest.delete(url)
                .headerMap(getHeaders(), true)
                .body(JSONUtil.toJsonStr(body));

        return parseResponse(request.execute());
    }

    /**
     * 删除单个聊天会话
     *
     * @param chatId    聊天ID
     * @param sessionId 会话ID
     * @return 响应结果
     */
    public JSONObject deleteChatSession(String chatId, String sessionId) {
        return deleteChatSessions(chatId, List.of(sessionId));
    }

    /**
     * 与聊天助手对话
     *
     * @param chatId    聊天ID
     * @param question  问题
     * @param stream    是否流式输出
     * @param sessionId 会话ID
     * @return 响应结果
     */
    public JSONObject converseWithChatAssistant(String chatId, String question, boolean stream, String sessionId) {
        return converseWithChatAssistant(chatId, question, stream, sessionId, null, null);
    }

    /**
     * 与聊天助手对话 - 完整参数
     *
     * @param chatId            聊天ID
     * @param question          问题
     * @param stream            是否流式输出
     * @param sessionId         会话ID
     * @param userId            用户ID
     * @param metadataCondition 元数据条件
     * @return 响应结果
     */
    public JSONObject converseWithChatAssistant(String chatId, String question, boolean stream,
                                                 String sessionId, String userId, Map<String, Object> metadataCondition) {
        String url = baseUrl + "/api/v1/chats/" + chatId + "/completions";

        Map<String, Object> body = new HashMap<>();
        body.put("question", question);
        body.put("stream", stream);
        if (sessionId != null) body.put("session_id", sessionId);
        if (userId != null) body.put("user_id", userId);
        if (metadataCondition != null) body.put("metadata_condition", metadataCondition);

        HttpRequest request = HttpRequest.post(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs())
                .body(JSONUtil.toJsonStr(body));

        return parseResponse(request.execute());
    }

    // ==================== Agent Management ====================

    /**
     * 创建代理会话
     *
     * @param agentId 代理ID
     * @return 响应结果
     */
    public JSONObject createAgentSession(String agentId) {
        return createAgentSession(agentId, null);
    }

    /**
     * 创建代理会话 - 带用户ID
     *
     * @param agentId 代理ID
     * @param userId  用户ID
     * @return 响应结果
     */
    public JSONObject createAgentSession(String agentId, String userId) {
        StringBuilder url = new StringBuilder(baseUrl + "/api/v1/agents/" + agentId + "/sessions");
        if (userId != null) {
            url.append("?user_id=").append(userId);
        }

        HttpRequest request = HttpRequest.post(url.toString())
                .headerMap(getHeaders(), true)
                .body("{}");

        return parseResponse(request.execute());
    }

    /**
     * 与代理对话
     *
     * @param agentId 代理ID
     * @param question 问题
     * @param stream   是否流式输出
     * @return 响应结果
     */
    public JSONObject converseWithAgent(String agentId, String question, boolean stream) {
        return converseWithAgent(agentId, question, stream, null, null, null, null);
    }

    /**
     * 与代理对话 - 完整参数
     *
     * @param agentId      代理ID
     * @param question     问题
     * @param stream       是否流式输出
     * @param sessionId    会话ID
     * @param inputs       输入参数
     * @param userId       用户ID
     * @param returnTrace  是否返回追踪信息
     * @return 响应结果
     */
    public JSONObject converseWithAgent(String agentId, String question, boolean stream,
                                         String sessionId, Map<String, Object> inputs,
                                         String userId, Boolean returnTrace) {
        String url = baseUrl + "/api/v1/agents/" + agentId + "/completions";

        Map<String, Object> body = new HashMap<>();
        body.put("question", question);
        body.put("stream", stream);
        if (sessionId != null) body.put("session_id", sessionId);
        if (inputs != null) body.put("inputs", inputs);
        if (userId != null) body.put("user_id", userId);
        if (returnTrace != null) body.put("return_trace", returnTrace);

        HttpRequest request = HttpRequest.post(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs())
                .body(JSONUtil.toJsonStr(body));

        return parseResponse(request.execute());
    }

    /**
     * 列出代理会话
     *
     * @param agentId 代理ID
     * @param params  查询参数
     * @return 响应结果
     */
    public JSONObject listAgentSessions(String agentId, Map<String, Object> params) {
        StringBuilder url = new StringBuilder(baseUrl + "/api/v1/agents/" + agentId + "/sessions?");

        if (params != null) {
            params.forEach((key, value) -> url.append(key).append("=").append(value).append("&"));
        }

        String urlStr = url.toString().replaceAll("&$", "");

        HttpRequest request = HttpRequest.get(urlStr)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs());

        return parseResponse(request.execute());
    }

    /**
     * 列出代理会话 - 简化版
     *
     * @param agentId 代理ID
     * @param page    页码
     * @param pageSize 每页数量
     * @return 响应结果
     */
    public JSONObject listAgentSessions(String agentId, Integer page, Integer pageSize) {
        Map<String, Object> params = new HashMap<>();
        if (page != null) params.put("page", page);
        if (pageSize != null) params.put("page_size", pageSize);
        return listAgentSessions(agentId, params);
    }

    /**
     * 删除代理会话
     *
     * @param agentId 代理ID
     * @param ids     会话ID列表，null表示删除所有
     * @return 响应结果
     */
    public JSONObject deleteAgentSessions(String agentId, List<String> ids) {
        String url = baseUrl + "/api/v1/agents/" + agentId + "/sessions";

        Map<String, Object> body = new HashMap<>();
        body.put("ids", ids);

        HttpRequest request = HttpRequest.delete(url)
                .headerMap(getHeaders(), true)
                .body(JSONUtil.toJsonStr(body));

        return parseResponse(request.execute());
    }

    /**
     * 生成相关问题
     *
     * @param question 原问题
     * @param industry 行业
     * @param loginToken 登录令牌
     * @return 响应结果
     */
    public JSONObject generateRelatedQuestions(String question, String industry, String loginToken) {
        String url = baseUrl + "/api/v1/sessions/related_questions";

        Map<String, Object> body = new HashMap<>();
        body.put("question", question);
        if (industry != null) body.put("industry", industry);

        Map<String, String> headers = getHeaders();
        headers.put("Authorization", "Bearer " + loginToken);

        HttpRequest request = HttpRequest.post(url)
                .headerMap(headers, true)
                .body(JSONUtil.toJsonStr(body));

        return parseResponse(request.execute());
    }

    /**
     * 列出代理
     *
     * @param params 查询参数
     * @return 响应结果
     */
    public JSONObject listAgents(Map<String, Object> params) {
        StringBuilder url = new StringBuilder(baseUrl + "/api/v1/agents?");

        if (params != null) {
            params.forEach((key, value) -> url.append(key).append("=").append(value).append("&"));
        }

        String urlStr = url.toString().replaceAll("&$", "");

        HttpRequest request = HttpRequest.get(urlStr)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs());

        return parseResponse(request.execute());
    }

    /**
     * 列出代理 - 简化版
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @return 响应结果
     */
    public JSONObject listAgents(Integer page, Integer pageSize) {
        Map<String, Object> params = new HashMap<>();
        if (page != null) params.put("page", page);
        if (pageSize != null) params.put("page_size", pageSize);
        return listAgents(params);
    }

    /**
     * 创建代理
     *
     * @param title       标题
     * @param description 描述
     * @param dsl         DSL配置
     * @return 响应结果
     */
    public JSONObject createAgent(String title, String description, Map<String, Object> dsl) {
        String url = baseUrl + "/api/v1/agents";

        Map<String, Object> body = new HashMap<>();
        body.put("title", title);
        if (description != null) body.put("description", description);
        if (dsl != null) body.put("dsl", dsl);

        HttpRequest request = HttpRequest.post(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs())
                .body(JSONUtil.toJsonStr(body));

        return parseResponse(request.execute());
    }

    /**
     * 更新代理
     *
     * @param agentId   代理ID
     * @param title     标题
     * @param description 描述
     * @param dsl       DSL配置
     * @return 响应结果
     */
    public JSONObject updateAgent(String agentId, String title, String description, Map<String, Object> dsl) {
        String url = baseUrl + "/api/v1/agents/" + agentId;

        Map<String, Object> body = new HashMap<>();
        if (title != null) body.put("title", title);
        if (description != null) body.put("description", description);
        if (dsl != null) body.put("dsl", dsl);

        HttpRequest request = HttpRequest.put(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs())
                .body(JSONUtil.toJsonStr(body));

        return parseResponse(request.execute());
    }

    /**
     * 删除代理
     *
     * @param agentId 代理ID
     * @return 响应结果
     */
    public JSONObject deleteAgent(String agentId) {
        String url = baseUrl + "/api/v1/agents/" + agentId;

        HttpRequest request = HttpRequest.delete(url)
                .headerMap(getHeaders(), true)
                .body("{}");

        return parseResponse(request.execute());
    }

    // ==================== Memory Management ====================

    /**
     * 创建记忆
     *
     * @param name        名称
     * @param memoryTypes 记忆类型列表
     * @param embdId      嵌入模型ID
     * @param llmId       LLM模型ID
     * @return 响应结果
     */
    public JSONObject createMemory(String name, List<String> memoryTypes, String embdId, String llmId) {
        String url = baseUrl + "/api/v1/memories";

        Map<String, Object> body = new HashMap<>();
        body.put("name", name);
        body.put("memory_type", memoryTypes);
        body.put("embd_id", embdId);
        body.put("llm_id", llmId);

        HttpRequest request = HttpRequest.post(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs())
                .body(JSONUtil.toJsonStr(body));

        return parseResponse(request.execute());
    }

    /**
     * 更新记忆
     *
     * @param memoryId 记忆ID
     * @param params   更新参数
     * @return 响应结果
     */
    public JSONObject updateMemory(String memoryId, Map<String, Object> params) {
        String url = baseUrl + "/api/v1/memories/" + memoryId;

        HttpRequest request = HttpRequest.put(url)
                .headerMap(getHeaders(), true)
                .body(JSONUtil.toJsonStr(params));

        return parseResponse(request.execute());
    }

    /**
     * 更新记忆名称
     *
     * @param memoryId 记忆ID
     * @param name     新名称
     * @return 响应结果
     */
    public JSONObject updateMemoryName(String memoryId, String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        return updateMemory(memoryId, params);
    }

    /**
     * 列出记忆
     *
     * @param params 查询参数
     * @return 响应结果
     */
    public JSONObject listMemories(Map<String, Object> params) {
        StringBuilder url = new StringBuilder(baseUrl + "/api/v1/memories?");

        if (params != null) {
            params.forEach((key, value) -> {
                if (value instanceof List) {
                    List<?> list = (List<?>) value;
                    String joined = list.stream()
                        .map(Object::toString)
                        .reduce((a, b) -> a + "," + b)
                        .orElse("");
                    url.append(key).append("=").append(joined).append("&");
                } else {
                    url.append(key).append("=").append(value).append("&");
                }
            });
        }

        String urlStr = url.toString().replaceAll("&$", "");

        HttpRequest request = HttpRequest.get(urlStr)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs());

        return parseResponse(request.execute());
    }

    /**
     * 列出记忆 - 简化版
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @return 响应结果
     */
    public JSONObject listMemories(Integer page, Integer pageSize) {
        Map<String, Object> params = new HashMap<>();
        if (page != null) params.put("page", page);
        if (pageSize != null) params.put("page_size", pageSize);
        return listMemories(params);
    }

    /**
     * 获取记忆配置
     *
     * @param memoryId 记忆ID
     * @return 响应结果
     */
    public JSONObject getMemoryConfig(String memoryId) {
        String url = baseUrl + "/api/v1/memories/" + memoryId + "/config";

        HttpRequest request = HttpRequest.get(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs());

        return parseResponse(request.execute());
    }

    /**
     * 删除记忆
     *
     * @param memoryId 记忆ID
     * @return 响应结果
     */
    public JSONObject deleteMemory(String memoryId) {
        String url = baseUrl + "/api/v1/memories/" + memoryId;

        HttpRequest request = HttpRequest.delete(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs());

        return parseResponse(request.execute());
    }

    /**
     * 列出记忆中的消息
     *
     * @param memoryId 记忆ID
     * @param params   查询参数
     * @return 响应结果
     */
    public JSONObject listMemoryMessages(String memoryId, Map<String, Object> params) {
        StringBuilder url = new StringBuilder(baseUrl + "/api/v1/memories/" + memoryId + "?");

        if (params != null) {
            params.forEach((key, value) -> url.append(key).append("=").append(value).append("&"));
        }

        String urlStr = url.toString().replaceAll("&$", "");

        HttpRequest request = HttpRequest.get(urlStr)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs());

        return parseResponse(request.execute());
    }

    /**
     * 添加消息
     *
     * @param memoryIds     记忆ID列表
     * @param agentId       代理ID
     * @param sessionId     会话ID
     * @param userId        用户ID
     * @param userInput     用户输入
     * @param agentResponse 代理响应
     * @return 响应结果
     */
    public JSONObject addMessage(List<String> memoryIds, String agentId, String sessionId,
                                  String userId, String userInput, String agentResponse) {
        String url = baseUrl + "/api/v1/messages";

        Map<String, Object> body = new HashMap<>();
        body.put("memory_id", memoryIds);
        body.put("agent_id", agentId);
        body.put("session_id", sessionId);
        if (userId != null) body.put("user_id", userId);
        body.put("user_input", userInput);
        body.put("agent_response", agentResponse);

        HttpRequest request = HttpRequest.post(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs())
                .body(JSONUtil.toJsonStr(body));

        return parseResponse(request.execute());
    }

    /**
     * 忘记消息
     *
     * @param memoryId 记忆ID
     * @param messageId 消息ID
     * @return 响应结果
     */
    public JSONObject forgetMessage(String memoryId, String messageId) {
        String url = baseUrl + "/api/v1/messages/" + memoryId + ":" + messageId;

        HttpRequest request = HttpRequest.delete(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs());

        return parseResponse(request.execute());
    }

    /**
     * 更新消息状态
     *
     * @param memoryId 记忆ID
     * @param messageId 消息ID
     * @param status    状态
     * @return 响应结果
     */
    public JSONObject updateMessageStatus(String memoryId, String messageId, boolean status) {
        String url = baseUrl + "/api/v1/messages/" + memoryId + ":" + messageId;

        Map<String, Object> body = new HashMap<>();
        body.put("status", status);

        HttpRequest request = HttpRequest.put(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs())
                .body(JSONUtil.toJsonStr(body));

        return parseResponse(request.execute());
    }

    /**
     * 搜索消息
     *
     * @param query                查询内容
     * @param memoryIds            记忆ID列表
     * @param similarityThreshold  相似度阈值
     * @param keywordsSimilarityWeight 关键词相似度权重
     * @param topN                 返回数量
     * @return 响应结果
     */
    public JSONObject searchMessages(String query, List<String> memoryIds, Float similarityThreshold,
                                      Float keywordsSimilarityWeight, Integer topN) {
        return searchMessages(query, memoryIds, null, null, similarityThreshold, keywordsSimilarityWeight, topN);
    }

    /**
     * 搜索消息 - 完整参数
     *
     * @param query                  查询内容
     * @param memoryIds              记忆ID列表
     * @param agentId                代理ID
     * @param sessionId              会话ID
     * @param similarityThreshold    相似度阈值
     * @param keywordsSimilarityWeight 关键词相似度权重
     * @param topN                   返回数量
     * @return 响应结果
     */
    public JSONObject searchMessages(String query, List<String> memoryIds, String agentId, String sessionId,
                                      Float similarityThreshold, Float keywordsSimilarityWeight, Integer topN) {
        StringBuilder url = new StringBuilder(baseUrl + "/api/v1/messages/search?");

        url.append("query=").append(query).append("&");
        url.append("memory_id=").append(String.join(",", memoryIds)).append("&");
        if (agentId != null) url.append("agent_id=").append(agentId).append("&");
        if (sessionId != null) url.append("session_id=").append(sessionId).append("&");
        if (similarityThreshold != null) url.append("similarity_threshold=").append(similarityThreshold).append("&");
        if (keywordsSimilarityWeight != null) url.append("keywords_similarity_weight=").append(keywordsSimilarityWeight).append("&");
        if (topN != null) url.append("top_n=").append(topN).append("&");

        String urlStr = url.toString().replaceAll("&$", "");

        HttpRequest request = HttpRequest.get(urlStr)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs());

        return parseResponse(request.execute());
    }

    /**
     * 获取最近消息
     *
     * @param memoryIds 记忆ID列表
     * @param limit     限制数量
     * @return 响应结果
     */
    public JSONObject getRecentMessages(List<String> memoryIds, Integer limit) {
        return getRecentMessages(memoryIds, null, null, limit);
    }

    /**
     * 获取最近消息 - 完整参数
     *
     * @param memoryIds 记忆ID列表
     * @param agentId   代理ID
     * @param sessionId 会话ID
     * @param limit     限制数量
     * @return 响应结果
     */
    public JSONObject getRecentMessages(List<String> memoryIds, String agentId, String sessionId, Integer limit) {
        StringBuilder url = new StringBuilder(baseUrl + "/api/v1/messages?");

        url.append("memory_id=").append(String.join(",", memoryIds)).append("&");
        if (agentId != null) url.append("agent_id=").append(agentId).append("&");
        if (sessionId != null) url.append("session_id=").append(sessionId).append("&");
        if (limit != null) url.append("limit=").append(limit).append("&");

        String urlStr = url.toString().replaceAll("&$", "");

        HttpRequest request = HttpRequest.get(urlStr)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs());

        return parseResponse(request.execute());
    }

    /**
     * 获取消息内容
     *
     * @param memoryId  记忆ID
     * @param messageId 消息ID
     * @return 响应结果
     */
    public JSONObject getMessageContent(String memoryId, String messageId) {
        String url = baseUrl + "/api/v1/messages/" + memoryId + ":" + messageId + "/content";

        HttpRequest request = HttpRequest.get(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs());

        return parseResponse(request.execute());
    }

    // ==================== System ====================

    /**
     * 检查系统健康状态
     *
     * @return 响应结果
     */
    public JSONObject checkSystemHealth() {
        String url = baseUrl + "/v1/system/healthz";

        HttpRequest request = HttpRequest.get(url)
                .header("Content-Type", "application/json");

        try (HttpResponse response = request.execute()) {
            if (response.getStatus() == 200 || response.getStatus() == 500) {
                return JSONUtil.parseObj(response.body());
            }
            throw new RuntimeException("健康检查失败: " + response.getStatus());
        }
    }

    // ==================== File Management ====================

    /**
     * 上传文件
     *
     * @param files    文件列表
     * @param parentId 父文件夹ID
     * @return 响应结果
     */
    public JSONObject uploadFiles(List<File> files, String parentId) {
        String url = baseUrl + "/api/v1/file/upload";

        HttpRequest request = HttpRequest.post(url)
                .headerMap(getMultipartHeaders(), false)
                .timeout(getTimeoutMs());

        for (File file : files) {
            request.form("file", file);
        }
        if (parentId != null) {
            request.form("parent_id", parentId);
        }

        return parseResponse(request.execute());
    }

    /**
     * 上传文件 - 简化版
     *
     * @param files 文件列表
     * @return 响应结果
     */
    public JSONObject uploadFiles(List<File> files) {
        return uploadFiles(files, null);
    }

    /**
     * 创建文件或文件夹
     *
     * @param name     名称
     * @param type     类型 FOLDER/VIRTUAL
     * @param parentId 父文件夹ID
     * @return 响应结果
     */
    public JSONObject createFileOrFolder(String name, String type, String parentId) {
        String url = baseUrl + "/api/v1/file/create";

        Map<String, Object> body = new HashMap<>();
        body.put("name", name);
        body.put("type", type);
        if (parentId != null) body.put("parent_id", parentId);

        HttpRequest request = HttpRequest.post(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs())
                .body(JSONUtil.toJsonStr(body));

        return parseResponse(request.execute());
    }

    /**
     * 创建文件夹
     *
     * @param name     文件夹名称
     * @param parentId 父文件夹ID
     * @return 响应结果
     */
    public JSONObject createFolder(String name, String parentId) {
        return createFileOrFolder(name, "FOLDER", parentId);
    }

    /**
     * 列出文件
     *
     * @param params 查询参数
     * @return 响应结果
     */
    public JSONObject listFiles(Map<String, Object> params) {
        StringBuilder url = new StringBuilder(baseUrl + "/api/v1/file/list?");

        if (params != null) {
            params.forEach((key, value) -> url.append(key).append("=").append(value).append("&"));
        }

        String urlStr = url.toString().replaceAll("&$", "");

        HttpRequest request = HttpRequest.get(urlStr)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs());

        return parseResponse(request.execute());
    }

    /**
     * 列出文件 - 简化版
     *
     * @param parentId 父文件夹ID
     * @param page     页码
     * @param pageSize 每页数量
     * @return 响应结果
     */
    public JSONObject listFiles(String parentId, Integer page, Integer pageSize) {
        Map<String, Object> params = new HashMap<>();
        if (parentId != null) params.put("parent_id", parentId);
        if (page != null) params.put("page", page);
        if (pageSize != null) params.put("page_size", pageSize);
        return listFiles(params);
    }

    /**
     * 获取根文件夹
     *
     * @return 响应结果
     */
    public JSONObject getRootFolder() {
        String url = baseUrl + "/api/v1/file/root_folder";

        HttpRequest request = HttpRequest.get(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs());

        return parseResponse(request.execute());
    }

    /**
     * 获取父文件夹
     *
     * @param fileId 文件ID
     * @return 响应结果
     */
    public JSONObject getParentFolder(String fileId) {
        String url = baseUrl + "/api/v1/file/parent_folder?file_id=" + fileId;

        HttpRequest request = HttpRequest.get(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs());

        return parseResponse(request.execute());
    }

    /**
     * 获取所有父文件夹
     *
     * @param fileId 文件ID
     * @return 响应结果
     */
    public JSONObject getAllParentFolders(String fileId) {
        String url = baseUrl + "/api/v1/file/all_parent_folder?file_id=" + fileId;

        HttpRequest request = HttpRequest.get(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs());

        return parseResponse(request.execute());
    }

    /**
     * 删除文件
     *
     * @param fileIds 文件ID列表
     * @return 响应结果
     */
    public JSONObject deleteFiles(List<String> fileIds) {
        String url = baseUrl + "/api/v1/file/rm";

        Map<String, Object> body = new HashMap<>();
        body.put("file_ids", fileIds);

        HttpRequest request = HttpRequest.post(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs())
                .body(JSONUtil.toJsonStr(body));

        return parseResponse(request.execute());
    }

    /**
     * 删除单个文件
     *
     * @param fileId 文件ID
     * @return 响应结果
     */
    public JSONObject deleteFile(String fileId) {
        return deleteFiles(List.of(fileId));
    }

    /**
     * 重命名文件
     *
     * @param fileId 文件ID
     * @param name   新名称
     * @return 响应结果
     */
    public JSONObject renameFile(String fileId, String name) {
        String url = baseUrl + "/api/v1/file/rename";

        Map<String, Object> body = new HashMap<>();
        body.put("file_id", fileId);
        body.put("name", name);

        HttpRequest request = HttpRequest.post(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs())
                .body(JSONUtil.toJsonStr(body));

        return parseResponse(request.execute());
    }

    /**
     * 下载文件
     *
     * @param fileId     文件ID
     * @param outputPath 输出路径
     * @return 是否成功
     */
    public boolean downloadFile(String fileId, String outputPath) {
        String url = baseUrl + "/api/v1/file/get/" + fileId;

        HttpRequest request = HttpRequest.get(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs())
                .setFollowRedirects(true);

        try (HttpResponse response = request.execute()) {
            if (response.getStatus() == 200) {
                java.nio.file.Files.copy(
                    response.bodyStream(),
                    java.nio.file.Paths.get(outputPath),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING
                );
                return true;
            }
            return false;
        } catch (IOException e) {
            throw new RuntimeException("下载文件失败: " + e.getMessage(), e);
        }
    }

    /**
     * 移动文件
     *
     * @param srcFileIds  源文件ID列表
     * @param destFileId  目标文件夹ID
     * @return 响应结果
     */
    public JSONObject moveFiles(List<String> srcFileIds, String destFileId) {
        String url = baseUrl + "/api/v1/file/mv";

        Map<String, Object> body = new HashMap<>();
        body.put("src_file_ids", srcFileIds);
        body.put("dest_file_id", destFileId);

        HttpRequest request = HttpRequest.post(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs())
                .body(JSONUtil.toJsonStr(body));

        return parseResponse(request.execute());
    }

    /**
     * 转换文件为文档并关联数据集
     *
     * @param fileIds 文件ID列表
     * @param kbIds   数据集ID列表
     * @return 响应结果
     */
    public JSONObject convertFilesToDocuments(List<String> fileIds, List<String> kbIds) {
        String url = baseUrl + "/api/v1/file/convert";

        Map<String, Object> body = new HashMap<>();
        body.put("file_ids", fileIds);
        body.put("kb_ids", kbIds);

        HttpRequest request = HttpRequest.post(url)
                .headerMap(getHeaders(), true)
                .timeout(getTimeoutMs())
                .body(JSONUtil.toJsonStr(body));

        return parseResponse(request.execute());
    }

    // ==================== 流式响应支持 ====================

    /**
     * 聊天助手对话 - 流式响应
     *
     * @param chatId   聊天ID
     * @param question 问题
     * @param sessionId 会话ID
     * @param listener 流式监听器
     */
    public void converseWithChatAssistantStream(String chatId, String question,
                                                  String sessionId, StreamListener listener) {
        converseWithChatAssistantStream(chatId, question, sessionId, null, listener);
    }

    /**
     * 聊天助手对话 - 流式响应
     *
     * @param chatId            聊天ID
     * @param question          问题
     * @param sessionId         会话ID
     * @param metadataCondition 元数据条件
     * @param listener          流式监听器
     */
    public void converseWithChatAssistantStream(String chatId, String question,
                                                  String sessionId,
                                                  Map<String, Object> metadataCondition,
                                                  StreamListener listener) {
        String url = baseUrl + "/api/v1/chats/" + chatId + "/completions";

        Map<String, Object> body = new HashMap<>();
        body.put("question", question);
        body.put("stream", true);
        if (sessionId != null) body.put("session_id", sessionId);
        if (metadataCondition != null) body.put("metadata_condition", metadataCondition);

        HttpRequest request = HttpRequest.post(url)
                .headerMap(getHeaders(), true)
                .body(JSONUtil.toJsonStr(body))
                .setFollowRedirects(true);

        try (HttpResponse response = request.execute()) {
            if (response.getStatus() == 200) {
                StreamResponseHandler handler = new StreamResponseHandler(listener);
                handler.handle(response.bodyStream(), chatId, sessionId);
            } else {
                String errorBody = response.body();
                if (listener != null) {
                    listener.onError("HTTP " + response.getStatus() + ": " + errorBody);
                }
            }
        } catch (Exception e) {
            if (listener != null) {
                listener.onException(e);
            }
        }
    }

    /**
     * 聊天助手对话 - 流式响应（收集结果）
     *
     * @param chatId   聊天ID
     * @param question 问题
     * @param sessionId 会话ID
     * @return 流式结果
     */
    public StreamResult converseWithChatAssistantStream(String chatId, String question, String sessionId) {
        return converseWithChatAssistantStreamCollect(chatId, question, sessionId, null);
    }

    /**
     * 聊天助手对话 - 流式响应（收集结果）
     *
     * @param chatId            聊天ID
     * @param question          问题
     * @param sessionId         会话ID
     * @param metadataCondition 元数据条件
     * @return 流式结果
     */
    public StreamResult converseWithChatAssistantStreamCollect(String chatId, String question,
                                                          String sessionId,
                                                          Map<String, Object> metadataCondition) {
        String url = baseUrl + "/api/v1/chats/" + chatId + "/completions";

        Map<String, Object> body = new HashMap<>();
        body.put("question", question);
        body.put("stream", true);
        if (sessionId != null) body.put("session_id", sessionId);
        if (metadataCondition != null) body.put("metadata_condition", metadataCondition);

        HttpRequest request = HttpRequest.post(url)
                .headerMap(getHeaders(), true)
                .body(JSONUtil.toJsonStr(body))
                .setFollowRedirects(true);

        try (HttpResponse response = request.execute()) {
            if (response.getStatus() == 200) {
                return StreamResponseHandler.collect(response.bodyStream(), chatId, sessionId);
            } else {
                StreamResult result = new StreamResult();
                result.setError("HTTP " + response.getStatus() + ": " + response.body());
                return result;
            }
        } catch (Exception e) {
            StreamResult result = new StreamResult();
            result.setException(e);
            return result;
        }
    }

    /**
     * OpenAI兼容聊天 - 流式响应
     *
     * @param chatId    聊天ID
     * @param model     模型名称
     * @param messages  消息列表
     * @param listener  流式监听器
     */
    public void createChatCompletionStream(String chatId, String model,
                                             List<Map<String, String>> messages,
                                             StreamListener listener) {
        createChatCompletionStream(chatId, model, messages, null, listener);
    }

    /**
     * OpenAI兼容聊天 - 流式响应
     *
     * @param chatId    聊天ID
     * @param model     模型名称
     * @param messages  消息列表
     * @param reference 是否包含引用
     * @param listener  流式监听器
     */
    public void createChatCompletionStream(String chatId, String model,
                                             List<Map<String, String>> messages,
                                             Boolean reference,
                                             StreamListener listener) {
        String url = baseUrl + "/api/v1/chats_openai/" + chatId + "/chat/completions";

        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("messages", messages);
        body.put("stream", true);

        Map<String, Object> extraBody = new HashMap<>();
        if (reference != null) {
            extraBody.put("reference", reference);
        }
        if (!extraBody.isEmpty()) {
            body.put("extra_body", extraBody);
        }

        HttpRequest request = HttpRequest.post(url)
                .headerMap(getHeaders(), true)
                .body(JSONUtil.toJsonStr(body))
                .setFollowRedirects(true);

        try (HttpResponse response = request.execute()) {
            if (response.getStatus() == 200) {
                StreamResponseHandler handler = new StreamResponseHandler(listener);
                handler.handle(response.bodyStream(), chatId, null);
            } else {
                if (listener != null) {
                    listener.onError("HTTP " + response.getStatus() + ": " + response.body());
                }
            }
        } catch (Exception e) {
            if (listener != null) {
                listener.onException(e);
            }
        }
    }

    /**
     * OpenAI兼容代理聊天 - 流式响应
     *
     * @param agentId   代理ID
     * @param model     模型名称
     * @param messages  消息列表
     * @param listener  流式监听器
     */
    public void createAgentCompletionStream(String agentId, String model,
                                              List<Map<String, String>> messages,
                                              StreamListener listener) {
        String url = baseUrl + "/api/v1/agents_openai/" + agentId + "/chat/completions";

        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("messages", messages);
        body.put("stream", true);

        HttpRequest request = HttpRequest.post(url)
                .headerMap(getHeaders(), true)
                .body(JSONUtil.toJsonStr(body))
                .setFollowRedirects(true);

        try (HttpResponse response = request.execute()) {
            if (response.getStatus() == 200) {
                StreamResponseHandler handler = new StreamResponseHandler(listener);
                handler.handle(response.bodyStream(), agentId, null);
            } else {
                if (listener != null) {
                    listener.onError("HTTP " + response.getStatus() + ": " + response.body());
                }
            }
        } catch (Exception e) {
            if (listener != null) {
                listener.onException(e);
            }
        }
    }

    /**
     * 与代理对话 - 流式响应
     *
     * @param agentId   代理ID
     * @param question  问题
     * @param sessionId 会话ID
     * @param inputs    输入参数
     * @param listener  流式监听器
     */
    public void converseWithAgentStream(String agentId, String question,
                                          String sessionId, Map<String, Object> inputs,
                                          StreamListener listener) {
        String url = baseUrl + "/api/v1/agents/" + agentId + "/completions";

        Map<String, Object> body = new HashMap<>();
        body.put("question", question);
        body.put("stream", true);
        if (sessionId != null) body.put("session_id", sessionId);
        if (inputs != null) body.put("inputs", inputs);

        HttpRequest request = HttpRequest.post(url)
                .headerMap(getHeaders(), true)
                .body(JSONUtil.toJsonStr(body))
                .setFollowRedirects(true);

        try (HttpResponse response = request.execute()) {
            if (response.getStatus() == 200) {
                StreamResponseHandler handler = new StreamResponseHandler(listener);
                handler.handle(response.bodyStream(), agentId, sessionId);
            } else {
                if (listener != null) {
                    listener.onError("HTTP " + response.getStatus() + ": " + response.body());
                }
            }
        } catch (Exception e) {
            if (listener != null) {
                listener.onException(e);
            }
        }
    }

    /**
     * 简单的流式回调处理器
     *
     * @param onChunk    内容片段回调
     * @param onComplete 完成回调
     * @param onError    错误回调
     * @return 监听器
     */
    public static StreamListener createSimpleListener(
            java.util.function.Consumer<String> onChunk,
            Runnable onComplete,
            java.util.function.Consumer<String> onError) {

        return new StreamListener() {
            @Override
            public void onEvent(StreamEvent event) {
                // 空实现，可根据需要扩展
            }

            @Override
            public void onChunk(String content, String chunkId) {
                if (onChunk != null) {
                    onChunk.accept(content);
                }
            }

            @Override
            public void onComplete(boolean complete) {
                if (complete && onComplete != null) {
                    onComplete.run();
                }
            }

            @Override
            public void onError(String error) {
                if (onError != null) {
                    onError.accept(error);
                }
            }
        };
    }
}
