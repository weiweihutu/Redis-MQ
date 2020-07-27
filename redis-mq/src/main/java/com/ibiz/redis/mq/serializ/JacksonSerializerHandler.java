package com.ibiz.redis.mq.serializ;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibiz.mq.common.constant.ErrorCode;
import com.ibiz.mq.common.exception.ServiceException;
import com.ibiz.mq.common.message.Message;
import com.ibiz.mq.common.serializ.ISerializerHandler;
import com.ibiz.mq.common.util.ValidateUtil;

import java.io.IOException;
import java.util.Objects;

/**
 * @auther yc
 * @date 2020/7/2414:13
 */
public class JacksonSerializerHandler implements ISerializerHandler {
    private final static ObjectMapper MAPPER = new ObjectMapper();
    @Override
    public byte[] serializerAsByteArray(Message message) {
        ValidateUtil.validate(message.getBody(), Objects::isNull, ErrorCode.COMMON_CODE, "obj is null");
        try {
            return MAPPER.writeValueAsBytes(message.getBody());
        } catch (JsonProcessingException e) {
            throw new ServiceException(ErrorCode.COMMON_CODE.getCode(), "jackson serialize object :" + message.getBody().getClass() + " error", e);
        }
    }

    @Override
    public Message deserializerForByteArray(byte[] buf, Class<?> clazz) {
        try {
            return Message.MessageBuilder.creator(MAPPER.readValue(buf, clazz));
        } catch (IOException e) {
            throw new ServiceException(ErrorCode.COMMON_CODE.getCode(), "jackson deserialize object :" + clazz.getName() + " error", e);
        }
    }

    @Override
    public String serializer(Message message) {
        throw new RuntimeException("not support method");
    }

    @Override
    public Message deserializer(String str, Class<?> clazz) {
        throw new RuntimeException("not support method");
    }


}
