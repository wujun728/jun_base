package com.ruoyi.ai.utils;


import com.ruoyi.ai.domain.ChatSession;
import com.ruoyi.ai.service.IChatSessionService;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatSessionManager {
    @Resource
    private PersistentChatMemoryStore chatMemoryStore;
    @Resource
    private IChatSessionService chatSessionService;

    private final ConcurrentHashMap<Long, ChatMemory> sessionMemories = new ConcurrentHashMap<>();
//    private final EmbeddingStoreContentRetriever contentRetriever; // 复用原有配置

    public void createSession(ChatSession session) {
        Long sessionId = session.getId();
        ChatMemory memory = MessageWindowChatMemory.builder()
                .id(sessionId)
                .maxMessages(20)
                .chatMemoryStore(chatMemoryStore)
                .build();
        sessionMemories.put(sessionId, memory);
    }

    public ChatMemory getSessionMemory(Long sessionId) {
        // 从缓存中获取
        if (sessionMemories.containsKey(sessionId)) {
            return sessionMemories.get(sessionId);
        }
        // 从数据库获取
        ChatSession chatSession = chatSessionService.selectChatSessionById(sessionId);
        if (chatSession != null) {
            ChatMemory memory = MessageWindowChatMemory.builder()
                    .id(sessionId)
                    .maxMessages(20)
                    .chatMemoryStore(chatMemoryStore)
                    .build();
            sessionMemories.put(sessionId, memory);
            return memory;
        }
        return null;
    }
}
