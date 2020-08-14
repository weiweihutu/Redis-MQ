package com.ibiz.mq.common.producer;


import com.ibiz.mq.common.ExtensionLoader;
import com.ibiz.mq.common.config.ConsumerConfig;
import com.ibiz.mq.common.constant.ErrorCode;
import com.ibiz.mq.common.exception.ServiceException;
import com.ibiz.mq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 生产者接口
 * @auther 喻场
 * @date 2020/7/229:30
 */
public class ProducerHolder {
    private static final Logger logger = LoggerFactory.getLogger(ProducerHolder.class);
    /**
     * config#bean作为一个group
     * config#bean_businessKey作为一个topic
     * 生产者发布生产消息
     * @param config 消费者配置 instanceId,protocol,bean 三个参数为必要参数
     * @param message 消息message,例如发送的实体
     * @param businessKey 业务关键字
     * @param <T> message类型
     */
    public static <T> void producer(ConsumerConfig config, Message message, String businessKey) {
        //mq 实例id
        String instanceId = config.getInstanceId();
        String protocol = config.getProtocol();
        ExtensionLoader extensionLoader = ExtensionLoader.getServiceLoader(IProducer.class);
        IProducer producer = (IProducer) extensionLoader.getInstance(protocol);
        try {
            producer.publisher(instanceId, config.getBean(), businessKey, config.getSerializer(), message);
        } catch (Exception e) {
            logger.error("producer task error", e);
            throw new ServiceException(ErrorCode.PRODUCE_ERROR, e);
        }
    }
}
