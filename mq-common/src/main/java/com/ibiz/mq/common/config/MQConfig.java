package com.ibiz.mq.common.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @auther yc
 * @date 2020/7/1612:30
 */
public class MQConfig {
    /**延迟加载*/
    public static int DEPLOY = 500;
    /**刷新配置间隔时间*/
    public static int REFRESH_INTERVAL = 600000;
    /**所有mq的配置*/
    public static final Map<String, Object> MQ_CONFIG = new ConcurrentHashMap<>();
    //mq类型实例配置
    // key instanceId
    public static final Map<String, InstanceConfig> INSTANCE_CONFIG = new ConcurrentHashMap<>();
    // key consumerName
    public static final Map<String, ConsumerConfig> CONSUMER_CONFIG = new ConcurrentHashMap<>();

}
