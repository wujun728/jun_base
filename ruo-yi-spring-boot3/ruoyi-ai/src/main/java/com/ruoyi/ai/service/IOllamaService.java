package com.ruoyi.ai.service;

import com.ruoyi.ai.domain.ChatModelList;
import org.springframework.ai.chat.ChatResponse;

public interface IOllamaService {


    /**
     * 发送信息
     *
     * @param msg 信息
     * @return ChatResponse
     */
    public ChatResponse sendMessage(String msg);
}
