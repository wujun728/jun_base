package com.ruoyi.ai.service.impl;

import com.ruoyi.ai.domain.ChatModelList;
import com.ruoyi.ai.service.IOllamaService;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * OllamaServiceImpl
 *
 * @author 欧阳日峰
 * @date 2025-02-06
 */
@Service
public class OllamaServiceImpl implements IOllamaService {
    @Autowired
    private OllamaChatClient ollamaChatClient;
    @Autowired
    private ChatModelListServiceImpl chatModelListService;


    @Override
    public ChatResponse sendMessage(String msg) {
        ChatModelList list = chatModelListService.selectEnableChatModel();
        ChatResponse call = ollamaChatClient.call(new Prompt(msg, OllamaOptions.create().withModel(list.getModelVersion()).withTemperature(list.getTemperature().floatValue())));
        return call;
    }
}
