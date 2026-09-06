package com.ruoyi.tools.utils.pools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 线程池
 *
 * @author wocurr.com
 */
public class DataPassPools {

    private static class NamedThreadFactory implements ThreadFactory {
        private final AtomicInteger threadNumber = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "data-pass-pool-" + threadNumber.getAndIncrement());
            t.setDaemon(false);
            t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }

    public static final ExecutorService pool = new ThreadPoolExecutor(
            10,                // 核心线程数
            500,                           // 最大线程数
            60L,                          // 空闲线程存活时间
            TimeUnit.SECONDS,             // 时间单位
            new LinkedBlockingQueue<>(500), // 小队列容量
            new NamedThreadFactory(),     // 线程工厂
            new ThreadPoolExecutor.DiscardPolicy() // 丢弃策略
    );

    private DataPassPools() {
        throw new AssertionError("工具类不允许实例化");
    }
}
