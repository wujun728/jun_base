package com.ruoyi.common.utils.ai;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.core.http.StreamResponse;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionAssistantMessageParam;
import com.openai.models.chat.completions.ChatCompletionChunk;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class OpenAI {
    /**
     * 会话参数
     *
     * @param model
     * @param messageList
     * @return
     */
    public static ChatCompletionCreateParams chatParams(String model, List<AIMessage> messageList) {
        ChatCompletionCreateParams.Builder builder = ChatCompletionCreateParams.builder();

        for (AIMessage message : messageList) {
            if (AIRole.USER.equals(message.getRole())) {
                builder.addUserMessage(message.getMessage());
            } else if (AIRole.ASSISTANT.equals(message.getRole())) {
                builder.addMessage(ChatCompletionAssistantMessageParam.builder().content(message.getMessage()).build());
            } else if (AIRole.SYSTEM.equals(message.getRole())) {
                builder.addSystemMessage(message.getMessage());
            }
        }

        return builder.model(model).build();
    }

    /**
     * 会话客户端
     *
     * @param apiKey
     * @param baseUrl
     * @param proxy
     * @return
     */
    /**
     * 代理示例：
     * new Proxy(
     * Proxy.Type.HTTP, new InetSocketAddress(
     * "10.60.127.10", 8080
     * )
     * )
     */
    public static OpenAIClient chatClient(String apiKey, String baseUrl, Proxy proxy) {
        OpenAIOkHttpClient.Builder builder = OpenAIOkHttpClient.builder();

        builder.apiKey(apiKey)
                .baseUrl(baseUrl);

        if (proxy != null) {
            builder.proxy(proxy);
        }

        return builder.build();
    }

    /**
     * 同步会话
     *
     * @param client
     * @param params
     * @return
     */
    public static String chat(OpenAIClient client, ChatCompletionCreateParams params) {
        ChatCompletion chatCompletion = client.chat().completions().create(params);

        return chatCompletion.choices().get(0).message().content().get();
    }

    /**
     * 异步会话
     *
     * @param client
     * @param params
     * @return
     */
    /**
     * 示例：
     * OpenAI.chatStream(client, params, new OpenAI.StreamContentListener() {
     *
     * @Override public void onContent(String content) {
     * // 每收到一段增量内容时会被调用
     * System.out.print(content); // 实时输出
     * }
     * @Override public void onComplete(String fullContent) {
     * // 全部内容流式结束后会被调用
     * System.out.println("\n全部内容：");
     * System.out.println(fullContent);
     * }
     * });
     */
    public static ResponseEntity<StreamingResponseBody> chatStream(OpenAIClient client, ChatCompletionCreateParams params, StreamContentListener listener) {
        StreamingResponseBody streamingResponseBody = outputStream -> {
            try {
                StreamResponse<ChatCompletionChunk> chatCompletion = client.chat().completions().createStreaming(params);
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
                StringBuilder fullContent = new StringBuilder();
                chatCompletion.stream().forEach(chunk -> {
                    try {
                        if (chunk.choices() != null && !chunk.choices().isEmpty()
                                && chunk.choices().get(0).delta() != null
                                && chunk.choices().get(0).delta().content() != null) {

                            String content = chunk.choices().get(0).delta().content().get();
                            writer.write(content);
                            writer.flush();
                            fullContent.append(content);
                            if (listener != null) listener.onContent(content);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                writer.write("[DONE]");
                writer.close();
                if (listener != null) listener.onComplete(fullContent.toString());

            } catch (Exception e) {
                e.printStackTrace();
                try {
                    outputStream.write(("Error: " + e.getMessage()).getBytes(StandardCharsets.UTF_8));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        };

        return ResponseEntity.ok()
                .header("Content-Type", "text/plain; charset=utf-8")
                .header("Cache-Control", "no-cache")
                .header("Connection", "keep-alive")
                .body(streamingResponseBody);
    }

    public interface StreamContentListener {
        void onContent(String content); // 每次增量

        void onComplete(String fullContent); // 全部内容
    }
}
