package com.ruoyi.redis.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import com.ruoyi.redis.annotation.RedisListener;

import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RedisListener("order.channel")
public class RedisMessageListener implements MessageListener {
    private final BlockingQueue<Message> messageQueue = new LinkedBlockingQueue<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.println("收到消息：" + message.toString());
        String channel = new String(message.getChannel());
        String body = new String(message.getBody());
        log.info("收到消息 - 频道: {}, 内容: {}", channel, body);
        messageQueue.offer(message);
    }

    public RedisMessageListener() {
        scheduler.scheduleAtFixedRate(this::processMessages, 0, 1, TimeUnit.SECONDS);
    }

    private void processMessages() {
        List<Message> messages = new ArrayList<>();
        messageQueue.drainTo(messages, 30);
        if (!messages.isEmpty()) {
            log.info("批量处理消息: {}", messages);
        }
    }

    @PreDestroy
    public void destroy() {
        scheduler.shutdown();
    }
}
