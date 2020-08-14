package com.ibiz.mq.test;

import com.google.protobuf.InvalidProtocolBufferException;
import com.ibiz.mq.common.ExtensionLoader;
import com.ibiz.mq.common.message.Message;
import com.ibiz.mq.common.serializ.ISerializerHandler;
import com.ibiz.redis.mq.domain.UserModel;
import com.ibiz.redis.mq.domain.UserProto;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * @auther 喻场
 * @date 2020/7/2115:29
 */
public class ProtobufSerializerTest {
    @Test
    public void objectTest() throws InvalidProtocolBufferException {
        UserProto.User user = UserFactory.getUser();
        Message message = new Message();
        message.setBody(user);
        ExtensionLoader extensionLoader = ExtensionLoader.getServiceLoader(ISerializerHandler.class);
        ISerializerHandler protobuf = (ISerializerHandler)extensionLoader.getInstance("protobuf");
        byte[] buff = protobuf.serializer(message);
        Message message1 = protobuf.deserializer(buff, UserProto.User.class);
        System.out.println(message);
        System.out.println(message1);
    }

    @Test
    public void timecost() {
        UserModel userModel = UserFactory.getJdkUser();
        UserProto.User user = UserFactory.getUser();
        Map<String, Long> cost = new HashMap<>();
        int cycle = 1000;
        int time = 1000;
        IntStream.range(0, cycle).forEach(
            i -> {
                protobufCost(user, cost, time);
                gsonCost(userModel, cost, time);
                jdkCost(userModel, cost, time);
                jacksonCost(userModel, cost, time);
            }
        );
        System.out.println("protobuf Object size " + ObjectSizeCalculator.getObjectSize(user) + " * (" + cycle +  " * " + time +") 序列化大小 " + cost.get("protobuf_serialize_size") +" , 序列化cost :" + cost.get("protobuf_serialize") + " 反序列化cost:" + cost.get("protobuf_deserialize"));
        System.out.println("jdk Object size " + ObjectSizeCalculator.getObjectSize(userModel) + " * (" + cycle + " * " + time + ") 序列化大小 " + cost.get("jdk_serialize_size") +" , 序列化cost :" + cost.get("jdk_serialize") + " 反序列化cost:" + cost.get("jdk_deserialize"));
        System.out.println("gson Object size " + ObjectSizeCalculator.getObjectSize(userModel) + " * (" + cycle + " * " + time + ") 序列化大小 " + cost.get("gson_serialize_size") +" , 序列化cost :" + cost.get("gson_serialize") + " 反序列化cost:" + cost.get("gson_deserialize"));
        System.out.println("jackson Object size " + ObjectSizeCalculator.getObjectSize(userModel) + " * (" + cycle + " * " + time + ") 序列化大小 " + cost.get("jackson_serialize_size") +" , 序列化cost :" + cost.get("jackson_serialize") + " 反序列化cost:" + cost.get("jackson_deserialize"));
    }

    private void protobufCost(UserProto.User user, Map<String, Long> cost, int range) {
        Message message = new Message();
        message.setBody(user);
        ExtensionLoader extensionLoader = ExtensionLoader.getServiceLoader(ISerializerHandler.class);
        ISerializerHandler protobuf = (ISerializerHandler)extensionLoader.getInstance("protobuf");
        List<byte[]> byts = new ArrayList<>();
        long start = System.currentTimeMillis();
        IntStream.range(0, range).forEach(i -> {
            byte[] bytes = protobuf.serializer(message);
            byts.add(bytes);
            long length = cost.getOrDefault("protobuf_serialize_size", 0L);
            length += (long)bytes.length;
            cost.put("protobuf_serialize_size", length);
        });
        //System.out.println(user);
        long time = cost.getOrDefault("protobuf_serialize", 0L);
        long end = System.currentTimeMillis();
        time += (end - start);
        cost.put("protobuf_serialize", time);
        byts.forEach(b -> {
            protobuf.deserializer(b, UserProto.User.class);
            /*JsonFormat jsonFormat = new JsonFormat();
            String json = jsonFormat.printToString(user1);
            System.out.println(">>>>>>>>>>>>>>>>" + json);*/
        });
        long deTime = cost.getOrDefault("protobuf_deserialize", 0L);
        long end2 = System.currentTimeMillis();
        deTime += (end2 - end);
        cost.put("protobuf_deserialize", deTime);
    }

    private void gsonCost(UserModel user, Map<String, Long> cost, int range) {
        Message message = new Message();
        message.setBody(user);
        ExtensionLoader extensionLoader = ExtensionLoader.getServiceLoader(ISerializerHandler.class);
        ISerializerHandler protobuf = (ISerializerHandler)extensionLoader.getInstance("gson");
        List<String> list = new ArrayList<>();
        long start = System.currentTimeMillis();
        IntStream.range(0, range).forEach(i -> {
            String str = new String(protobuf.serializer(message));
            list.add(str);
            long length = cost.getOrDefault("gson_serialize_size", 0L);
            length += (long)str.length();
            cost.put("gson_serialize_size", length);
        });
        long end = System.currentTimeMillis();
        long time = cost.getOrDefault("gson_serialize", 0L);
        time += (end - start);
        cost.put("gson_serialize", time);
        list.forEach(b -> {
            Message message1 = protobuf.deserializer(b.getBytes(), Message.class);
        });
        long deTime = cost.getOrDefault("gson_deserialize", 0L);
        long end2 = System.currentTimeMillis();
        deTime += (end2 - end);
        cost.put("gson_deserialize", deTime);
    }

    public void jdkCost(UserModel user, Map<String, Long> cost, int range) {
        Message message = new Message();
        message.setBody(user);
        ExtensionLoader extensionLoader = ExtensionLoader.getServiceLoader(ISerializerHandler.class);
        ISerializerHandler protobuf = (ISerializerHandler)extensionLoader.getInstance("jdk");
        List<byte[]> byts = new ArrayList<>();
        long start = System.currentTimeMillis();
        IntStream.range(0, range).forEach(i -> {
            byte[] bytes = protobuf.serializer(message);
            byts.add(bytes);
            long length = cost.getOrDefault("jdk_serialize_size", 0L);
            length += bytes.length;
            cost.put("jdk_serialize_size", length);
        });
        long time = cost.getOrDefault("jdk_serialize", 0L);
        long end = System.currentTimeMillis();
        time += (end - start);
        cost.put("jdk_serialize", time);
        byts.forEach(b -> {
            Message deserializer = protobuf.deserializer(b, UserModel.class);
        });
        long deTime = cost.getOrDefault("jdk_deserialize", 0L);
        long end2 = System.currentTimeMillis();
        deTime += (end2 - end);
        cost.put("jdk_deserialize", deTime);
    }

    public void jacksonCost(UserModel user, Map<String, Long> cost, int range) {
        Message message = new Message();
        message.setBody(user);
        ExtensionLoader extensionLoader = ExtensionLoader.getServiceLoader(ISerializerHandler.class);
        ISerializerHandler protobuf = (ISerializerHandler)extensionLoader.getInstance("jackson");
        List<byte[]> byts = new ArrayList<>();
        long start = System.currentTimeMillis();
        IntStream.range(0, range).forEach(i -> {
            byte[] bytes = protobuf.serializer(message);
            byts.add(bytes);
            long length = cost.getOrDefault("jackson_serialize_size", 0L);
            length += bytes.length;
            cost.put("jackson_serialize_size", length);
        });
        long time = cost.getOrDefault("jackson_serialize", 0L);
        long end = System.currentTimeMillis();
        time += (end - start);
        cost.put("jackson_serialize", time);
        byts.forEach(b -> {
            Message deserializer = protobuf.deserializer(b, UserModel.class);
        });
        long deTime = cost.getOrDefault("jackson_deserialize", 0L);
        long end2 = System.currentTimeMillis();
        deTime += (end2 - end);
        cost.put("jackson_deserialize", deTime);
    }
}
