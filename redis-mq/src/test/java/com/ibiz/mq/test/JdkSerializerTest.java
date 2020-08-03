package com.ibiz.mq.test;

import com.ibiz.mq.common.ExtensionLoader;
import com.ibiz.mq.common.message.Message;
import com.ibiz.mq.common.serializ.ISerializerHandler;
import com.ibiz.redis.mq.domain.UserModel;
import org.junit.Test;

/**
 * @auther yc
 * @date 2020/7/2715:34
 */
public class JdkSerializerTest {

    @Test
    public void objestTest() {
        objectTest("jdk");
        objectTest("jackson");
        objectTest("gson");
    }


    public void objectTest(String serialize) {
        UserModel user = UserFactory.getJdkUser();
        Message message = new Message();
        message.setBody(user);
        ExtensionLoader extensionLoader = ExtensionLoader.getServiceLoader(ISerializerHandler.class);
        ISerializerHandler protobuf = (ISerializerHandler)extensionLoader.getInstance(serialize);
        byte[] buf = protobuf.serializer(message);
        Message deserializer = protobuf.deserializer(buf, UserModel.class);
        System.out.println(message);
        System.out.println(deserializer);
    }


}
