package com.ruoyi.ai.service;

import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import org.springframework.ai.chat.ChatResponse;
import reactor.core.publisher.Flux;

public interface ILangchainService {

    /**
     * 发送信息
     *
     * @param msg 信息
     * @return sendMessage
     */
    public String ollamaChatModel(String msg);

    public Flux<String>  ollamaChatStreaming(String msg);
}
