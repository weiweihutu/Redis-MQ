package com.ibiz.redis.mq.lifecycle;

import com.ibiz.mq.common.config.InstanceConfig;
import com.ibiz.mq.common.config.MQConfig;
import com.ibiz.mq.common.lifecycle.Lifecycle;
import com.ibiz.redis.mq.config.RedisConfig;
import com.ibiz.redis.mq.factory.MQThreadFactory;
import com.ibiz.redis.mq.thread.BossThread;
import com.ibiz.redis.mq.thread.BossThreadManager;
import com.ibiz.redis.mq.thread.WorkThreadPoolManager;
import com.ibiz.redis.mq.client.JedisClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.util.Pool;

/**
 * @auther 喻场
 * @date 2020/7/1612:43
 */
public class RedisLifecycle implements Lifecycle {
    private Holder<Pool<Jedis>> holder;
    @Override
    public void init() {
        Pool<Jedis> jedisPool = JedisClient.JedisPoolCreator.getInstance(this.instanceConfig);
        holder = new Holder<>(jedisPool);
        MQConfig.CONSUMER_CONFIG.forEach((id, consumerConfig) -> {
            if (instanceConfig.getInstanceId().equals(consumerConfig.getInstanceId())) {
                BossThread bossThread = MQThreadFactory.getBossThread(consumerConfig);
                BossThreadManager.getInstance().registry(instanceConfig.getInstanceId(), id , bossThread);
            }
        });
    }
    @Override
    public void start() {
        BossThreadManager.getInstance().start(instanceConfig.getInstanceId());
    }

    @Override
    public void deploy() {
        BossThreadManager.getInstance().deploy(instanceConfig.getInstanceId());
        WorkThreadPoolManager.getInstance().deploy(instanceConfig.getInstanceId());
    }
    
    public static class Holder<T> {
        final T value;
        public T get() {
            return value;
        }
        public Holder(T value) {
            this.value = value;
        }
    }

    public Jedis getJedis() {
        return holder.get().getResource();
    }

    public void setInstanceConfig(InstanceConfig instanceConfig) {
        this.instanceConfig = (RedisConfig)instanceConfig;
    }

    private RedisConfig instanceConfig;
}
