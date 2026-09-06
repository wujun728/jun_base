package com.geek.common.core.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import com.geek.common.enums.MessageType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    /** 消息唯一标识符 */
    @Builder.Default
    private String messageId = UUID.randomUUID().toString();
    /** 发送者标识 */
    private String sender;
    /** 接收者标识 */
    private String receiver;
    /** 消息时间戳 */
    @Builder.Default
    private Instant timestamp = Instant.now();
    /** 消息类型（如命令、聊天、日志、事件等） */
    private MessageType type;
    /** 消息主题或事件名称 */
    private String subject;
    /** 消息数据内容 */
    private String content;
    /** 消息数据负载 */
    private Map<String, Object> payload;
    /** 元数据，用于存储额外的信息 */
    private Map<String, Object> metadata;
    /** 消息状态（如成功、失败、重试等） */
    private String status;
    /** 重试次数 */
    private int retryCount;
    /** 最大重试次数 */
    private int maxRetries;
    /** 重试间隔 */
    private String retryInterval;
}