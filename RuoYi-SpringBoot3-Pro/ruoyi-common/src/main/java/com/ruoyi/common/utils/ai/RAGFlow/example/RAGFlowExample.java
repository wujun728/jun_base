package com.ruoyi.common.utils.ai.RAGFlow.example;

import cn.hutool.json.JSONObject;
import com.ruoyi.common.utils.ai.RAGFlow.RAGFlow;
import com.ruoyi.common.utils.ai.RAGFlow.config.RAGFlowConfig;
import com.ruoyi.common.utils.ai.RAGFlow.dto.*;
import com.ruoyi.common.utils.ai.RAGFlow.util.RAGFlowUtils;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RAGFlow 客户端使用示例
 */
public class RAGFlowExample {

    public static void main(String[] args) {
        // 方式1: 直接创建客户端
        RAGFlow ragflow = new RAGFlow("http://localhost:9222", "your-api-key");

        // 方式2: 使用配置类创建（Spring环境）
        // RAGFlow ragflow = RAGFlowConfig.createClient("http://localhost:9222", "your-api-key");

        // 方式3: 使用工具类创建
        // RAGFlow ragflow = RAGFlowUtils.createClient("http://localhost:9222", "your-api-key");

        try {
            // ========== 数据集管理 ==========
            exampleDatasetManagement(ragflow);

            // ========== 文档管理 ==========
            exampleDocumentManagement(ragflow);

            // ========== 块管理 ==========
            exampleChunkManagement(ragflow);

            // ========== 聊天助手 ==========
            exampleChatAssistant(ragflow);

            // ========== 检索 ==========
            exampleRetrieval(ragflow);

            // ========== 文件管理 ==========
            exampleFileManagement(ragflow);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 数据集管理示例
     */
    private static void exampleDatasetManagement(RAGFlow ragflow) {
        // 1. 创建数据集
        JSONObject createResult = ragflow.createDataset("test_dataset");
        if (RAGFlowUtils.isSuccess(createResult)) {
            String datasetId = createResult.getJSONObject("data").getStr("id");
            System.out.println("创建数据集成功: " + datasetId);
        }

        // 2. 列出数据集
        JSONObject listResult = ragflow.listDatasets(1, 30, "create_time", true, null, null);
        if (RAGFlowUtils.isSuccess(listResult)) {
            List<DatasetDTO.Dataset> datasets = RAGFlowUtils.getDataList(listResult, DatasetDTO.Dataset.class);
            System.out.println("数据集数量: " + datasets.size());
        }

        // 3. 更新数据集
        Map<String, Object> updateParams = new HashMap<>();
        updateParams.put("name", "updated_dataset");
        ragflow.updateDataset("dataset_id", updateParams);

        // 4. 删除数据集
        ragflow.deleteDatasets(List.of("dataset_id"));

        // 5. 获取知识图谱
        ragflow.getKnowledgeGraph("dataset_id");

        // 6. 构建知识图谱
        JSONObject graphResult = ragflow.constructKnowledgeGraph("dataset_id");
        if (RAGFlowUtils.isSuccess(graphResult)) {
            String taskId = graphResult.getJSONObject("data").getStr("graphrag_task_id");
            System.out.println("知识图谱构建任务ID: " + taskId);
        }
    }

    /**
     * 文档管理示例
     */
    private static void exampleDocumentManagement(RAGFlow ragflow) {
        String datasetId = "dataset_id";

        // 1. 上传文档
        File file = new File("test.pdf");
        JSONObject uploadResult = ragflow.uploadDocuments(datasetId, List.of(file));
        if (RAGFlowUtils.isSuccess(uploadResult)) {
            String documentId = uploadResult.getJSONArray("data").getJSONObject(0).getStr("id");
            System.out.println("上传文档成功: " + documentId);
        }

        // 2. 列出文档
        JSONObject listResult = ragflow.listDocuments(datasetId, 1, 10);
        if (RAGFlowUtils.isSuccess(listResult)) {
            System.out.println("文档列表获取成功");
        }

        // 3. 更新文档
        ragflow.updateDocument(datasetId, "document_id", Map.of("name", "new_name.pdf"));

        // 4. 删除文档
        ragflow.deleteDocuments(datasetId, List.of("document_id"));

        // 5. 下载文档
        ragflow.downloadDocument(datasetId, "document_id", "download.pdf");

        // 6. 解析文档
        ragflow.parseDocuments(datasetId, List.of("document_id"));
    }

    /**
     * 块管理示例
     */
    private static void exampleChunkManagement(RAGFlow ragflow) {
        String datasetId = "dataset_id";
        String documentId = "document_id";

        // 1. 添加块
        JSONObject addResult = ragflow.addChunk(datasetId, documentId, "这是块内容");
        System.out.println("添加块: " + addResult);

        // 2. 列出块
        JSONObject listResult = ragflow.listChunks(datasetId, documentId, 1, 10, null);
        if (RAGFlowUtils.isSuccess(listResult)) {
            System.out.println("块列表获取成功");
        }

        // 3. 更新块
        ragflow.updateChunk(datasetId, documentId, "chunk_id", Map.of("content", "更新后的内容"));

        // 4. 删除块
        ragflow.deleteChunks(datasetId, documentId, List.of("chunk_id"));

        // 5. 获取元数据摘要
        JSONObject metaResult = ragflow.getMetadataSummary(datasetId);
        System.out.println("元数据摘要: " + metaResult);
    }

    /**
     * 聊天助手示例
     */
    private static void exampleChatAssistant(RAGFlow ragflow) {
        // 1. 创建聊天助手
        Map<String, Object> llm = new HashMap<>();
        llm.put("model_name", "qwen-plus@Tongyi-Qianwen");
        llm.put("temperature", 0.1);

        JSONObject createResult = ragflow.createChatAssistant(
                "my_chat",
                List.of("dataset_id"),
                null,
                llm,
                null
        );

        if (RAGFlowUtils.isSuccess(createResult)) {
            String chatId = createResult.getJSONObject("data").getStr("id");
            System.out.println("创建聊天助手成功: " + chatId);

            // 2. 创建会话
            JSONObject sessionResult = ragflow.createSession(chatId, "新会话");
            if (RAGFlowUtils.isSuccess(sessionResult)) {
                String sessionId = sessionResult.getJSONObject("data").getStr("id");
                System.out.println("创建会话成功: " + sessionId);

                // 3. 对话
                JSONObject chatResult = ragflow.converseWithChatAssistant(
                        chatId,
                        "你好，请介绍一下RAGFlow",
                        false,
                        sessionId
                );
                System.out.println("对话响应: " + chatResult);
            }
        }

        // 4. 列出聊天助手
        ragflow.listChatAssistants(1, 30);

        // 5. 更新聊天助手
        ragflow.updateChatAssistant("chat_id", Map.of("name", "新名称"));

        // 6. 删除聊天助手
        ragflow.deleteChatAssistant("chat_id");
    }

    /**
     * 检索示例
     */
    private static void exampleRetrieval(RAGFlow ragflow) {
        // 1. 简单检索
        JSONObject result = ragflow.retrieveChunks(
                "RAGFlow是什么",
                List.of("dataset_id")
        );

        if (RAGFlowUtils.isSuccess(result)) {
            System.out.println("检索结果: " + result);
        }

        // 2. 带条件的检索
        ChunkDTO.MetadataCondition condition = ChunkDTO.MetadataCondition.and(
                List.of(
                        new ChunkDTO.Condition("author", "is", "Alice"),
                        new ChunkDTO.Condition("tag", "contains", "技术")
                )
        );

        ragflow.retrieveChunks(
                "RAGFlow是什么",
                List.of("dataset_id"),
                null,
                1,
                10,
                0.2f,
                0.3f,
                1024,
                false,
                true,
                RAGFlowUtils.buildMetadataCondition(
                        List.of(
                                RAGFlowUtils.buildCondition("author", "is", "Alice"),
                                RAGFlowUtils.buildCondition("tag", "contains", "技术")
                        ),
                        "and"
                )
        );
    }

    /**
     * 文件管理示例
     */
    private static void exampleFileManagement(RAGFlow ragflow) {
        // 1. 上传文件
        File file = new File("test.txt");
        JSONObject uploadResult = ragflow.uploadFiles(List.of(file));
        System.out.println("上传文件: " + uploadResult);

        // 2. 创建文件夹
        ragflow.createFolder("新文件夹", null);

        // 3. 列出文件
        ragflow.listFiles(null, 1, 30);

        // 4. 获取根文件夹
        ragflow.getRootFolder();

        // 5. 重命名文件
        ragflow.renameFile("file_id", "新名称.txt");

        // 6. 移动文件
        ragflow.moveFiles(List.of("file_id"), "dest_folder_id");

        // 7. 删除文件
        ragflow.deleteFile("file_id");

        // 8. 下载文件
        ragflow.downloadFile("file_id", "download.txt");
    }

    /**
     * 使用DTO示例
     */
    private static void exampleDTOUsage() {
        // 创建请求对象
        DatasetDTO.CreateDatasetRequest datasetRequest = new DatasetDTO.CreateDatasetRequest("my_dataset");
        datasetRequest.setChunkMethod(DatasetDTO.ChunkMethod.NAIVE.getValue());
        datasetRequest.setPermission(DatasetDTO.Permission.ME.getValue());

        // 创建聊天请求
        ChatDTO.ConversationRequest chatRequest = new ChatDTO.ConversationRequest("你好");
        chatRequest.setStream(false);
        chatRequest.setSessionId("session_id");

        // 创建会话请求
        ChatDTO.CreateSessionRequest sessionRequest = new ChatDTO.CreateSessionRequest("新会话");
        sessionRequest.setUserId("user_123");

        // 创建元数据条件
        ChunkDTO.MetadataCondition condition = ChunkDTO.MetadataCondition.and(
                List.of(
                        new ChunkDTO.Condition("author", "is", "bob")
                )
        );
    }
}
