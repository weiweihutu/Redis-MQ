package com.ibiz.redis.mq.test;

import com.ibiz.mq.common.Invocation;
import com.ibiz.mq.common.message.Message;
import com.ibiz.redis.mq.domain.UserProto;

import java.util.concurrent.atomic.AtomicInteger;

public class ProducerDemo implements Invocation {

    private final AtomicInteger count = new AtomicInteger(0);
    @Override
    public void invoke(Message message) {
        Object body = message.getBody();
        //UserProto.User user = (UserProto.User) body;
        int i = count.incrementAndGet();
        System.out.println("第" + i + "次消费 message :" + body);
    }
}
