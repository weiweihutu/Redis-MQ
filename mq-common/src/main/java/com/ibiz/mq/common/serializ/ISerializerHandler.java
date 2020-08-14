package com.ibiz.mq.common.serializ;


import com.ibiz.mq.common.message.Message;

/**
 * 序列化反序列化接口
 * @auther 喻场
 * @date 2020/7/2413:55
 */
public interface ISerializerHandler {

    byte[] serializer(Message message);

    Message deserializer(byte[] buf, Class<?> clazz);
}
