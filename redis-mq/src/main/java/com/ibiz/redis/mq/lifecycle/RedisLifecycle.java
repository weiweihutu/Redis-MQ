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
import redis.clients.jedis.JedisPool;

/**
 * @auther yc
 * @date 2020/7/1612:43
 */
public class RedisLifecycle implements Lifecycle {
    private Holder<JedisPool> holder;
    @Override
    public void init() {
        JedisPool jedisPool = JedisClient.JedisPoolCreator.getInstance(this.instanceConfig);
        holder = new Holder<>(jedisPool);
        MQConfig.CONSUMER_CONFIG.forEach((id, consumerConfig) -> {
            //只关注当前实例下的消费者
            if (instanceConfig.getInstanceId().equals(consumerConfig.getInstanceId())) {
                BossThread bossThread = MQThreadFactory.getBossThread(consumerConfig);
                BossThreadManager.getInstance().registry(id, bossThread);
            }
        });
    }
    @Override
    public void start() {
        BossThreadManager.getInstance().start();
    }

    @Override
    public void deploy() {
        BossThreadManager.getInstance().deploy();
        WorkThreadPoolManager.getInstance().deploy();
    }
    
    public static class Holder<JedisPool> {
        final JedisPool value;
        public JedisPool get() {
            return value;
        }
        public Holder(JedisPool value) {
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
