package com.ibiz.redis.mq.parse;
import com.ibiz.mq.common.config.ConsumerConfig;
import com.ibiz.mq.common.config.InstanceConfig;
import com.ibiz.mq.common.config.MQConfig;
import com.ibiz.redis.mq.constant.Constant;
import com.ibiz.redis.mq.context.SpringContextHolder;
import com.ibiz.redis.mq.factory.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.*;
import org.springframework.core.io.support.ResourcePropertySource;
import java.util.Arrays;

/**
 * @auther yc
 * @date 2020/7/1519:16
 */
public class ConfigParse {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public synchronized void loadConfigurer() {
        ConfigurableEnvironment env = SpringContextHolder.getBean(ConfigurableEnvironment.class);
        MutablePropertySources propertySources = env.getPropertySources();
        propertySources.stream().filter(p -> p instanceof ResourcePropertySource).forEach(p ->
                Arrays.stream(((ResourcePropertySource) p).getPropertyNames()).forEach(k -> MQConfig.MQ_CONFIG.put(k, p.getProperty(k))));
    }

    /**
     * 解析配置项,组装InstanceConfig/ConsumerConfig
     */
    public void parse() {
        //redis-mq-config.consumer={instanceId:"m1",filter:"wms-service.importTaskConsumerHandler",corePoolSize:5,maximumPoolSize:10,workQueueSize:100,ratio:"7:3"}
        parseInstance();
        parseConsumer();
    }

    private void parseConsumer() {
        MQConfig.MQ_CONFIG.forEach((k, v) -> {
            int keyIdx;
            if ((keyIdx = k.indexOf(Constant.CONSUMER_KEY)) > -1) {
                //获取mq类型,默认使用redis MQ
                String protocol = keyIdx - 1 <= 0 ? Constant.AUTO_PROTOCOL : k.substring(0, keyIdx - 1);
                String id = k.substring(keyIdx + Constant.INSTANCE_KEY.length() + 1);
                ConsumerConfig consumerConfig = ConfigFactory.getNewConsumerConfig();
                consumerConfig.init((String)v);
                consumerConfig.setProtocol(protocol);
                consumerConfig.setId(id);
                MQConfig.CONSUMER_CONFIG.putIfAbsent(id, consumerConfig);
            }
        });
        logger.info("consumer config :{}", MQConfig.CONSUMER_CONFIG);
    }

    private void parseInstance() {
        //redis-mq-config-instance.m1={hostname:"127.0.0.1",port:6379,timeout:3000,usePool:true,dbIndex:0,maxTotal:500,maxIdle:20,timeBetweenEvictionRunsMillis:30000,minEvictableIdleTimeMillis:30000,maxWaitMillis:5000,testOnCreate:true,testOnBorrow:false,testOnReturn:true,testWhileIdle:true}
        MQConfig.MQ_CONFIG.forEach((k, v) -> {
            int keyIdx;
            if ((keyIdx = k.indexOf(Constant.INSTANCE_KEY)) > -1) {
                //获取mq类型,默认使用redis MQ
                String protocol = keyIdx - 1 <= 0 ? Constant.AUTO_PROTOCOL : k.substring(0, keyIdx - 1);
                String instanceId = k.substring(keyIdx + Constant.INSTANCE_KEY.length() + 1);
                InstanceConfig instanceConfig = ConfigFactory.getNewInstanceConfig(protocol);
                instanceConfig.init((String)v);
                instanceConfig.setProtocol(protocol);
                instanceConfig.setInstanceId(instanceId);
                System.out.println("instanceConfig:" + instanceConfig);
                MQConfig.INSTANCE_CONFIG.putIfAbsent(instanceId, instanceConfig);
            }
        });
        logger.info("instance config :{}", MQConfig.INSTANCE_CONFIG);
    }
}
