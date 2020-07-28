package com.ibiz.redis.mq.parse;
import com.ibiz.mq.common.config.ConsumerConfig;
import com.ibiz.mq.common.config.InstanceConfig;
import com.ibiz.mq.common.config.MQConfig;
import com.ibiz.mq.common.constant.ErrorCode;
import com.ibiz.mq.common.util.ValidateUtil;
import com.ibiz.redis.mq.constant.Constant;
import com.ibiz.redis.mq.context.SpringContextHolder;
import com.ibiz.redis.mq.factory.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.*;
import org.springframework.core.io.support.ResourcePropertySource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
        parseInstance();
        parseConsumer();
    }

    private void parseConsumer() {
        Set<String> consumerIds = new HashSet<>();
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
                ValidateUtil.validate(id, (o) -> !consumerIds.add(o), ErrorCode.COMMON_CODE, "repeat consumerId :" + id);
                MQConfig.CONSUMER_CONFIG.put(id, consumerConfig);
            }
        });
        logger.info("consumer config :{}", MQConfig.CONSUMER_CONFIG);
    }

    private void parseInstance() {
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
                MQConfig.INSTANCE_CONFIG.put(instanceId, instanceConfig);
            }
        });
        logger.info("instance config :{}", MQConfig.INSTANCE_CONFIG);
    }
}
