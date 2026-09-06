package com.ruoyi.common.utils.ai.RAGFlow.stream;

import java.util.EventListener;

/**
 * 流式响应监听器
 * 用于处理RAGFlow流式响应（SSE）
 *
 * @author RuoYi-SpringBoot3-Pro
 */
public interface StreamListener extends EventListener {

    /**
     * 接收到数据事件
     *
     * @param event 流式事件
     */
    void onEvent(StreamEvent event);

    /**
     * 流开始
     *
     * @param chatId    聊天ID
     * @param sessionId 会话ID
     */
    default void onStart(String chatId, String sessionId) {}

    /**
     * 接收到消息片段
     *
     * @param content 消息内容
     * @param chunkId 块ID
     */
    default void onChunk(String content, String chunkId) {}

    /**
     * 接收到引用信息
     *
     * @param reference 引用数据
     */
    default void onReference(Object reference) {}

    /**
     * 流结束
     *
     * @param complete 是否正常完成
     */
    default void onComplete(boolean complete) {}

    /**
     * 发生错误
     *
     * @param error 错误信息
     */
    default void onError(String error) {}

    /**
     * 发生异常
     *
     * @param exception 异常
     */
    default void onException(Exception exception) {}
}
