package com.ibiz.redis.mq.serializ;


import com.ibiz.mq.common.constant.ErrorCode;
import com.ibiz.mq.common.message.Message;
import com.ibiz.mq.common.serializ.ISerializerHandler;
import com.ibiz.mq.common.util.GsonUtil;
import com.ibiz.mq.common.util.ValidateUtil;

import java.util.Objects;

/**
 * @auther yc
 * @date 2020/7/2414:13
 */
public class GsonSerializerHandler implements ISerializerHandler {
    @Override
    public byte[] serializerAsByteArray(Message message) {
        return serializer(message).getBytes();
    }

    @Override
    public Message deserializerForByteArray(byte[] buf, Class<?> clazz) {
        String json = new String(buf);
        return deserializer(json, clazz);
    }

    @Override
    public String serializer(Message message) {
        ValidateUtil.validate(message.getBody(), Objects::isNull, ErrorCode.COMMON_CODE, "obj is null");
        return GsonUtil.getGsonInstance().toJson(message.getBody());
    }

    @Override
    public Message deserializer(String str, Class<?> clazz) {
        ValidateUtil.validate(str, (o) -> Objects.isNull(o) || "".equals(o.trim()), ErrorCode.COMMON_CODE, "str is null");
        ValidateUtil.validate(clazz, Objects::isNull, ErrorCode.COMMON_CODE, "clazz is null");
        Object t = GsonUtil.getGsonInstance().fromJson(str, clazz);
        return Message.MessageBuilder.creator(t);
    }

}
