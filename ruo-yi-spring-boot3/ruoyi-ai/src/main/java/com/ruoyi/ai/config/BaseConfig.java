package com.ruoyi.ai.config;


import com.ruoyi.ai.domain.ChatModelList;
import com.ruoyi.ai.domain.VectorDatabaseList;
import com.ruoyi.ai.service.ChatAssistant;
import com.ruoyi.ai.service.impl.ChatModelListServiceImpl;
import com.ruoyi.ai.service.impl.VectorDatabaseListServiceImpl;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import dev.langchain4j.store.embedding.qdrant.QdrantEmbeddingStore;
import dev.langchain4j.store.embedding.redis.RedisEmbeddingStore;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import org.springframework.ai.autoconfigure.openai.OpenAiChatProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration(proxyBeanMethods = false)
public class BaseConfig {


    @Autowired
    private ChatModelListServiceImpl chatModelListService;
    @Autowired
    private VectorDatabaseListServiceImpl vectorDatabaseListService;

    @Bean
    public StreamingChatLanguageModel streamingChatLanguageModel(){
        // 选择一个启用的聊天模型配置
        ChatModelList chooseModel = chatModelListService.selectEnableChatModel();
        StreamingChatLanguageModel ollamaStreamingChatModel = OllamaStreamingChatModel.builder()
                .baseUrl(chooseModel.getBaseUrl())  // 基础URL
                .modelName(chooseModel.getModelVersion())  // 模型名称
                .temperature(chooseModel.getTemperature().doubleValue())  // 温度
                .timeout(Duration.ofSeconds(30))  // 设置超时
                .build();
        return ollamaStreamingChatModel;
    }

    @Bean
    public ChatLanguageModel chatLanguageModel(){
        // 选择一个启用的聊天模型配置
        ChatModelList chooseModel = chatModelListService.selectEnableChatModel();
        ChatLanguageModel chatModel = OllamaChatModel.builder()
                .baseUrl(chooseModel.getBaseUrl())
                .modelName(chooseModel.getModelVersion())
                .temperature(chooseModel.getTemperature().doubleValue())
                .timeout(Duration.ofSeconds(30))
                .maxRetries(3)
                .logRequests(true)
                .logResponses(true)
                .build();
        return chatModel;
    }
    @Bean
    public ChatAssistant chatAssistant(StreamingChatLanguageModel streamingChatLanguageModel, EmbeddingStore<TextSegment> inMemoryEmbeddingStore){
        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);
        return AiServices.builder(ChatAssistant.class)
                .streamingChatLanguageModel(streamingChatLanguageModel)
                .chatMemory(chatMemory)
                //.contentRetriever(EmbeddingStoreContentRetriever.from(inMemoryEmbeddingStore))
                .build();
    }

    /**
     * 嵌入存储 (简易内存存储)
     *
     * @return {@link InMemoryEmbeddingStore }<{@link TextSegment }>
     */
    @Bean
    public InMemoryEmbeddingStore<TextSegment> inMemoryEmbeddingStore() {
        return new InMemoryEmbeddingStore<>();
    }


    @Bean
    public OllamaEmbeddingModel ollamaEmbeddingModel() {
        return OllamaEmbeddingModel.builder()
                .modelName("nomic-embed-text:latest")
                .baseUrl("http://127.0.0.1:11434")
                .build();
    }



    @Bean
    public QdrantClient qdrantClient() {
        QdrantGrpcClient.Builder grpcClientBuilder = QdrantGrpcClient.newBuilder("192.168.48.128", 6334, false);
        return new QdrantClient(grpcClientBuilder.build());
    }

//    @Bean
//    public EmbeddingStore<TextSegment> embeddingStore() {
//        return RedisEmbeddingStore.builder()
//                .host("192.168.48.129")
//                .port(6379)
//                .dimension(768) //维度，需要与计算结果保持⼀致。如果使⽤其他的模型，可能会有不同的结果。
//                .build();
//    }
    @Bean
    public PgVectorEmbeddingStore pgVectorEmbeddingStore() {
        // 本地无 PgVector 服务, 禁用该 Bean, 使用 inMemoryEmbeddingStore
        return null;
    }

}
