package com.ruoyi.ai.tool;


import com.ruoyi.ai.domain.ChatSession;
import com.ruoyi.ai.service.IChatSessionService;
import com.ruoyi.ai.utils.PersistentChatMemoryStore;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class AiTool {

    @Tool("Returns a square root of a given number")
    public double squareRoot(double x) {
        return Math.sqrt(x);
    }

}
