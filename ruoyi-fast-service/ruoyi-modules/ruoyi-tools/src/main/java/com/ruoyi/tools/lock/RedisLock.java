package com.ruoyi.tools.lock;

import com.ruoyi.common.core.exception.base.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 * @author wocurr.com
 */
@Slf4j
@Component
public class RedisLock {
    @Autowired
    private RedissonClient redissonClient;

    /**
     * 加锁处理
     * @param key 锁实例
     * @param function 处理逻辑函数
     */
    public boolean doLock(String key, Function function) {
        RLock lock = redissonClient.getLock(key);
        if (lock.tryLock()) {
            try {
                function.execute();
            } finally {
                lock.unlock();
            }
            return true;
        }
        return false;
    }

    /**
     * 加锁处理
     * @param key 锁实例
     * @param timeout 等待时间
     * @param unit 时间粒度
     * @param function 处理逻辑函数
     */
    public boolean doLock(String key, long timeout, TimeUnit unit, Function function) throws InterruptedException {
        RLock lock = redissonClient.getLock(key);
        if (lock.tryLock(timeout, unit)) {
            try {
                function.execute();
            } finally {
                lock.unlock();
            }
            return true;
        }
        return false;
    }

    /**
     * 加锁处理
     * @param key 锁实例
     * @param waitTime 等待时间
     * @param leaseTime 超时自动释放锁时间
     * @param unit 时间粒度
     * @param function 处理逻辑函数
     */
    public boolean doLock(String key, long waitTime, long leaseTime, TimeUnit unit, Function function) throws InterruptedException {
        RLock lock = redissonClient.getLock(key);
        if (lock.tryLock(waitTime, leaseTime, unit)) {
            try {
                function.execute();
            } finally {
                lock.unlock();
            }
            return true;
        }
        return false;
    }

    /**
     * 获取锁
     * @param lockKey 锁实例key
     * @return 锁信息
     */
    public RLock getRLock(String lockKey) {
        return redissonClient.getLock(lockKey);
    }

    /**
     * 获取锁
     * @param key
     * @param waitTime 等待时间
     * @param leaseTime 锁超时时间
     * @param unit
     * @return
     */
    public boolean tryLock(String key, long waitTime, long leaseTime, TimeUnit unit) {
        if(StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("key 不能为空");
        }
        RLock lock = redissonClient.getLock(key);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            throw new BaseException("加锁失败！");
        }
    }

    /**
     * 加锁
     * @param lockKey 锁实例key
     * @return 锁信息
     */
    public RLock lock(String lockKey) {
        RLock lock = getRLock(lockKey);
        lock.lock();
        return lock;
    }

    /**
     * 释放锁
     * @param lockKey 锁实例key
     */
    public void unlock(String lockKey) {
        try {
            RLock lock = getRLock(lockKey);
            lock.unlock();
        } catch (Exception e) {
            log.error("释放锁失败！", e);
        }
    }

    @FunctionalInterface
    public interface Function {
        void execute();
    }
}
