package com.ibiz.redis.mq.producer;

import com.ibiz.mq.common.Invocation;
import com.ibiz.mq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class ProducerDemo implements Invocation {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    public static Date start;
    private final AtomicInteger count = new AtomicInteger(0);
    @Override
    public void invoke(Message message) {
        Object body = message.getBody();
        //UserProto.User user = (UserProto.User) body;
        int i = count.incrementAndGet();
        Date end = new Date();
        logger.info("第{}次消费 cost :{}", i, (end.getTime() - start.getTime()));
    }
}
