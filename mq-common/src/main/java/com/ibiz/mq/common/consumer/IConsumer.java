package com.ibiz.mq.common.consumer;

/**
 * @auther yc
 * @date 2020/7/2219:17
 */
public interface IConsumer {

    /**
     * config#bean作为一个group
     * config#bean_businessKey作为一个topic
     * 消费消息
     * @param instanceId mq类型实例id,主要用于查找具体类型,例如一个redis类型的instanceId,可以获取到redis连接池
     * @param bean 消费者配置 instanceId,protocol,bean 三个参数为必要参数
     * @param topic bean_业务关键字
     * @param serialize 实体对象序列化方式
     */
    void consume(String instanceId, String bean, String topic, String serialize);
}
