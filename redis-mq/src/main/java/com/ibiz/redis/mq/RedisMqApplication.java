package com.ibiz.redis.mq;

import com.ibiz.mq.common.ExtensionLoader;
import com.ibiz.mq.common.config.ConsumerConfig;
import com.ibiz.mq.common.config.MQConfig;
import com.ibiz.mq.common.message.Message;
import com.ibiz.mq.common.producer.IProducer;
import com.ibiz.redis.mq.config.Config;
import com.ibiz.redis.mq.context.SpringContextHolder;
import com.ibiz.redis.mq.domain.UserProto;
import com.ibiz.redis.mq.producer.ProducerDemo;
import com.ibiz.redis.mq.thread.DefineThreadPoolExecutor;
import com.ibiz.redis.mq.thread.WorkThreadPoolManager;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.IntStream;

public class RedisMqApplication {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext cxt = new AnnotationConfigApplicationContext();
        cxt.register(Config.class, SpringContextHolder.class);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                producer(10000);
            }
        }, 10000);
        cxt.refresh();
    }

    private static void producer(int size) {
        ConsumerConfig consumerConfig = MQConfig.CONSUMER_CONFIG.get("test");
        ExtensionLoader extensionLoader = ExtensionLoader.getServiceLoader(IProducer.class);
        IProducer producer = (IProducer) extensionLoader.getInstance(consumerConfig.getProtocol());
        String msg = "测试走一手";
        Message message = new Message();
        message.setBody(msg);
        ProducerDemo.start = new Date();
        DefineThreadPoolExecutor pte = WorkThreadPoolManager.getInstance().createExecutor(consumerConfig);
        IntStream.range(0, size).forEach(tpc -> {
            pte.increment();
            try {
                pte.submit(() -> {
                    producer.publisher(consumerConfig.getInstanceId(), consumerConfig.getBean(), "TEST", consumerConfig.getSerializer(), message);
                });
            } catch (Exception e) {
                pte.decrement();
            }
        });
    }
}