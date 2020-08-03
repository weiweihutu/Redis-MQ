package com.ibiz.mq.redis.mq.test.controller;

import com.ibiz.mq.common.ExtensionLoader;
import com.ibiz.mq.common.config.ConsumerConfig;
import com.ibiz.mq.common.config.MQConfig;
import com.ibiz.mq.common.message.Message;
import com.ibiz.mq.common.producer.IProducer;
import com.ibiz.mq.redis.mq.test.domain.UserInfo;
import com.ibiz.mq.redis.mq.test.service.IUserService;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @auther yc
 * @date 2020/6/1717:44
 */
@RestController
public class RedisMqController  {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IUserService userService;
    /**
     * 生产任务
     * @param size  任务数量
     * @param table 操作的表名
     */
    @GetMapping("/push")
    public void pushQueue(@RequestParam(name = "size") Integer size, @RequestParam(name = "table") String table) {
        producer(size, table, Arrays.asList("userInfoConsumerHandler"));
    }

    /**
     * 不同redis实例
     * @param size
     * @param table
     */
    @GetMapping("/push2")
    public void pushDiffRedisInstance(@RequestParam(name = "size") Integer size, @RequestParam(name = "table") String table) {
        //userInfoConsumerHandler userInfoConsumer 消费者id ,在配置文件中配置
        producer(size, table, Arrays.asList("userInfoConsumerHandler", "userInfoConsumer"));
    }

    @GetMapping("/count")
    public int count(@RequestParam(name = "table") String table) {
        return userService.count(table);
    }

    private void producer(int size, String table, List<String> consumerIds) {
        consumerIds.stream().forEach(id -> {
            ConsumerConfig consumerConfig = MQConfig.CONSUMER_CONFIG.get(id);
            ExtensionLoader extensionLoader = ExtensionLoader.getServiceLoader(IProducer.class);
            IProducer producer = (IProducer) extensionLoader.getInstance(consumerConfig.getProtocol());
            StringBuilder sb = new StringBuilder();
            IntStream.range(0,2000).forEach(i -> sb.append("中"));
            String name = sb.toString();
            IntStream.range(0, size).forEach(tpc -> {
                Message message = new Message();
                UserInfo userInfo = new UserInfo();
                userInfo.setAge(tpc % 10 + 20);
                userInfo.setCity("深圳" + tpc);
                userInfo.setCountry("CN");
                userInfo.setTable(table);
                userInfo.setUserName(name);
                message.setBody(userInfo);
                producer.publisher(consumerConfig.getInstanceId(), consumerConfig.getBean(), "TEST", consumerConfig.getSerializer(), message);
            });
        });
    }

}
