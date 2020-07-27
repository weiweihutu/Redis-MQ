package com.ibiz.redis.mq.factory;

import com.ibiz.mq.common.config.ConsumerConfig;
import com.ibiz.mq.common.config.InstanceConfig;
import com.ibiz.redis.mq.config.RedisConfig;

public class ConfigFactory {

    public static InstanceConfig getNewInstanceConfig(String type) {
        if ("redis".equals(type)) {
            return new RedisConfig();
        }
        throw new RuntimeException("not support mq type :" + type);
    }

    public static ConsumerConfig getNewConsumerConfig() {
        return new ConsumerConfig();
    }
}
