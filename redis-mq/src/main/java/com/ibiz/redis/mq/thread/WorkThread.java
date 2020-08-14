package com.ibiz.redis.mq.thread;

import com.ibiz.mq.common.ExtensionLoader;
import com.ibiz.mq.common.config.ConsumerConfig;
import com.ibiz.mq.common.consumer.IConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @auther 喻场
 * @date 2020/7/2318:11
 */
public class WorkThread implements Runnable {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String topic;
    private ConsumerConfig consumerConfig;
    public WorkThread(String topic, ConsumerConfig consumerConfig) {
        this.topic = topic;
        this.consumerConfig = consumerConfig;
    }

    @Override
    public void run() {
        logger.info("instanceId:{} work thread running", consumerConfig.getInstanceId());
        ExtensionLoader extensionLoader = ExtensionLoader.getServiceLoader(IConsumer.class);
        IConsumer consumer = (IConsumer) extensionLoader.getInstance(consumerConfig.getProtocol());
        logger.debug("instanceId:{} work thread running consumerConfig:{}", consumerConfig.getInstanceId(), consumerConfig);
        consumer.consume(consumerConfig.getInstanceId(), consumerConfig.getBean(), topic, consumerConfig.getSerializer());
    }
}
