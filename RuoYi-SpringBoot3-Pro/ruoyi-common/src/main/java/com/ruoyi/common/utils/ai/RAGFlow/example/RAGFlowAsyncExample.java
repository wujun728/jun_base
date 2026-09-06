package com.ruoyi.common.utils.ai.RAGFlow.example;

import cn.hutool.json.JSONObject;
import com.ruoyi.common.utils.ai.RAGFlow.RAGFlow;
import com.ruoyi.common.utils.ai.RAGFlow.async.RAGFlowAsync;
import com.ruoyi.common.utils.ai.RAGFlow.stream.StreamResult;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * RAGFlow 异步客户端使用示例
 *
 * @author RuoYi-SpringBoot3-Pro
 */
public class RAGFlowAsyncExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 创建同步客户端
        RAGFlow ragflow = new RAGFlow("http://localhost:9222", "your-api-key");

        // 创建异步客户端
        RAGFlowAsync asyncClient = new RAGFlowAsync(ragflow);

        try {
            // ========== 基础异步调用 ==========
            basicAsyncExamples(asyncClient);

            // ========== 组合异步调用 ==========
            combinedAsyncExamples(asyncClient);

            // ========== 异常处理 ==========
            errorHandlingExamples(asyncClient);

        } finally {
            // 关闭异步客户端资源
            asyncClient.close();
        }
    }

    /**
     * 基础异步调用示例
     */
    private static void basicAsyncExamples(RAGFlowAsync asyncClient)
            throws ExecutionException, InterruptedException {

        // 1. 简单的异步调用
        CompletableFuture<JSONObject> future = asyncClient.createDataset("async_dataset");
        JSONObject result = future.get();  // 阻塞等待结果
        System.out.println("创建数据集: " + result);

        // 2. 使用thenCompose链式调用
        String datasetId = asyncClient.createDataset("chain_dataset")
                .thenCompose(response -> {
                    if (response.getInt("code") == 0) {
                        return CompletableFuture.completedFuture(
                                response.getJSONObject("data").getStr("id"));
                    }
                    return CompletableFuture.failedFuture(
                            new RuntimeException("创建数据集失败"));
                })
                .get();

        System.out.println("创建的数据集ID: " + datasetId);

        // 3. 并行执行多个请求
        CompletableFuture<JSONObject> listFuture = asyncClient.listDatasets(1, 30, "create_time", true, null, null);
        CompletableFuture<JSONObject> healthFuture = asyncClient.checkSystemHealth();

        // 等待所有请求完成
        CompletableFuture.allOf(listFuture, healthFuture).get();

        JSONObject listResult = listFuture.get();
        JSONObject healthResult = healthFuture.get();

        System.out.println("数据集列表: " + listResult);
        System.out.println("健康检查: " + healthResult);
    }

    /**
     * 组合异步调用示例
     */
    private static void combinedAsyncExamples(RAGFlowAsync asyncClient)
            throws ExecutionException, InterruptedException {

        // 1. 先创建数据集，再上传文档
        String datasetId = asyncClient.createDataset("upload_dataset")
                .thenCompose(createResult -> {
                    if (createResult.getInt("code") == 0) {
                        return CompletableFuture.completedFuture(
                                createResult.getJSONObject("data").getStr("id"));
                    }
                    throw new RuntimeException("创建数据集失败: " + createResult.getStr("message"));
                })
                .get();

        // 上传文档
        List<java.io.File> files = Arrays.asList(
                new java.io.File("doc1.pdf"),
                new java.io.File("doc2.pdf")
        );

        JSONObject uploadResult = asyncClient.uploadDocuments(datasetId, files).get();
        System.out.println("上传文档结果: " + uploadResult);

        // 2. 并行执行不相关的请求
        CompletableFuture<JSONObject> datasetsFuture = asyncClient.listDatasets(1, 10, null, null, null, null);
        CompletableFuture<JSONObject> chatsFuture = asyncClient.listChatAssistants(null);
        CompletableFuture<JSONObject> healthFuture = asyncClient.checkSystemHealth();

        // 等待所有请求完成
        CompletableFuture.allOf(datasetsFuture, chatsFuture, healthFuture).join();

        System.out.println("数据集: " + datasetsFuture.get());
        System.out.println("聊天助手: " + chatsFuture.get());
        System.out.println("系统状态: " + healthFuture.get());

        // 3. 第一个请求完成后触发第二个请求
        asyncClient.listChatAssistants(null)
                .thenCompose(chats -> {
                    if (chats.getInt("code") == 0) {
                        // 获取第一个聊天助手ID
                        String chatId = chats.getJSONArray("data")
                                .getJSONObject(0)
                                .getStr("id");

                        // 发起对话
                        return asyncClient.converseWithChatAssistant(chatId, "你好", false, null)
                                .thenApply(chatResult -> {
                                    System.out.println("对话结果: " + chatResult);
                                    return chatResult;
                                });
                    }
                    return CompletableFuture.completedFuture(chats);
                })
                .get();
    }

    /**
     * 异常处理示例
     */
    private static void errorHandlingExamples(RAGFlowAsync asyncClient)
            throws ExecutionException, InterruptedException {

        // 1. 基础异常处理
        CompletableFuture<JSONObject> future = asyncClient.createDataset("test");
        try {
            JSONObject result = future.get();
            if (result.getInt("code") != 0) {
                System.err.println("请求失败: " + result.getStr("message"));
            }
        } catch (ExecutionException e) {
            System.err.println("执行异常: " + e.getCause().getMessage());
        }

        // 2. 使用exceptionally处理异常
        JSONObject result = asyncClient.createDataset("test2")
                .exceptionally(throwable -> {
                    System.err.println("发生异常: " + throwable.getMessage());
                    return new JSONObject(Map.of("code", -1, "message", throwable.getMessage()));
                })
                .get();

        System.out.println("结果: " + result);

        // 3. 使用handle统一处理结果和异常
        JSONObject handledResult = asyncClient.createDataset("test3")
                .handle((resp, ex) -> {
                    if (ex != null) {
                        System.err.println("异常: " + ex.getMessage());
                        return new JSONObject(Map.of("code", -1, "message", ex.getMessage()));
                    }
                    return resp;
                })
                .get();

        System.out.println("处理后的结果: " + handledResult);
    }

    /**
     * 流式响应异步示例
     */
    private static void streamExample(RAGFlowAsync asyncClient)
            throws ExecutionException, InterruptedException {

        // 1. 流式响应收集结果
        StreamResult streamResult = asyncClient
                .converseWithChatAssistantStream("chat_id", "请详细介绍一下RAGFlow", null)
                .get();

        System.out.println("完整内容: " + streamResult.getFullContent());
        System.out.println("片段数量: " + streamResult.getChunkCount());
        System.out.println("引用: " + streamResult.getReference());
        System.out.println("完成: " + streamResult.isComplete());

        // 2. 流式响应后继续异步操作
        asyncClient.converseWithChatAssistantStream("chat_id", "第二个问题", null)
                .thenAccept(streamResult2 -> {
                    System.out.println("流式响应完成");
                    System.out.println("内容长度: " + streamResult2.getFullContent().length());

                    // 可以继续执行其他异步操作
                    asyncClient.checkSystemHealth()
                            .thenAccept(health -> System.out.println("健康检查: " + health));
                })
                .get();
    }

    /**
     * 文件管理异步示例
     */
    private static void fileManagementExample(RAGFlowAsync asyncClient)
            throws ExecutionException, InterruptedException {

        // 1. 批量上传文件
        List<java.io.File> files = Arrays.asList(
                new java.io.File("file1.txt"),
                new java.io.File("file2.txt"),
                new java.io.File("file3.txt")
        );

        asyncClient.uploadFiles(files, null)
                .thenAccept(uploadResult -> System.out.println("上传结果: " + uploadResult))
                .get();

        // 2. 并行列文件和检查健康
        CompletableFuture<JSONObject> filesFuture = asyncClient.listFiles(null);
        CompletableFuture<JSONObject> healthFuture = asyncClient.checkSystemHealth();

        CompletableFuture.allOf(filesFuture, healthFuture).join();

        System.out.println("文件列表: " + filesFuture.get());
        System.out.println("系统健康: " + healthFuture.get());
    }

    /**
     * 使用自定义执行器
     */
    private static void customExecutorExample(RAGFlow ragflow) {
        // 创建自定义执行器（固定大小线程池）
        java.util.concurrent.ExecutorService customExecutor =
                java.util.concurrent.Executors.newFixedThreadPool(4);

        // 使用自定义执行器创建异步客户端
        RAGFlowAsync asyncClient = new RAGFlowAsync(ragflow, customExecutor);

        try {
            // 执行异步操作
            asyncClient.checkSystemHealth()
                    .thenAccept(System.out::println)
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            customExecutor.shutdown();
        }
    }
}
