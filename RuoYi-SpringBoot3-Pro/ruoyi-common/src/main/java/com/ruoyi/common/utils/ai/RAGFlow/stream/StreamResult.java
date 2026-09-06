package com.ruoyi.common.utils.ai.RAGFlow.stream;

import java.util.ArrayList;
import java.util.List;

/**
 * 流式结果收集器
 * 用于同步获取流式响应的完整结果
 *
 * @author RuoYi-SpringBoot3-Pro
 */
public class StreamResult {

    /**
     * 收集的内容片段
     */
    private StringBuilder contentBuilder = new StringBuilder();

    /**
     * 完整的响应内容
     */
    private String fullContent;

    /**
     * 引用信息
     */
    private Object reference;

    /**
     * 聊天ID
     */
    private String chatId;

    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * 消息ID
     */
    private String messageId;

    /**
     * 最后一个块ID
     */
    private String chunkId;

    /**
     * 是否完成
     */
    private boolean complete;

    /**
     * 错误信息
     */
    private String error;

    /**
     * 异常
     */
    private Exception exception;

    /**
     * 所有事件
     */
    private List<StreamEvent> events = new ArrayList<>();

    /**
     * 收集的内容片段列表
     */
    private List<String> chunks = new ArrayList<>();

    /**
     * 追加内容
     */
    public void appendContent(String content) {
        if (content != null) {
            contentBuilder.append(content);
            chunks.add(content);
        }
    }

    /**
     * 获取完整内容
     */
    public String getFullContent() {
        if (fullContent == null) {
            fullContent = contentBuilder.toString();
        }
        return fullContent;
    }

    /**
     * 获取内容片段
     */
    public List<String> getChunks() {
        return chunks;
    }

    /**
     * 获取内容片段数量
     */
    public int getChunkCount() {
        return chunks.size();
    }

    /**
     * 是否为空
     */
    public boolean isEmpty() {
        return contentBuilder.length() == 0;
    }

    /**
     * 是否有内容
     */
    public boolean hasContent() {
        return contentBuilder.length() > 0;
    }

    /**
     * 是否成功完成
     */
    public boolean isSuccess() {
        return complete && error == null && exception == null;
    }

    // Getters and Setters
    public Object getReference() { return reference; }
    public void setReference(Object reference) { this.reference = reference; }

    public String getChatId() { return chatId; }
    public void setChatId(String chatId) { this.chatId = chatId; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }

    public String getChunkId() { return chunkId; }
    public void setChunkId(String chunkId) { this.chunkId = chunkId; }

    public boolean isComplete() { return complete; }
    public void setComplete(boolean complete) { this.complete = complete; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public Exception getException() { return exception; }
    public void setException(Exception exception) { this.exception = exception; }

    public List<StreamEvent> getEvents() { return events; }
    public void setEvents(List<StreamEvent> events) { this.events = events; }

    public void addEvent(StreamEvent event) {
        this.events.add(event);
    }

    @Override
    public String toString() {
        return "StreamResult{" +
                "contentLength=" + contentBuilder.length() +
                ", chunkCount=" + chunks.size() +
                ", complete=" + complete +
                ", hasReference=" + (reference != null) +
                ", error=" + error +
                '}';
    }
}
