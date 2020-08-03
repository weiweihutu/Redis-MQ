package com.ibiz.redis.mq.serializ;

import com.ibiz.mq.common.constant.ErrorCode;
import com.ibiz.mq.common.exception.ServiceException;
import com.ibiz.mq.common.message.Message;
import com.ibiz.mq.common.serializ.ISerializerHandler;
import com.ibiz.mq.common.util.ClassUtil;
import com.ibiz.mq.common.util.RuntimeError;
import com.ibiz.mq.common.util.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @auther yc
 * @date 2020/7/2414:13
 */
public class ProtobufSerializerHandler implements ISerializerHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public byte[] serializer(Message message) {
        ValidateUtil.validate(message.getBody(), Objects::isNull, ErrorCode.COMMON_CODE, "obj is null");
        try {
            Class<?> clazz = ClassUtil.getClass(message.getClazz());
            Method method = clazz.getMethod("toByteArray");
            return (byte[])method.invoke(message.getBody());
        } catch (Exception e) {
            RuntimeError.creator( "Protobuf serializer class :" + message.getClazz() +" error ,", e);
        }
        return null;
    }

    @Override
    public Message deserializer(byte[] buf, Class<?> clazz) {
        ValidateUtil.validate(buf, Objects::isNull, ErrorCode.COMMON_CODE, "buf is null");
        ValidateUtil.validate(clazz, Objects::isNull, ErrorCode.COMMON_CODE, "clazz is null");
        try {
            Method parseFrom = clazz.getMethod("parseFrom", byte[].class);
            Object obj = parseFrom.invoke(clazz, buf);
            return Message.MessageBuilder.creator(obj);
        } catch (Exception e) {
            RuntimeError.creator("Protobuf deserializer class " + clazz.getName() +" error", e);
        }
        return null;
    }

}
