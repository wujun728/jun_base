package com.ruoyi.common.utils.ai.RAGFlow.stream;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * 流式响应处理器
 * 用于处理RAGFlow的SSE（Server-Sent Events）流式响应
 *
 * @author RuoYi-SpringBoot3-Pro
 */
public class StreamResponseHandler {

    private StreamListener listener;
    private ExecutorService executor;
    private boolean running = false;

    public StreamResponseHandler() {
        this.executor = Executors.newCachedThreadPool();
    }

    public StreamResponseHandler(StreamListener listener) {
        this.listener = listener;
        this.executor = Executors.newCachedThreadPool();
    }

    /**
     * 设置监听器
     */
    public StreamResponseHandler setListener(StreamListener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * 设置自定义执行器
     */
    public StreamResponseHandler setExecutor(ExecutorService executor) {
        this.executor = executor;
        return this;
    }

    /**
     * 异步处理流式响应
     *
     * @param inputStream 输入流
     * @param chatId      聊天ID
     * @param sessionId   会话ID
     */
    public void handleAsync(InputStream inputStream, String chatId, String sessionId) {
        running = true;
        executor.submit(() -> {
            try {
                handle(inputStream, chatId, sessionId);
            } catch (Exception e) {
                if (listener != null) {
                    listener.onException(e);
                }
            }
        });
    }

    /**
     * 同步处理流式响应
     *
     * @param inputStream 输入流
     * @param chatId      聊天ID
     * @param sessionId   会话ID
     */
    public void handle(InputStream inputStream, String chatId, String sessionId) {
        running = true;
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        String line;
        StringBuilder currentData = new StringBuilder();
        String currentEvent = null;
        String currentId = null;

        // 通知流开始
        if (listener != null) {
            listener.onStart(chatId, sessionId);
        }

        try {
            while (running && (line = reader.readLine()) != null) {
                // 空行表示一个事件结束
                if (line.isEmpty()) {
                    if (currentData.length() > 0) {
                        processEvent(currentData.toString(), currentEvent, currentId,
                                    chatId, sessionId);
                        currentData = new StringBuilder();
                        currentEvent = null;
                        currentId = null;
                    }
                    continue;
                }

                // 解析SSE格式
                if (line.startsWith("data:")) {
                    currentData.append(line.substring(5).trim());
                } else if (line.startsWith("event:")) {
                    currentEvent = line.substring(6).trim();
                } else if (line.startsWith("id:")) {
                    currentId = line.substring(3).trim();
                } else if (line.startsWith("retry:")) {
                    // 重试时间，暂不处理
                } else if (line.startsWith(":")) {
                    // 注释行，忽略
                } else {
                    // 其他行，添加到数据中
                    currentData.append(line);
                }
            }

            // 处理最后的数据
            if (currentData.length() > 0) {
                processEvent(currentData.toString(), currentEvent, currentId, chatId, sessionId);
            }

        } catch (Exception e) {
            if (listener != null) {
                listener.onException(e);
            }
        } finally {
            running = false;
            try {
                reader.close();
            } catch (Exception ignored) {}
        }

        // 通知流完成
        if (listener != null) {
            listener.onComplete(true);
        }
    }

    /**
     * 处理单个事件
     */
    private void processEvent(String data, String eventType, String id,
                              String chatId, String sessionId) {
        StreamEvent event = new StreamEvent();
        event.setData(data);
        event.setEvent(eventType);
        event.setId(id);
        event.setChatId(chatId);
        event.setSessionId(sessionId);
        event.setRawLine(data);

        // 解析事件类型
        if ("[DONE]".equals(data.trim())) {
            event.setType(StreamEvent.EventType.DONE);
        } else if (data.startsWith("{") && data.contains("\"code\"")) {
            // 错误响应
            event.setType(StreamEvent.EventType.ERROR);
            try {
                JSONObject errorJson = JSONUtil.parseObj(data);
                String message = errorJson.getStr("message", "Unknown error");
                if (listener != null) {
                    listener.onError(message);
                }
            } catch (Exception ignored) {
                if (listener != null) {
                    listener.onError(data);
                }
            }
        } else {
            // 数据事件
            event.setType(StreamEvent.EventType.DATA);

            // 解析JSON数据
            try {
                JSONObject jsonData = JSONUtil.parseObj(data);

                // 检查是否为delta（增量数据）
                if (jsonData.containsKey("choices")) {
                    String content = parseContent(jsonData);
                    String reference = parseReference(jsonData);

                    if (content != null && !content.isEmpty()) {
                        event.setType(StreamEvent.EventType.DELTA);
                        event.setData(content);
                        if (listener != null) {
                            listener.onChunk(content, id);
                        }
                    }

                    if (reference != null && !reference.isEmpty()) {
                        event.setType(StreamEvent.EventType.REFERENCE);
                        event.setData(reference);
                        if (listener != null) {
                            listener.onReference(JSONUtil.parseObj(reference));
                        }
                    }
                }

                // 提取session_id
                if (jsonData.containsKey("session_id")) {
                    event.setSessionId(jsonData.getStr("session_id"));
                }

                // 提取message_id
                if (jsonData.containsKey("id")) {
                    event.setMessageId(jsonData.getStr("id"));
                }

            } catch (Exception e) {
                // 非JSON数据，直接作为内容返回
                if (listener != null) {
                    listener.onChunk(data, id);
                }
            }
        }

        if (listener != null) {
            listener.onEvent(event);
        }
    }

    /**
     * 解析内容
     */
    private String parseContent(JSONObject json) {
        try {
            var choices = json.getJSONArray("choices");
            if (choices != null && choices.size() > 0) {
                JSONObject choice = choices.getJSONObject(0);

                // 检查delta格式
                if (choice.containsKey("delta")) {
                    JSONObject delta = choice.getJSONObject("delta");
                    return delta.getStr("content", "");
                }

                // 检查message格式
                if (choice.containsKey("message")) {
                    JSONObject message = choice.getJSONObject("message");
                    return message.getStr("content", "");
                }

                // 检查answer格式（非流式）
                if (choice.containsKey("answer")) {
                    return choice.getStr("answer", "");
                }
            }
        } catch (Exception ignored) {}
        return null;
    }

    /**
     * 解析引用信息
     */
    private String parseReference(JSONObject json) {
        try {
            var choices = json.getJSONArray("choices");
            if (choices != null && choices.size() > 0) {
                JSONObject choice = choices.getJSONObject(0);

                // 检查delta中的reference
                if (choice.containsKey("delta")) {
                    JSONObject delta = choice.getJSONObject("delta");
                    if (delta.containsKey("reference")) {
                        return delta.getJSONObject("reference").toString();
                    }
                }

                // 检查message中的reference
                if (choice.containsKey("message")) {
                    JSONObject message = choice.getJSONObject("message");
                    if (message.containsKey("reference")) {
                        return message.getJSONObject("reference").toString();
                    }
                }

                // 检查answer中的reference（非流式）
                if (choice.containsKey("answer")) {
                    JSONObject answerObj = choice.getJSONObject("answer");
                    // answer可能是字符串或对象
                    if (answerObj != null && answerObj.containsKey("reference")) {
                        return answerObj.getJSONObject("reference").toString();
                    }
                }
            }
        } catch (Exception ignored) {}
        return null;
    }

    /**
     * 停止处理
     */
    public void stop() {
        running = false;
    }

    /**
     * 是否正在运行
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * 关闭资源
     */
    public void close() {
        stop();
        if (executor != null) {
            executor.shutdownNow();
        }
    }

    /**
     * 创建简单的回调式处理器
     *
     * @param onChunk      接收到内容片段的回调
     * @param onComplete   完成的回调
     * @param onError      错误的回调
     * @return 处理器实例
     */
    public static StreamResponseHandler create(
            Consumer<String> onChunk,
            Runnable onComplete,
            Consumer<String> onError) {

        return new StreamResponseHandler(new StreamListener() {
            @Override
            public void onEvent(StreamEvent event) {
                // 根据事件类型分发到对应的回调
                if (event == null) {
                    return;
                }
                switch (event.getType()) {
                    case DELTA:
                        if (onChunk != null) {
                            onChunk.accept(event.getData());
                        }
                        break;
                    case DONE:
                        if (onComplete != null) {
                            onComplete.run();
                        }
                        break;
                    case ERROR:
                        if (onError != null) {
                            onError.accept(event.getData());
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onChunk(String content, String chunkId) {
                if (onChunk != null) {
                    onChunk.accept(content);
                }
            }

            @Override
            public void onComplete(boolean complete) {
                if (complete && onComplete != null) {
                    onComplete.run();
                }
            }

            @Override
            public void onError(String error) {
                if (onError != null) {
                    onError.accept(error);
                }
            }

            @Override
            public void onException(Exception exception) {
                if (onError != null) {
                    onError.accept(exception.getMessage());
                }
            }
        });
    }

    /**
     * 收集所有内容片段
     *
     * @param inputStream 输入流
     * @param chatId      聊天ID
     * @param sessionId   会话ID
     * @return 收集的结果
     */
    public static StreamResult collect(InputStream inputStream, String chatId, String sessionId) {
        StreamResult result = new StreamResult();
        StreamListener listener = new StreamListener() {
            @Override
            public void onEvent(StreamEvent event) {
                // 根据事件类型分发
                if (event == null) {
                    return;
                }
                switch (event.getType()) {
                    case START:
                        result.setChatId(event.getChatId());
                        result.setSessionId(event.getSessionId());
                        break;
                    case DELTA:
                        result.appendContent(event.getData());
                        result.setChunkId(event.getId());
                        break;
                    case REFERENCE:
                        result.setReference(event.getData());
                        break;
                    case DONE:
                        result.setComplete(true);
                        break;
                    case ERROR:
                        result.setError(event.getData());
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onStart(String chatId, String sessionId) {
                result.setChatId(chatId);
                result.setSessionId(sessionId);
            }

            @Override
            public void onChunk(String content, String chunkId) {
                result.appendContent(content);
                result.setChunkId(chunkId);
            }

            @Override
            public void onReference(Object reference) {
                result.setReference(reference);
            }

            @Override
            public void onComplete(boolean complete) {
                result.setComplete(complete);
            }

            @Override
            public void onError(String error) {
                result.setError(error);
            }

            @Override
            public void onException(Exception exception) {
                result.setException(exception);
            }
        };

        StreamResponseHandler handler = new StreamResponseHandler(listener);
        handler.handle(inputStream, chatId, sessionId);
        handler.close();

        return result;
    }
}
