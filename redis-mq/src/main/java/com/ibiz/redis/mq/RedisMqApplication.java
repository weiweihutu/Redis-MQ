package com.ibiz.redis.mq;

import com.ibiz.redis.mq.config.Config;
import com.ibiz.redis.mq.context.SpringContextHolder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class RedisMqApplication {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext cxt = new AnnotationConfigApplicationContext();
        cxt.register(Config.class, SpringContextHolder.class);
        cxt.refresh();
    }
}