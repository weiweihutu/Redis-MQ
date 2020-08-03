package com.ibiz.redis.mq.serializ;


import com.ibiz.mq.common.constant.ErrorCode;
import com.ibiz.mq.common.exception.ServiceException;
import com.ibiz.mq.common.message.Message;
import com.ibiz.mq.common.serializ.ISerializerHandler;
import com.ibiz.mq.common.util.RuntimeError;
import com.ibiz.mq.common.util.ValidateUtil;

import java.io.*;
import java.util.Objects;

/**
 * @auther yc
 * @date 2020/7/2414:13
 */
public class JdkSerializerHandler implements ISerializerHandler {
    @Override
    public byte[] serializer(Message message) {
        ValidateUtil.validate(message.getBody(), Objects::isNull, ErrorCode.COMMON_CODE, "obj is null");
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)){
            oos.writeObject(message.getBody());
            return baos.toByteArray();
        } catch (IOException e) {
            RuntimeError.creator( "jdk serialize object :" + message.getBody().getClass() + " error", e);
        }
        return null;
    }

    @Override
    public Message deserializer(byte[] buf, Class<?> clazz) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(buf);
             ObjectInputStream ois = new ObjectInputStream(bais)){
            return Message.MessageBuilder.creator(ois.readObject());
        } catch (Exception e) {
            RuntimeError.creator("jdk deserialize object :" + clazz.getName() + " error", e);
        }
        return null;
    }

}
