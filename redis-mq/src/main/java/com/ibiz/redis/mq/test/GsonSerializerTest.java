package com.ibiz.redis.mq.test;

import com.ibiz.mq.common.ExtensionLoader;
import com.ibiz.mq.common.message.Message;
import com.ibiz.mq.common.serializ.ISerializerHandler;
import com.ibiz.redis.mq.domain.UserModel;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auther yc
 * @date 2020/7/2415:45
 */
public class GsonSerializerTest {

    @Test
    public void objectTest() {
        UserModel user = UserFactory.getJdkUser();
        Message message = new Message();
        message.setBody(user);
        ExtensionLoader extensionLoader = ExtensionLoader.getServiceLoader(ISerializerHandler.class);
        ISerializerHandler protobuf = (ISerializerHandler)extensionLoader.getInstance("gson");
        String serializer = protobuf.serializer(message);
        Message deserializer = protobuf.deserializer(serializer, UserModel.class);
        System.out.println(message);
        System.out.println(deserializer);
    }

    @Test
    public void mapTest() {
        Message msg = new Message();
        Map map1 = new HashMap();
        map1.put("a", 2);
        UserModel user = UserFactory.getJdkUser();
        map1.put("b", user);
        msg.setBody(map1);
        ExtensionLoader extensionLoader = ExtensionLoader.getServiceLoader(ISerializerHandler.class);
        ISerializerHandler protobuf = (ISerializerHandler)extensionLoader.getInstance("gson");
        byte[] buff2 = protobuf.serializerAsByteArray(msg);
        Message msg2 = protobuf.deserializerForByteArray(buff2, HashMap.class);
        System.out.println(msg);
        System.out.println(msg2);
    }

    @Test
    public void listTest() {
        Message msg = new Message();
        List list1 = new ArrayList();
        UserModel user = UserFactory.getJdkUser();
        list1.add(user);
        list1.add(user);
        msg.setBody(list1);
        ExtensionLoader extensionLoader = ExtensionLoader.getServiceLoader(ISerializerHandler.class);
        ISerializerHandler protobuf = (ISerializerHandler)extensionLoader.getInstance("gson");

        byte[] buff2 = protobuf.serializerAsByteArray(msg);
        Message msg2 = protobuf.deserializerForByteArray(buff2, ArrayList.class);
        System.out.println(list1);
        System.out.println(msg2);
    }

    @Test
    public void arrTest() {
        Message msg = new Message();
        UserModel[] arr = new UserModel[2];
        UserModel user = UserFactory.getJdkUser();
        arr[0] = user;
        arr[1] = user;
        msg.setBody(arr);
        ExtensionLoader extensionLoader = ExtensionLoader.getServiceLoader(ISerializerHandler.class);
        ISerializerHandler protobuf = (ISerializerHandler)extensionLoader.getInstance("gson");

        byte[] buff2 = protobuf.serializerAsByteArray(msg);
        Message msg2 = protobuf.deserializerForByteArray(buff2, UserModel[].class);
        System.out.println(msg);
        System.out.println(msg2);
    }
}
