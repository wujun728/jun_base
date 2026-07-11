package com.ruoyi.common.core.domain;

import com.ruoyi.common.enums.MessageType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Message {
    private MessageType type;
    private Object payload;
    private String receiver;
    private String sender;
}
