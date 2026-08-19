package com.ruoyi.ai.utils;

import com.ruoyi.ai.domain.ChatSession;
import com.ruoyi.ai.service.IChatSessionService;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersistentChatMemoryStore implements ChatMemoryStore {

    @Autowired
    private IChatSessionService chatSessionService;

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        // TODO: 实现根据 memoryId 从持久化存储中获取所有消息。
        // 可以使用 ChatMessageDeserializer.messageFromJson(String) 和
        // ChatMessageDeserializer.messagesFromJson(String) 辅助方法将 JSON 反序列化为聊天消息。
        ChatSession session = chatSessionService.selectChatSessionById(Long.valueOf(String.valueOf(memoryId)));
        return ChatMessageDeserializer.messagesFromJson(session.getMemory());
    }


    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        // TODO: 实现根据 memoryId 更新持久化存储中的所有消息。
        // 可以使用 ChatMessageSerializer.messageToJson(ChatMessage) 和
        // ChatMessageSerializer.messagesToJson(List<ChatMessage>) 辅助方法将聊天消息序列化为 JSON。
        String chatMsg = ChatMessageSerializer.messagesToJson(messages);
        ChatSession session = new ChatSession();
        session.setId(Long.valueOf(String.valueOf(memoryId)));
        session.setMemory(chatMsg);
        chatSessionService.updateChatSession(session);
    }

    @Override
    public void deleteMessages(Object memoryId) {
        // TODO: 实现根据 memoryId 删除持久化存储中的所有消息。
    }
}
