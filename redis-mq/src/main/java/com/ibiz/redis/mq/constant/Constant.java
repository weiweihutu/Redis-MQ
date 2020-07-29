package com.ibiz.redis.mq.constant;

/**
 * @auther yc
 * @date 2020/7/1613:03
 */
public class Constant {
    public static final String INSTANCE_KEY = "mq-config-instance";
    public static final String CONSUMER_KEY = "mq-config-consumer";
    //默认使用redisMQ
    public static final String AUTO_PROTOCOL = "redis";
    public static final String LUA_SCRIPT_ROOT_PATH = "config" + System.getProperty("file.separator") + "lua" + System.getProperty("file.separator");
    public static final String LUA_PUBLISH = "publish.lua";
    public static final String LUA_CONSUME = "consume.lua";
    /**
     * redis mq key 前缀
     */
    public static final String REDIS_KEY_PREFIX = "REDIS_MQ_";
    /**redis message 实体class后缀  key = topic_CLAZZ  topic是REDIS_MQ_ + 具体bean_business_key_*/
    public static final String REDIS_KEY_MESSAGE_CLAZZ_SUFFIX = "_CLAZZ";
    /**每个消费者总数量key sortset*/
    public static final String REDIS_MQ_EVERY_PRODUCE_POP_QUEUE_QUANTITY = "REDIS_MQ_EVERY_PRODUCE_POP_QUEUE_QUANTITY";
    /**总消费者数量 key*/
    public static final String REDIS_MQ_TOTAL_PRODUCE_POP_QUEUE_QUANTITY = "REDIS_MQ_TOTAL_PRODUCE_POP_QUEUE_QUANTITY";
    /**总消费数量次数 超过10000000000000000000,在sortset 添加REDIS_MQ_TOTAL_PRODUCE_POP_QUANTITY_TIME
     * 最后总的数量=get(REDIS_MQ_TOTAL_PRODUCE_POP_QUEUE_QUANTITY) + (10000000000000000000 * get(REDIS_MQ_TOTAL_PRODUCE_POP_QUANTITY_TIME))*/
    public static final String REDIS_MQ_TOTAL_PRODUCE_POP_QUANTITY_TIME = "REDIS_MQ_TOTAL_PRODUCE_POP_QUANTITY_TIME";

    /**每个生产者总数量key sortset*/
    public static final String REDIS_MQ_EVERY_PRODUCE_PULL_QUEUE_QUANTITY = "REDIS_MQ_EVERY_PRODUCE_PULL_QUEUE_QUANTITY";
    /**总生产者数量 key*/
    public static final String REDIS_MQ_TOTAL_PRODUCE_PULL_QUEUE_QUANTITY = "REDIS_MQ_TOTAL_PRODUCE_PULL_QUEUE_QUANTITY";
    /**总生产者数量次数 超过10000000000000000000,在sortset 添加REDIS_MQ_TOTAL_PRODUCE_PULL_QUANTITY_TIME
     * 最后总的数量=get(REDIS_MQ_TOTAL_PRODUCE_PULL_QUEUE_QUANTITY) + (10000000000000000000 * get(REDIS_MQ_TOTAL_PRODUCE_PULL_QUANTITY_TIME))*/
    public static final String REDIS_MQ_TOTAL_PRODUCE_PULL_QUANTITY_TIME = "REDIS_MQ_TOTAL_PRODUCE_PULL_QUANTITY_TIME";

}
