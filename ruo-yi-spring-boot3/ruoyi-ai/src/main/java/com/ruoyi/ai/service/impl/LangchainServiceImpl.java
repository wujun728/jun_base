package com.ruoyi.ai.service.impl;

import com.ruoyi.ai.domain.ChatModelList;
import com.ruoyi.ai.service.ChatAssistant;
import com.ruoyi.ai.service.ILangchainService;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
public class LangchainServiceImpl implements ILangchainService {


    @Autowired
    private ChatModelListServiceImpl chatModelListService;

    @Autowired
    private ChatAssistant chatAssistant;

    @Override
    public String ollamaChatModel(String msg) {
        interface Assistant {
            @SystemMessage("请用中文回答")
            String chat(String msg);
        }
        ChatModelList chooseModel = chatModelListService.selectEnableChatModel();
        OllamaChatModel chatModel = OllamaChatModel.builder()
                .baseUrl(chooseModel.getBaseUrl())
                .modelName(chooseModel.getModelVersion())
                .temperature(chooseModel.getTemperature().doubleValue())
                .timeout(Duration.ofSeconds(30))
                .topK(chooseModel.getTopK())
                .topP(chooseModel.getTopP().doubleValue())
                .repeatPenalty(chooseModel.getRepeatPenalty().doubleValue())
                .numPredict(chooseModel.getNumPredict())
                .numCtx(chooseModel.getNumPredict())
                .maxRetries(3)
                .logRequests(true)
                .logResponses(true)
                .build();
        Assistant assistant = AiServices.create(Assistant.class, chatModel);
        String answer = assistant.chat(msg);
        return answer;
    }


    /**
     * 弃用
     * @param msg
     * @return
     */
    @Override
    public Flux<String> ollamaChatStreaming(String msg) {
       return chatAssistant.ollamaChatStreaming(msg);
    }
}
