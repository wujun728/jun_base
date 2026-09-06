package com.geek.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MessageType {

    /**
     * 事件
     */
    EVENT("event"),

    /**
     * 普通消息
     */
    MESSAGE("message"),

    /**
     * 异步消息
     */
    ASYNC_MESSAGE("asyncMessage");

    private final String type;

    MessageType(String type) {
        this.type = type;
    }

    @JsonCreator
    public static MessageType fromType(String value) {
        if (value == null) {
            return null;
        }
        for (MessageType messageType : MessageType.values()) {
            if (messageType.type.equalsIgnoreCase(value) || messageType.name().equalsIgnoreCase(value)) {
                return messageType;
            }
        }
        throw new IllegalArgumentException("Unknown message type: " + value);
    }

    @JsonValue
    public String getType() {
        return type;
    }
}
