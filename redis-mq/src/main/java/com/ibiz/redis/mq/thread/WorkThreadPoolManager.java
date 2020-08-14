package com.ibiz.redis.mq.thread;

import com.ibiz.mq.common.config.ConsumerConfig;
import com.ibiz.mq.common.util.StringUtil;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * WORK线程池管理
 * @auther 喻场
 * @date 2020/7/2112:37
 */
public class WorkThreadPoolManager {
    private final Lock lock = new ReentrantLock();
    /**
     * consumerId 对应消费者线程池
     * key:consumerId
     * value:DefineThreadPoolExecutor
     */
    private final Map<String, DefineThreadPoolExecutor> WORK_THREAD_POOL_MANAGER = new ConcurrentHashMap<>();
    /**
     * 根据消费者id创建线程池,如果已经创建则直接返回,反之新创建一个
     * @param consumerConfig    consumerConfig
     * @return  ThreadPoolExecutor
     */
    public DefineThreadPoolExecutor createExecutor(ConsumerConfig consumerConfig) {
        lock.lock();
        try {
            DefineThreadPoolExecutor pool = WORK_THREAD_POOL_MANAGER.getOrDefault(consumerConfig.getId(),
                    new DefineThreadPoolExecutor(consumerConfig.getCorePoolSize(), consumerConfig.getMaximumPoolSize(), consumerConfig.getQueueSize()
                            , consumerConfig.getBean(), consumerConfig.getSleep()));
            WORK_THREAD_POOL_MANAGER.put(consumerConfig.getId(), pool);
            return pool;
        } finally {
            lock.unlock();
        }
    }

    public void deploy(String consumerId) {
        lock.lock();
        try {
            WORK_THREAD_POOL_MANAGER.forEach((k, v) -> {
                if (StringUtil.isBlank(consumerId) || StringUtil.equals(consumerId, k)) {
                    v.shutdown();
                }
            });
            clear(consumerId);
        } finally {
            lock.unlock();
        }
    }

    private void clear(String consumerId) {
        WORK_THREAD_POOL_MANAGER.remove(consumerId);
    }

    public static WorkThreadPoolManager getInstance() {
        return WorkThreadPoolManagerHolder.MANAGER;
    }

    public static class WorkThreadPoolManagerHolder {
        private static WorkThreadPoolManager MANAGER = new WorkThreadPoolManager();
    }
}
