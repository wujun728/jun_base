package com.ruoyi.common.utils.ai.RAGFlow.async;

import cn.hutool.json.JSONObject;
import com.ruoyi.common.utils.ai.RAGFlow.RAGFlow;
import com.ruoyi.common.utils.ai.RAGFlow.stream.StreamListener;
import com.ruoyi.common.utils.ai.RAGFlow.stream.StreamResult;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * RAGFlow 异步客户端
 * 提供基于CompletableFuture的异步API调用
 *
 * @author RuoYi-SpringBoot3-Pro
 */
public class RAGFlowAsync {

    private final RAGFlow ragflow;
    private final ExecutorService executor;

    public RAGFlowAsync(RAGFlow ragflow) {
        this.ragflow = ragflow;
        this.executor = Executors.newCachedThreadPool();
    }

    public RAGFlowAsync(RAGFlow ragflow, ExecutorService executor) {
        this.ragflow = ragflow;
        this.executor = executor;
    }

    /**
     * 设置自定义执行器
     */
    public RAGFlowAsync setExecutor(ExecutorService executor) {
        return new RAGFlowAsync(ragflow, executor);
    }

    // ==================== 数据集管理异步方法 ====================

    /**
     * 创建数据集 - 异步
     */
    public CompletableFuture<JSONObject> createDataset(String name) {
        return CompletableFuture.supplyAsync(() -> ragflow.createDataset(name), executor);
    }

    /**
     * 创建数据集 - 异步（完整参数）
     */
    public CompletableFuture<JSONObject> createDataset(String name, String avatar, String description,
                                                        String embeddingModel, String permission,
                                                        String chunkMethod, Map<String, Object> parserConfig) {
        return CompletableFuture.supplyAsync(() ->
            ragflow.createDataset(name, avatar, description, embeddingModel, permission, chunkMethod, parserConfig),
            executor);
    }

    /**
     * 删除数据集 - 异步
     */
    public CompletableFuture<JSONObject> deleteDatasets(List<String> ids) {
        return CompletableFuture.supplyAsync(() -> ragflow.deleteDatasets(ids), executor);
    }

    /**
     * 删除单个数据集 - 异步
     */
    public CompletableFuture<JSONObject> deleteDataset(String datasetId) {
        return CompletableFuture.supplyAsync(() -> ragflow.deleteDataset(datasetId), executor);
    }

    /**
     * 更新数据集 - 异步
     */
    public CompletableFuture<JSONObject> updateDataset(String datasetId, Map<String, Object> params) {
        return CompletableFuture.supplyAsync(() -> ragflow.updateDataset(datasetId, params), executor);
    }

    /**
     * 列出数据集 - 异步
     */
    public CompletableFuture<JSONObject> listDatasets(Integer page, Integer pageSize, String orderBy,
                                                       Boolean desc, String name, String id) {
        return CompletableFuture.supplyAsync(() ->
            ragflow.listDatasets(page, pageSize, orderBy, desc, name, id),
            executor);
    }

    /**
     * 获取知识图谱 - 异步
     */
    public CompletableFuture<JSONObject> getKnowledgeGraph(String datasetId) {
        return CompletableFuture.supplyAsync(() -> ragflow.getKnowledgeGraph(datasetId), executor);
    }

    /**
     * 构建知识图谱 - 异步
     */
    public CompletableFuture<JSONObject> constructKnowledgeGraph(String datasetId) {
        return CompletableFuture.supplyAsync(() -> ragflow.constructKnowledgeGraph(datasetId), executor);
    }

    // ==================== 文档管理异步方法 ====================

    /**
     * 上传文档 - 异步
     */
    public CompletableFuture<JSONObject> uploadDocuments(String datasetId, List<java.io.File> files) {
        return CompletableFuture.supplyAsync(() -> ragflow.uploadDocuments(datasetId, files), executor);
    }

    /**
     * 更新文档 - 异步
     */
    public CompletableFuture<JSONObject> updateDocument(String datasetId, String documentId,
                                                         Map<String, Object> params) {
        return CompletableFuture.supplyAsync(() ->
            ragflow.updateDocument(datasetId, documentId, params),
            executor);
    }

    /**
     * 列出文档 - 异步
     */
    public CompletableFuture<JSONObject> listDocuments(String datasetId, Map<String, Object> params) {
        return CompletableFuture.supplyAsync(() -> ragflow.listDocuments(datasetId, params), executor);
    }

    /**
     * 删除文档 - 异步
     */
    public CompletableFuture<JSONObject> deleteDocuments(String datasetId, List<String> documentIds) {
        return CompletableFuture.supplyAsync(() -> ragflow.deleteDocuments(datasetId, documentIds), executor);
    }

    /**
     * 下载文档 - 异步
     */
    public CompletableFuture<Boolean> downloadDocument(String datasetId, String documentId, String outputPath) {
        return CompletableFuture.supplyAsync(() ->
            ragflow.downloadDocument(datasetId, documentId, outputPath),
            executor);
    }

    // ==================== 块管理异步方法 ====================

    /**
     * 添加块 - 异步
     */
    public CompletableFuture<JSONObject> addChunk(String datasetId, String documentId, String content) {
        return CompletableFuture.supplyAsync(() -> ragflow.addChunk(datasetId, documentId, content), executor);
    }

    /**
     * 列出块 - 异步
     */
    public CompletableFuture<JSONObject> listChunks(String datasetId, String documentId,
                                                     Map<String, Object> params) {
        return CompletableFuture.supplyAsync(() -> ragflow.listChunks(datasetId, documentId, params), executor);
    }

    /**
     * 更新块 - 异步
     */
    public CompletableFuture<JSONObject> updateChunk(String datasetId, String documentId, String chunkId,
                                                      Map<String, Object> params) {
        return CompletableFuture.supplyAsync(() ->
            ragflow.updateChunk(datasetId, documentId, chunkId, params),
            executor);
    }

    /**
     * 删除块 - 异步
     */
    public CompletableFuture<JSONObject> deleteChunks(String datasetId, String documentId,
                                                       List<String> chunkIds) {
        return CompletableFuture.supplyAsync(() -> ragflow.deleteChunks(datasetId, documentId, chunkIds), executor);
    }

    // ==================== 检索异步方法 ====================

    /**
     * 检索块 - 异步
     */
    public CompletableFuture<JSONObject> retrieveChunks(String question, List<String> datasetIds) {
        return CompletableFuture.supplyAsync(() -> ragflow.retrieveChunks(question, datasetIds), executor);
    }

    /**
     * 检索块 - 异步（完整参数）
     */
    public CompletableFuture<JSONObject> retrieveChunks(String question, List<String> datasetIds,
                                                         List<String> documentIds, Integer page,
                                                         Integer pageSize, Float similarityThreshold,
                                                         Float vectorSimilarityWeight, Integer topK,
                                                         Boolean keyword, Boolean highlight,
                                                         Map<String, Object> metadataCondition) {
        return CompletableFuture.supplyAsync(() ->
            ragflow.retrieveChunks(question, datasetIds, documentIds, page, pageSize,
                similarityThreshold, vectorSimilarityWeight, topK, keyword, highlight, metadataCondition),
            executor);
    }

    // ==================== 聊天助手异步方法 ====================

    /**
     * 创建聊天助手 - 异步
     */
    public CompletableFuture<JSONObject> createChatAssistant(String name, List<String> datasetIds) {
        return CompletableFuture.supplyAsync(() -> ragflow.createChatAssistant(name, datasetIds), executor);
    }

    /**
     * 更新聊天助手 - 异步
     */
    public CompletableFuture<JSONObject> updateChatAssistant(String chatId, Map<String, Object> params) {
        return CompletableFuture.supplyAsync(() -> ragflow.updateChatAssistant(chatId, params), executor);
    }

    /**
     * 列出聊天助手 - 异步
     */
    public CompletableFuture<JSONObject> listChatAssistants(Map<String, Object> params) {
        return CompletableFuture.supplyAsync(() -> ragflow.listChatAssistants(params), executor);
    }

    /**
     * 删除聊天助手 - 异步
     */
    public CompletableFuture<JSONObject> deleteChatAssistant(String chatId) {
        return CompletableFuture.supplyAsync(() -> ragflow.deleteChatAssistant(chatId), executor);
    }

    /**
     * 对话 - 异步（非流式）
     */
    public CompletableFuture<JSONObject> converseWithChatAssistant(String chatId, String question,
                                                                    boolean stream, String sessionId) {
        return CompletableFuture.supplyAsync(() ->
            ragflow.converseWithChatAssistant(chatId, question, stream, sessionId),
            executor);
    }

    /**
     * 对话 - 流式异步
     */
    public void converseWithChatAssistantStream(String chatId, String question, String sessionId,
                                                  StreamListener listener) {
        ragflow.converseWithChatAssistantStream(chatId, question, sessionId, listener);
    }

    /**
     * 对话 - 流式异步（收集结果）
     */
    public CompletableFuture<StreamResult> converseWithChatAssistantStream(String chatId, String question,
                                                                            String sessionId) {
        return CompletableFuture.supplyAsync(() ->
            ragflow.converseWithChatAssistantStream(chatId, question, sessionId),
            executor);
    }

    // ==================== 会话管理异步方法 ====================

    /**
     * 创建会话 - 异步
     */
    public CompletableFuture<JSONObject> createSession(String chatId, String name) {
        return CompletableFuture.supplyAsync(() -> ragflow.createSession(chatId, name), executor);
    }

    /**
     * 列出会话 - 异步
     */
    public CompletableFuture<JSONObject> listChatSessions(String chatId, Map<String, Object> params) {
        return CompletableFuture.supplyAsync(() -> ragflow.listChatSessions(chatId, params), executor);
    }

    /**
     * 删除会话 - 异步
     */
    public CompletableFuture<JSONObject> deleteChatSessions(String chatId, List<String> ids) {
        return CompletableFuture.supplyAsync(() -> ragflow.deleteChatSessions(chatId, ids), executor);
    }

    // ==================== 文件管理异步方法 ====================

    /**
     * 上传文件 - 异步
     */
    public CompletableFuture<JSONObject> uploadFiles(List<java.io.File> files, String parentId) {
        return CompletableFuture.supplyAsync(() -> ragflow.uploadFiles(files, parentId), executor);
    }

    /**
     * 列出文件 - 异步
     */
    public CompletableFuture<JSONObject> listFiles(Map<String, Object> params) {
        return CompletableFuture.supplyAsync(() -> ragflow.listFiles(params), executor);
    }

    /**
     * 创建文件夹 - 异步
     */
    public CompletableFuture<JSONObject> createFolder(String name, String parentId) {
        return CompletableFuture.supplyAsync(() -> ragflow.createFolder(name, parentId), executor);
    }

    /**
     * 删除文件 - 异步
     */
    public CompletableFuture<JSONObject> deleteFiles(List<String> fileIds) {
        return CompletableFuture.supplyAsync(() -> ragflow.deleteFiles(fileIds), executor);
    }

    /**
     * 重命名文件 - 异步
     */
    public CompletableFuture<JSONObject> renameFile(String fileId, String name) {
        return CompletableFuture.supplyAsync(() -> ragflow.renameFile(fileId, name), executor);
    }

    /**
     * 移动文件 - 异步
     */
    public CompletableFuture<JSONObject> moveFiles(List<String> srcFileIds, String destFileId) {
        return CompletableFuture.supplyAsync(() -> ragflow.moveFiles(srcFileIds, destFileId), executor);
    }

    /**
     * 下载文件 - 异步
     */
    public CompletableFuture<Boolean> downloadFile(String fileId, String outputPath) {
        return CompletableFuture.supplyAsync(() -> ragflow.downloadFile(fileId, outputPath), executor);
    }

    // ==================== 系统相关异步方法 ====================

    /**
     * 检查系统健康 - 异步
     */
    public CompletableFuture<JSONObject> checkSystemHealth() {
        return CompletableFuture.supplyAsync(() -> ragflow.checkSystemHealth(), executor);
    }

    // ==================== 工具方法 ====================

    /**
     * 异步执行回调
     */
    public <T> CompletableFuture<T> execute(java.util.function.Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(supplier, executor);
    }

    /**
     * 异步执行Runnable
     */
    public CompletableFuture<Void> runAsync(Runnable runnable) {
        return CompletableFuture.runAsync(runnable, executor);
    }

    /**
     * 关闭资源
     */
    public void close() {
        executor.shutdownNow();
    }
}
