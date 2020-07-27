package com.ibiz.redis.mq.config;

import com.ibiz.redis.mq.MQBootstrap;
import com.ibiz.redis.mq.parse.ConfigParse;
import com.ibiz.redis.mq.test.ProducerDemo;
import org.springframework.context.annotation.*;


/**
 * @auther yc
 * @date 2020/7/1519:22
 */
@Configuration
@PropertySource("application.properties")
public class Config {
    @Bean
    ConfigParse configParse() {
        return new ConfigParse();
    }
    @Bean
    MQBootstrap bootstrap() {
        return new MQBootstrap();
    }
    @Bean("producer.demo")
    ProducerDemo producerDemo() {return new ProducerDemo();}
}
