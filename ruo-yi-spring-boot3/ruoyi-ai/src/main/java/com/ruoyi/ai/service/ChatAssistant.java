package com.ruoyi.ai.service;


import reactor.core.publisher.Flux;

/**
 * @author 欧阳
 * @date 2025/2/18
 */
public interface ChatAssistant {

    Flux<String> ollamaChatStreaming(String msg);

    String chat(String msg);
}
