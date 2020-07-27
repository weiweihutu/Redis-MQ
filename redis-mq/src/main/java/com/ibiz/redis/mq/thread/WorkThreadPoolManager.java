package com.ibiz.redis.mq.thread;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @auther yc
 * @date 2020/7/2112:37
 */
public class WorkThreadPoolManager {

    private final Map<String, DefineThreadPoolExecutor> WORK_THREAD_POOL_MANAGER = new ConcurrentHashMap<>();

    /**
     * 根据消费者id创建线程池,如果已经创建则直接返回,反之新创建一个
     * @param bean    消费者id
     * @param corePoolSize  核心线程数
     * @param maximumPoolSize   最大线程数
     * @param queueSize 队列最大容量
     * @return  ThreadPoolExecutor
     */
    public DefineThreadPoolExecutor createExecutor(String bean, int corePoolSize, int maximumPoolSize, int queueSize, long sleep) {
        DefineThreadPoolExecutor pte = WORK_THREAD_POOL_MANAGER.get(bean);
        if (Objects.isNull(pte)) {
            synchronized (WORK_THREAD_POOL_MANAGER) {
                pte = WORK_THREAD_POOL_MANAGER.get(bean);
                if (Objects.isNull(pte)) {
                    pte = new DefineThreadPoolExecutor(corePoolSize, maximumPoolSize, queueSize, bean, sleep);
                    WORK_THREAD_POOL_MANAGER.putIfAbsent(bean, pte);
                }
            }
        }
        return pte;
    }

    public void remove(String bean) {
        WORK_THREAD_POOL_MANAGER.remove(bean);
    }

    public void deploy() {
        WORK_THREAD_POOL_MANAGER.forEach((k, v) -> v.shutdown());
        clear();
    }
    private void clear() {
        WORK_THREAD_POOL_MANAGER.clear();
    }

    public static WorkThreadPoolManager getInstance() {
        return WorkThreadPoolManagerHolder.MANAGER;
    }
    public static class WorkThreadPoolManagerHolder {
        private static WorkThreadPoolManager MANAGER = new WorkThreadPoolManager();
    }
}
