package com.ruoyi.redis.config;

import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.ruoyi.redis.annotation.RedisListener;

@Configuration
public class RedisPubSubConfig implements ApplicationListener<ContextRefreshedEvent> {
    protected final Log log = LogFactory.getLog(this.getClass());

    @Bean
    public ThreadPoolTaskExecutor redisListenerExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("redis-listener-");
        executor.setRejectedExecutionHandler(new CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setTaskExecutor(redisListenerExecutor());
        return container;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext context = event.getApplicationContext();

        if (context.getParent() != null) {
            return;
        }

        String[] beanNames = context.getBeanNamesForAnnotation(RedisListener.class);
        RedisMessageListenerContainer container = context.getBean(RedisMessageListenerContainer.class);

        for (String beanName : beanNames) {
            Object bean = context.getBean(beanName);

            if (!(bean instanceof MessageListener)) {
                throw new IllegalStateException(
                        "@RedisListener The annotated class must implement the MessageListener interface. Bean Name: "
                                + beanName);
            }
            MessageListener listener = (MessageListener) bean;

            RedisListener annotation = bean.getClass().getAnnotation(RedisListener.class);
            String channelPattern = annotation.value();

            container.addMessageListener(listener, new PatternTopic(channelPattern));
            log.info("Registered Redis message listener [" + beanName + "] listening channel: " + channelPattern);
        }
    }
}