package com.ibiz.redis.mq.producer;

import com.ibiz.mq.common.Invocation;
import com.ibiz.mq.common.message.Message;
import com.ibiz.redis.mq.RedisMqApplication;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class ProducerDemo implements Invocation {
    public static Date start;
    private final AtomicInteger count = new AtomicInteger(0);
    @Override
    public void invoke(Message message) {
        Object body = message.getBody();
        //UserProto.User user = (UserProto.User) body;
        int i = count.incrementAndGet();
        Date end = new Date();
        System.out.println("第" + i + "次消费 cost :" + (end.getTime() - start.getTime()));
    }
}
