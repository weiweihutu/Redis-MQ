package com.ibiz.mq.common.serializ;


import com.ibiz.mq.common.message.Message;

/**
 * 序列化反序列化接口
 * @auther yc
 * @date 2020/7/2413:55
 */
public interface ISerializerHandler {

    String serializer(Message message);

    byte[] serializerAsByteArray(Message message);

    Message deserializer(String t, Class<?> clazz);

    Message deserializerForByteArray(byte[] buf, Class<?> clazz);
}
