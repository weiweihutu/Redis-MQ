package com.ibiz.redis.mq.serializ;


import com.ibiz.mq.common.constant.ErrorCode;
import com.ibiz.mq.common.message.Message;
import com.ibiz.mq.common.serializ.ISerializerHandler;
import com.ibiz.mq.common.util.GsonUtil;
import com.ibiz.mq.common.util.ValidateUtil;

import java.util.Objects;

/**
 * @auther 喻场
 * @date 2020/7/2414:13
 */
public class GsonSerializerHandler implements ISerializerHandler {
    @Override
    public byte[] serializer(Message message) {
        ValidateUtil.validate(message.getBody(), Objects::isNull, ErrorCode.COMMON_CODE, "obj is null");
        return GsonUtil.getGsonInstance().toJson(message.getBody()).getBytes();
    }

    @Override
    public Message deserializer(byte[] buf, Class<?> clazz) {
        String json = new String(buf);
        ValidateUtil.validate(json, (o) -> Objects.isNull(o) || "".equals(o.trim()), ErrorCode.COMMON_CODE, "str is null");
        ValidateUtil.validate(clazz, Objects::isNull, ErrorCode.COMMON_CODE, "clazz is null");
        Object t = GsonUtil.getGsonInstance().fromJson(json, clazz);
        return Message.MessageBuilder.creator(t);
    }

}
