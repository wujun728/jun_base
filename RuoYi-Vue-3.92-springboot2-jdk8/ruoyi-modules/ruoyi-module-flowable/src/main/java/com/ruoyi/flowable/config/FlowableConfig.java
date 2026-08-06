package com.ruoyi.flowable.config;

import java.util.concurrent.ThreadPoolExecutor;

import org.flowable.engine.impl.db.DbIdGenerator;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class FlowableConfig implements EngineConfigurationConfigurer<SpringProcessEngineConfiguration> {
    @Override
    public void configure(SpringProcessEngineConfiguration engineConfiguration) {
        engineConfiguration.setActivityFontName("宋体");
        engineConfiguration.setLabelFontName("宋体");
        engineConfiguration.setAnnotationFontName("宋体");
        engineConfiguration.setIdGenerator(new DbIdGenerator());
    }

    @Bean("applicationTaskExecutor")
    public ThreadPoolTaskExecutor applicationTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 此方法返回可用处理器的虚拟机的最大数量; 不小于1
        int core = Runtime.getRuntime().availableProcessors();
        executor.setCorePoolSize(core);// 设置核心线程数
        executor.setMaxPoolSize(core * 2 + 1);// 设置最大线程数
        executor.setKeepAliveSeconds(120);// 除核心线程外的线程存活时间
        executor.setQueueCapacity(120);// 如果传入值大于0，底层队列使用的是LinkedBlockingQueue,否则默认使用SynchronousQueue
        executor.setThreadNamePrefix("thread-default-execute");// 线程名称前缀
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());// 设置拒绝策略，抛出
                                                                                   // RejectedExecutionException来拒绝新任务的处理。
        // executor.setRejectedExecutionHandler(new
        // ThreadPoolExecutor.CallerRunsPolicy());//设置拒绝策略，使用主线程
        // executor.setRejectedExecutionHandler(new
        // ThreadPoolExecutor.DiscardPolicy());//设置拒绝策略，直接丢弃掉
        // executor.setRejectedExecutionHandler(new
        // ThreadPoolExecutor.DiscardOldestPolicy());//设置拒绝策略，丢弃最早的未处理的任务请求。
        return executor;
    }
}
