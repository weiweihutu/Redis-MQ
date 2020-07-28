package com.ibiz.redis.mq.thread;

import com.ibiz.mq.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
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
    /**
     * consumerId 消费者id 与 BossThread
     * key : consumerId
     * value : BossThread (Runnable)
     */
    private final Map<String, BossThread> BOSS_RUNNABLE_MANAGER = new ConcurrentHashMap<>(64);
    /**
     * consumerId 消费者id 与 Thread
     * key : consumerId
     * value : Thread
     */
    private final Map<String, Thread> BOSS_THREAD_MANAGER =  new ConcurrentHashMap<>(64);
    /**
     * redis实例对应所有consumerId
     * instanceId - List&lt;consumerId&gt;
     */
    private final Map<String, Set<String>> INSTANCE_CONSUMER_MANAGER = new ConcurrentHashMap<>();

    /**
     * 注册实例与boss线程
     * @param instanceId redis实例id
     * @param bossThread boss线程
     */
    public void registry(String instanceId, String consumerId, BossThread bossThread) {
        lock.lock();
        try {
            BOSS_RUNNABLE_MANAGER.put(consumerId, bossThread);
            BOSS_THREAD_MANAGER.put(consumerId, new Thread(bossThread));
            Set<String> consumerIds = INSTANCE_CONSUMER_MANAGER.getOrDefault(instanceId, new HashSet<>());
            consumerIds.add(consumerId);
            INSTANCE_CONSUMER_MANAGER.put(instanceId, consumerIds);
        } finally {
            lock.unlock();
        }

    }

    /**
     * 启动指定redis实例BOSS线程
     */
    public void start(String instanceId) {
        lock.lock();
        try {
            INSTANCE_CONSUMER_MANAGER.forEach((k, v) -> {
                if (StringUtil.isBlank(instanceId) || StringUtil.equals(k, instanceId)) {
                    //启动redis实例下所有BOSS线程
                    v.forEach(cid -> BOSS_THREAD_MANAGER.get(cid).start());
                    logger.info("MQ instanceId :{} Boss Thread start", k);
                }
            });
        } finally {
            lock.unlock();
        }
    }

    /**
     * 开启所有所有BOSS线程
     */
    public void start() {
        start(null);
    }

    /**
     * 销毁instanceId 下的 consumerId 线程池
     * 如果consumerId 为null ,销毁instanceId下的BOSS线程，已经WORK线程池
     * 如果consumerId 为null , consumerId 为null 销毁所有BOSS线程和WORK线程池
     * @param instanceId
     * @param consumerId
     */
    public void deploy(String instanceId, String consumerId) {
        lock.lock();
        try {
            INSTANCE_CONSUMER_MANAGER.forEach((k, v) -> {
                if (StringUtil.isBlank(instanceId) || StringUtil.equals(k, instanceId)) {
                    //暂停redis实例下所有BOSS线程
                    v.forEach(cid -> BOSS_RUNNABLE_MANAGER.get(cid).shutdown());
                    logger.info("MQ instanceId :{} Boss Thread start", k);
                }
                if (StringUtil.isNotBlank(consumerId)) {
                    //销毁WORK线程池
                    WorkThreadPoolManager.getInstance().deploy(consumerId);
                }
            });
            clear(instanceId);
        } finally {
            lock.unlock();
        }
    }
    /**
     * 销毁指定redis实例线程
     * @param instanceId
     */
    public void deploy(String instanceId) {
        deploy(instanceId, null);
    }
    /**
     * 销毁所有BOSS线程
     */
    public void deploy() {
        deploy(null);
    }

    public void clear() {
        clear(null);
    }

    public void clear(String instanceId) {
        INSTANCE_CONSUMER_MANAGER.forEach((k, v) -> {
            if (StringUtil.isBlank(instanceId) || StringUtil.equals(k, instanceId)) {
                v.forEach(cid -> {
                    BOSS_RUNNABLE_MANAGER.remove(cid);
                    BOSS_THREAD_MANAGER.remove(cid);
                });
            }
        });
        if (StringUtil.isNotBlank(instanceId)) {
            INSTANCE_CONSUMER_MANAGER.remove(instanceId);
            return;
        }
        INSTANCE_CONSUMER_MANAGER.clear();
    }

    private BossThreadManager() {}
    public static BossThreadManager getInstance() {
        return BossThreadManagerHolder.MANAGER;
    }

    private static class BossThreadManagerHolder {
        static BossThreadManager MANAGER = new BossThreadManager();
    }
}
