package com.ruoyi.ai.controller;


import com.ruoyi.ai.domain.ChatSession;
import com.ruoyi.ai.domain.ChatSessionDetail;
import com.ruoyi.ai.service.ChatAssistant;
import com.ruoyi.ai.service.IChatSessionDetailService;
import com.ruoyi.ai.service.impl.LangchainServiceImpl;
import com.ruoyi.ai.utils.ChatSessionManager;
import com.ruoyi.ai.utils.PersistentChatMemoryStore;
import com.ruoyi.ai.utils.RuoyiDocumentSplitter;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.*;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/langchain")
public class LangchainController {

    @Autowired
    private LangchainServiceImpl langchainService;

    @Autowired
    private OllamaStreamingChatModel ollamaStreamingChatModel;

    @Autowired
    private OllamaEmbeddingModel ollamaEmbeddingModel;

    @Autowired
    @org.springframework.beans.factory.annotation.Qualifier("inMemoryEmbeddingStore")
    private EmbeddingStore embeddingStore;

    @Autowired
    private InMemoryEmbeddingStore inMemoryEmbeddingStore;

    @Autowired
    private ChatAssistant assistant;

    @Autowired
    private ChatSessionManager chatSessionManager;

    @Autowired
    private IChatSessionDetailService chatSessionDetailService;

    /**
     * 普通消息
     */
    @GetMapping("/ollamaChatModel")
    public AjaxResult ollamaChatModel(@RequestParam("msg") String msg){
        String answer = langchainService.ollamaChatModel(msg);
        return AjaxResult.success(answer);
    }

    /**
     * 流式消息
     */
    @GetMapping(value = "/ollamaChatStreaming",produces = "text/event-stream")
    public Flux<String> ollamaChatStreaming(@RequestParam("msg") String msg){
        return langchainService.ollamaChatStreaming(msg);
    }

    /**
     * 放行流式消息
     */
    @GetMapping(value = "/ollamaChatStreamingRelease")
    public Flux<String> ollamaChatStreamingRelease(@RequestParam("msg") String msg){
        return langchainService.ollamaChatStreaming(msg);
    }


    /**
     * 带文本的存储
     */
    @GetMapping(value = "/withTextStorage")
    public AjaxResult withTextStorage(@RequestParam("content") String content){
        TextSegment textSegment = TextSegment.from(content);
        textSegment.metadata().put("author", "欧阳日峰");
        Embedding embedding = ollamaEmbeddingModel.embed(textSegment).content();
        embeddingStore.add(embedding, textSegment);
        return AjaxResult.success();
    }


    /**
     * Redis向量库
     */
    @GetMapping(value = "/withTextStorageByRedis")
    public AjaxResult withTextStorageByRedis(@RequestParam("content") String content){
        TextSegment textSegment = TextSegment.from(content);
        textSegment.metadata().put("author", "欧阳日峰");
        Embedding embedding = ollamaEmbeddingModel.embed(textSegment).content();
        embeddingStore.add(embedding, textSegment);
        return AjaxResult.success(embedding.vector().length);
    }

    /**
     * Redis文本向量化
     */
    @GetMapping(value = "/withDocumentStorageByRedis")
    public AjaxResult withDocumentStorageByRedis(){
        Path documentPath = null;
        try {
            documentPath = Paths.get(LangchainController.class.getClassLoader().getResource("Java题目.txt").toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        DocumentParser documentParser = new TextDocumentParser();
        Document document = FileSystemDocumentLoader.loadDocument(documentPath,documentParser);
        DocumentSplitter splitter = new  RuoyiDocumentSplitter();
        List<TextSegment> segments = splitter.split(document);
        List<Embedding> embeddings = ollamaEmbeddingModel.embedAll(segments).content();
        embeddingStore.addAll(embeddings, segments);
        return AjaxResult.success();
    }



    /**
     * Langchain内存向量化
     */
    @GetMapping(value = "/withDocumentStorageByLangchain")
    @Anonymous
    public Flux<String> withDocumentStorageByLangchain(){
        Document document = FileSystemDocumentLoader.loadDocument("F:\\ruoyi-deepseek\\ruoyi-ai\\target\\classes\\Java题目.pdf");
        EmbeddingStoreIngestor.ingest(document,inMemoryEmbeddingStore);
        return assistant.ollamaChatStreaming("总结这个文档");
    }

    /**
     * Pg向量化
     */
    @GetMapping(value = "/withDocumentStorageByPg" )
    @Anonymous
    public AjaxResult withDocumentStorageByPg(){
        EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();
        Document document = FileSystemDocumentLoader.loadDocument("F:\\ruoyi-deepseek\\ruoyi-ai\\target\\classes\\Java题目.txt");
        DocumentSplitter documentSplitter = new  RuoyiDocumentSplitter();
        EmbeddingStoreIngestor.builder()
                .documentSplitter(documentSplitter)
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build()
                .ingest(document);
        return AjaxResult.success();
    }
    /**
     * Pg向量化查找
     */
    @GetMapping(value = "/findDocumentStorageByPg",produces = "text/event-stream")
    @Anonymous
    public Flux<String> findDocumentStorageByPg(@RequestParam("text") String text){
        EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .maxMessages(10) // 设置最大消息数为 10
                .build();

        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(2) // on each interaction we will retrieve the 2 most relevant segments
                .minScore(0.9) // we want to retrieve segments at least somewhat similar to user query
                .build();
        ChatAssistant chatAssistant = AiServices.builder(ChatAssistant.class)
                .streamingChatLanguageModel(ollamaStreamingChatModel)
                .contentRetriever(contentRetriever)
                .chatMemory(chatMemory)
                .build();
        return chatAssistant.ollamaChatStreaming(text);
    }

    /**
     * chat
     */
    @GetMapping(value = "/streamChat", produces = "text/event-stream")
    @Anonymous
    public Flux<String> streamChat(@RequestParam("text") String text, @RequestParam("sessionId") Long sessionId) {
        ChatMemory memory = chatSessionManager.getSessionMemory(sessionId);
        if (memory == null) {
            throw new IllegalArgumentException("Invalid session ID");
        }
        // 保存用户消息
        ChatSessionDetail userMsg = new ChatSessionDetail ();
        userMsg.setSessionId(sessionId);
        userMsg.setRole("user");
        userMsg.setContent(text);
        userMsg.setCreateTime(new Date());
        chatSessionDetailService.insertChatSessionDetail(userMsg);
        EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();
        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(2) // on each interaction we will retrieve the 2 most relevant segments
                .minScore(0.2) // we want to retrieve segments at least somewhat similar to user query
                .build();
        ChatAssistant chatAssistant = AiServices.builder(ChatAssistant.class)
                .streamingChatLanguageModel(ollamaStreamingChatModel)
                .contentRetriever(contentRetriever)
                .chatMemory(memory)
                .build();
        StringBuilder responseBuffer = new StringBuilder();
        return chatAssistant.ollamaChatStreaming(text)
                .doOnNext(responseBuffer::append)
                .doFinally(e -> {
                    // 保存AI响应消息
                    ChatSessionDetail aiMsg = new ChatSessionDetail();
                    aiMsg.setSessionId(sessionId);
                    aiMsg.setRole("assistant");
                    aiMsg.setContent(responseBuffer.toString());
                    aiMsg.setCreateTime(new Date());
                    chatSessionDetailService.insertChatSessionDetail(aiMsg);
                });
    }
}
