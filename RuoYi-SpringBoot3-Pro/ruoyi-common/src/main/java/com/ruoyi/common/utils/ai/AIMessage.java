package com.ruoyi.common.utils.ai;

import lombok.Data;

@Data
public class AIMessage {
    public AIMessage() {

    }

    public AIMessage(AIRole role, String message) {
        this.role = role;
        this.message = message;
    }

    private AIRole role;
    private String message;
}
