package com.ibiz.mq.redis.mq.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.ibiz")
public class RedisMqTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisMqTestApplication.class, args);
    }

}
