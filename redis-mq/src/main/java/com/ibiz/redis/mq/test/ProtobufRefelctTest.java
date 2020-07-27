package com.ibiz.redis.mq.test;

import com.ibiz.mq.common.message.Message;
import com.ibiz.redis.mq.domain.CopyUserProto;
import com.ibiz.redis.mq.domain.UserProto;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @auther yc
 * @date 2020/7/2515:24
 */
public class ProtobufRefelctTest {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        UserProto.User user = UserFactory.getUser();
        byte[] bytes = user.toByteArray();
        Message message = new Message();
        message.setBody(user);
        //com.ibiz.redis.mq.domain.UserProto.User
        //Class<?> aClass = Class.forName(message.getClazz());
        Class<?> aClass = CopyUserProto.CopyUser.class;
        Method parseFrom = aClass.getMethod("parseFrom", byte[].class);
        Object obj = parseFrom.invoke(aClass, bytes);
        System.out.println(obj);
        System.out.println(((CopyUserProto.CopyUser)obj));
    }
}
