package com.ibiz.mq.common.producer;

import com.ibiz.mq.common.exception.ServiceException;
import com.ibiz.mq.common.message.Message;

/**
 * @auther 喻场
 * @date 2020/7/229:39
 */
public interface IProducer {
    /**
     * config#bean作为一个group
     * config#bean_businessKey作为一个topic
     * 生产者发布生产消息
     * @param instanceId mq类型实例id,主要用于查找具体类型,例如一个redis类型的instanceId,可以获取到redis连接池
     * @param bean 消费者配置 instanceId,protocol,bean 三个参数为必要参数
     * @param message 消息message,例如发送的实体
     * @param businessKey 业务关键字
     * @param serialize 序列化方式
     */
    void publisher(String instanceId, String bean, String businessKey, String serialize, Message message);
}
