package com.ibiz.redis.mq.serializ;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibiz.mq.common.constant.ErrorCode;
import com.ibiz.mq.common.exception.ServiceException;
import com.ibiz.mq.common.message.Message;
import com.ibiz.mq.common.serializ.ISerializerHandler;
import com.ibiz.mq.common.util.RuntimeError;
import com.ibiz.mq.common.util.ValidateUtil;

import java.io.IOException;
import java.util.Objects;

/**
 * @auther 喻场
 * @date 2020/7/2414:13
 */
public class JacksonSerializerHandler implements ISerializerHandler {
    private final static ObjectMapper MAPPER = new ObjectMapper();
    @Override
    public byte[] serializer(Message message) {
        ValidateUtil.validate(message.getBody(), Objects::isNull, ErrorCode.COMMON_CODE, "obj is null");
        try {
            return MAPPER.writeValueAsBytes(message.getBody());
        } catch (JsonProcessingException e) {
            RuntimeError.creator( "jackson serialize object :" + message.getBody().getClass() + " error", e);
        }
        return null;
    }

    @Override
    public Message deserializer(byte[] buf, Class<?> clazz) {
        try {
            return Message.MessageBuilder.creator(MAPPER.readValue(buf, clazz));
        } catch (IOException e) {
            RuntimeError.creator( "jackson deserialize object :" + clazz.getName() + " error", e);
        }
        return null;
    }

}
