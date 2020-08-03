package com.ibiz.mq.test;

import com.ibiz.mq.common.ExtensionLoader;
import com.ibiz.mq.common.config.ConsumerConfig;
import com.ibiz.mq.common.config.MQConfig;
import com.ibiz.mq.common.consumer.IConsumer;
import com.ibiz.mq.common.message.Message;
import com.ibiz.mq.common.producer.IProducer;
import com.ibiz.redis.mq.config.Config;
import com.ibiz.redis.mq.context.SpringContextHolder;
import com.ibiz.redis.mq.domain.UserModel;
import com.ibiz.redis.mq.domain.UserProto;
import com.ibiz.redis.mq.producer.ProducerDemo;
import com.ibiz.redis.mq.thread.DefineThreadPoolExecutor;
import com.ibiz.redis.mq.thread.WorkThreadPoolManager;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

class RedisMqApplicationTests {
    public final static CountDownLatch latch = new CountDownLatch(1);
    private static void await() {
        try {
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        AnnotationConfigApplicationContext cxt = new AnnotationConfigApplicationContext(Config.class, SpringContextHolder.class);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                producer(100);
            }
        }, 10000);
        //cxt.refresh();
        //producer(1);
        //consume(10);
        //await();
    }

    public  void consume(int size) throws NoSuchFieldException, IllegalAccessException {
        ExtensionLoader extensionLoader = ExtensionLoader.getServiceLoader(IConsumer.class);
        ConsumerConfig consumerConfig = MQConfig.CONSUMER_CONFIG.get("test");
        IConsumer consumer = (IConsumer) extensionLoader.getInstance(consumerConfig.getProtocol());
        IntStream.range(0, size).forEach(i -> {
            new Thread(() -> {
                consumer.consume("m1", "producer.demo", "TEST", consumerConfig.getSerializer());
            }).start();
        });
    }

    private static void producer(int size) {
        ConsumerConfig consumerConfig = MQConfig.CONSUMER_CONFIG.get("test");
        ExtensionLoader extensionLoader = ExtensionLoader.getServiceLoader(IProducer.class);
        IProducer producer = (IProducer) extensionLoader.getInstance(consumerConfig.getProtocol());
        //UserProto.User user = UserFactory.getUser();
        UserModel user = UserFactory.getJdkUser();
        Message message = new Message();
        message.setBody(user);
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
