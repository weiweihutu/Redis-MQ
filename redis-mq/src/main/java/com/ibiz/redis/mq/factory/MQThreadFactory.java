package com.ibiz.redis.mq.factory;

import com.ibiz.mq.common.config.ConsumerConfig;
import com.ibiz.redis.mq.thread.BossThread;

public class MQThreadFactory {

    public static BossThread getBossThread(ConsumerConfig consumerConfig) {
        return new BossThread(consumerConfig);
    }
}
