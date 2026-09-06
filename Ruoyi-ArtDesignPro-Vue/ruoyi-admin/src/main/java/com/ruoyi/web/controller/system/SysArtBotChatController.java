package com.ruoyi.web.controller.system;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.Executor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysArtBotConversation;
import com.ruoyi.system.domain.SysArtBotMessage;
import com.ruoyi.system.domain.SysArtBotModel;
import com.ruoyi.system.service.ISysArtBotService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@Tag(name = "AI 对话")
@RestController
@RequestMapping("/artbot")
@PreAuthorize("@ss.hasPermi('artbot:chat:use')")
public class SysArtBotChatController extends BaseController
{
    @Autowired
    private ISysArtBotService artBotService;

    @Autowired
    @Qualifier("threadPoolTaskExecutor")
    private Executor executor;

    @Operation(summary = "查询可用 AI 模型")
    @GetMapping("/models")
    public AjaxResult models()
    {
        return success(artBotService.selectEnabledModels());
    }

    @GetMapping("/conversations")
    public AjaxResult conversations()
    {
        return success(artBotService.selectConversationList(getUserId()));
    }

    @Operation(summary = "创建 AI 会话")
    @PostMapping("/conversations")
    public AjaxResult createConversation(@RequestBody ChatRequest request)
    {
        return success(artBotService.createConversation(getUserId(), request.getModelId()));
    }

    @DeleteMapping("/conversations/{conversationId}")
    @io.swagger.v3.oas.annotations.Parameter(description = "AI 会话ID")
    public AjaxResult deleteConversation(@PathVariable Long conversationId)
    {
        artBotService.deleteConversation(conversationId, getUserId());
        return success();
    }

    @Operation(summary = "查询 AI 会话消息")
    @GetMapping("/conversations/{conversationId}/messages")
    @io.swagger.v3.oas.annotations.Parameter(description = "AI 会话ID")
    public AjaxResult messages(@PathVariable Long conversationId)
    {
        return success(artBotService.selectMessageList(conversationId, getUserId()));
    }

    @PostMapping(value = "/chat", produces = "text/event-stream")
    public SseEmitter chat(@RequestBody ChatRequest request)
    {
        if (StringUtils.isEmpty(request.getContent()))
        {
            throw new ServiceException("消息不能为空");
        }
        Long userId = getUserId();
        SysArtBotConversation conversation = artBotService.selectConversation(request.getConversationId(), userId);
        SysArtBotModel model = artBotService.selectModelSecretById(conversation.getModelId());
        if (model == null || !"0".equals(model.getStatus()))
        {
            throw new ServiceException("当前会话模型已停用");
        }
        boolean firstMessage = artBotService.selectRecentMessages(conversation.getConversationId(), 1).isEmpty();
        artBotService.insertMessage(conversation.getConversationId(), "user", request.getContent().trim());
        if (firstMessage)
        {
            artBotService.titleConversation(conversation.getConversationId(), request.getContent());
        }

        SseEmitter emitter = new SseEmitter(0L);
        executor.execute(() -> stream(model, conversation.getConversationId(), emitter));
        return emitter;
    }

    private void stream(SysArtBotModel model, Long conversationId, SseEmitter emitter)
    {
        StringBuilder answer = new StringBuilder();
        try
        {
            JSONObject body = createCompletionBody(model, conversationId);
            HttpRequest upstream = HttpRequest.newBuilder(URI.create(SysArtBotModelController.endpoint(model.getBaseUrl())))
                .timeout(Duration.ofMinutes(5))
                .header("Authorization", "Bearer " + model.getApiKey())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body.toJSONString()))
                .build();
            HttpResponse<java.io.InputStream> response = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10)).build()
                .send(upstream, HttpResponse.BodyHandlers.ofInputStream());
            if (response.statusCode() < 200 || response.statusCode() >= 300)
            {
                String error = new String(response.body().readAllBytes(), StandardCharsets.UTF_8);
                throw new ServiceException("上游服务异常：HTTP " + response.statusCode() + " - " + error);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body(), StandardCharsets.UTF_8)))
            {
                String line;
                while ((line = reader.readLine()) != null)
                {
                    if (!line.startsWith("data:")) continue;
                    String data = line.substring(5).trim();
                    if ("[DONE]".equals(data)) break;
                    JSONObject chunk = JSONObject.parseObject(data);
                    JSONArray choices = chunk.getJSONArray("choices");
                    if (choices == null || choices.isEmpty()) continue;
                    JSONObject delta = choices.getJSONObject(0).getJSONObject("delta");
                    String content = delta == null ? null : delta.getString("content");
                    if (StringUtils.isNotEmpty(content))
                    {
                        answer.append(content);
                        emitter.send(SseEmitter.event().name("chunk").data(content));
                    }
                }
            }
            if (!answer.isEmpty())
            {
                artBotService.insertMessage(conversationId, "assistant", answer.toString());
            }
            emitter.send(SseEmitter.event().name("done").data("ok"));
            emitter.complete();
        }
        catch (Exception e)
        {
            try
            {
                emitter.send(SseEmitter.event().name("error").data(e.getMessage() == null ? "生成失败" : e.getMessage()));
            }
            catch (Exception ignored)
            {
            }
            emitter.complete();
        }
    }

    private JSONObject createCompletionBody(SysArtBotModel model, Long conversationId)
    {
        JSONObject body = new JSONObject();
        body.put("model", model.getModelCode());
        body.put("stream", true);
        if (model.getTemperature() != null) body.put("temperature", model.getTemperature());
        if (model.getMaxTokens() != null) body.put("max_tokens", model.getMaxTokens());
        JSONArray messages = new JSONArray();
        if (StringUtils.isNotEmpty(model.getSystemPrompt()))
        {
            messages.add(JSONObject.of("role", "system", "content", model.getSystemPrompt()));
        }
        List<SysArtBotMessage> recent = artBotService.selectRecentMessages(conversationId, 20);
        recent.forEach(message -> messages.add(JSONObject.of("role", message.getRole(), "content", message.getContent())));
        body.put("messages", messages);
        return body;
    }

    public static class ChatRequest
    {
        private Long conversationId;
        private Long modelId;
        private String content;
        public Long getConversationId() { return conversationId; }
        public void setConversationId(Long conversationId) { this.conversationId = conversationId; }
        public Long getModelId() { return modelId; }
        public void setModelId(Long modelId) { this.modelId = modelId; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }
}
