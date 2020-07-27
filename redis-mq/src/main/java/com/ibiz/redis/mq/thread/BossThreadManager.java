package com.ibiz.redis.mq.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Boss线程管理
 * 管理consumer主线程
 */
public class BossThreadManager {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    Lock lock = new ReentrantLock();

    private Map<String, BossThread> BOSS_RUNNABLE_MANAGER = new ConcurrentHashMap<>(64);
    private Map<String, Thread> BOSS_THREAD_MANAGER =  new ConcurrentHashMap<>(64);
    //TODO服务治理功能使用，根据修改的实例对线程池做管理
    private Map<String, List<DefineThreadPoolExecutor>> INSTANCE_WORK_THREAD_POOL_MANAGER = new ConcurrentHashMap<>(64);

    public void registry(String instanceId, BossThread bossThread) {
        BOSS_RUNNABLE_MANAGER.putIfAbsent(instanceId, bossThread);
        BOSS_THREAD_MANAGER.put(instanceId, new Thread(bossThread));
    }

    public void registry(String instanceId, DefineThreadPoolExecutor workThreadPool) {
        List<DefineThreadPoolExecutor> executors = INSTANCE_WORK_THREAD_POOL_MANAGER.getOrDefault(instanceId, new ArrayList<>());
        executors.add(workThreadPool);
        INSTANCE_WORK_THREAD_POOL_MANAGER.put(instanceId, executors);
    }

    /**
     * 启动所有BOSS线程
     */
    public void start() {
        lock.lock();
        try {
            BOSS_THREAD_MANAGER.forEach((k, v) -> {
                v.start();
                logger.info("MQ instanceId :{} Boss Thread start", k);
            });
        } finally {
            lock.unlock();
        }
    }

    /**
     * 销毁所有BOSS线程
     */
    public void deploy() {
        lock.lock();
        try {
            BOSS_RUNNABLE_MANAGER.forEach((k, v) -> {
                //把运行状态关闭，关闭BOSS线程
                v.shutdown();
            });
            clear();
        } finally {
            lock.unlock();
        }
    }

    private void clear() {
        BOSS_RUNNABLE_MANAGER.clear();
        BOSS_THREAD_MANAGER.clear();
        INSTANCE_WORK_THREAD_POOL_MANAGER.clear();
    }

    public void remove(String instanceId) {
        BOSS_RUNNABLE_MANAGER.remove(instanceId);
        BOSS_THREAD_MANAGER.remove(instanceId);
    }

    private BossThreadManager() {}
    public static BossThreadManager getInstance() {
        return BossThreadManagerHolder.MANAGER;
    }

    private static class BossThreadManagerHolder {
        static BossThreadManager MANAGER = new BossThreadManager();
    }
}
