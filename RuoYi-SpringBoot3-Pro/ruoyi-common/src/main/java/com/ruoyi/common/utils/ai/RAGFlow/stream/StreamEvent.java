package com.ruoyi.common.utils.ai.RAGFlow.stream;

/**
 * 流式事件
 *
 * @author RuoYi-SpringBoot3-Pro
 */
public class StreamEvent {

    /**
     * 事件类型
     */
    private EventType type;

    /**
     * 事件数据
     */
    private String data;

    /**
     * 事件ID
     */
    private String id;

    /**
     * 事件类型名称
     */
    private String event;

    /**
     * 重试时间（毫秒）
     */
    private Long retry;

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
     * 原始行数据
     */
    private String rawLine;

    public StreamEvent() {}

    public StreamEvent(EventType type, String data) {
        this.type = type;
        this.data = data;
    }

    /**
     * 事件类型枚举
     */
    public enum EventType {
        /** 开始 */
        START,
        /** 数据 */
        DATA,
        /** 增量（内容） */
        DELTA,
        /** 引用 */
        REFERENCE,
        /** 完成 */
        DONE,
        /** 错误 */
        ERROR,
        /** 未知 */
        UNKNOWN
    }

    // Getters and Setters
    public EventType getType() { return type; }
    public void setType(EventType type) { this.type = type; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEvent() { return event; }
    public void setEvent(String event) { this.event = event; }

    public Long getRetry() { return retry; }
    public void setRetry(Long retry) { this.retry = retry; }

    public String getChatId() { return chatId; }
    public void setChatId(String chatId) { this.chatId = chatId; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }

    public String getRawLine() { return rawLine; }
    public void setRawLine(String rawLine) { this.rawLine = rawLine; }

    /**
     * 检查是否为完成事件
     */
    public boolean isDone() {
        return type == EventType.DONE ||
               (data != null && data.contains("[DONE]"));
    }

    /**
     * 检查是否为错误事件
     */
    public boolean isError() {
        return type == EventType.ERROR ||
               (data != null && data.startsWith("{") && data.contains("\"code\""));
    }

    @Override
    public String toString() {
        return "StreamEvent{" +
                "type=" + type +
                ", data='" + (data != null && data.length() > 100 ?
                    data.substring(0, 100) + "..." : data) +
                ", id='" + id + '\'' +
                '}';
    }
}
