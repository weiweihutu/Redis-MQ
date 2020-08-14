package com.ibiz.mq.test;

import com.ibiz.mq.common.ExtensionLoader;
import com.ibiz.mq.common.config.ConsumerConfig;
import com.ibiz.mq.common.config.MQConfig;
import com.ibiz.mq.common.consumer.IConsumer;
import com.ibiz.mq.common.util.GsonUtil;
import com.ibiz.redis.mq.client.JedisClient;
import com.ibiz.redis.mq.config.RedisConfig;
import com.ibiz.redis.mq.context.InstanceHolder;
import com.ibiz.redis.mq.factory.LifecycleFactory;
import com.ibiz.redis.mq.lifecycle.RedisLifecycle;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.util.Pool;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * @auther 喻场
 * @date 2020/7/2218:47
 */
public class RedisProduceTest {
    @Test
    public void producer() throws NoSuchFieldException, IllegalAccessException {
        //redis-mq-config-consumer.test={instanceId:"m1",bean:"producer.demo",corePoolSize:5,maximumPoolSize:10,queueSize:100,strategy:"MORE_FIRST"}
        //redis-mq-config-instance.m1={hostname:"127.0.0.1",port:6379,timeout:3000,usePool:true,dbIndex:0,maxTotal:500,maxIdle:20,timeBetweenEvictionRunsMillis:30000,minEvictableIdleTimeMillis:30000,maxWaitMillis:5000,testOnCreate:true,testOnBorrow:false,testOnReturn:true,testWhileIdle:true,sentinel:false,sentinelMasterName:"sentinelMaster",sentinels:["127.0.0.1:6378","127.0.0.1:6379"]}

        String instanceJson = "{hostname:\"192.168.242.101\",port:6379,timeout:3000,usePool:true,dbIndex:0,maxTotal:500,maxIdle:20,timeBetweenEvictionRunsMillis:30000,minEvictableIdleTimeMillis:30000,maxWaitMillis:5000,testOnCreate:true,testOnBorrow:false,testOnReturn:true,testWhileIdle:true,sentinel:false,sentinelMasterName:\"sentinelMaster\",sentinels:[\"127.0.0.1:6378\",\"127.0.0.1:6379\"]}";
        String consumerJson = "{instanceId:\"m1\",bean:\"wms-service.importTaskConsumerHandler\",corePoolSize:5,maximumPoolSize:10,queueSize:100,strategy:\"MORE_FIRST\"}";
        RedisConfig instanceConfig = GsonUtil.fromJson(instanceJson, RedisConfig.class);
        ConsumerConfig consumerConfig = GsonUtil.fromJson(consumerJson, ConsumerConfig.class);
        instanceConfig.setInstanceId("m1");
        instanceConfig.setProtocol("redis");
        consumerConfig.setInstanceId("m1");
        consumerConfig.setProtocol("redis");
        consumerConfig.setId("test");
        MQConfig.INSTANCE_CONFIG.put(instanceConfig.getInstanceId(), instanceConfig);
        MQConfig.CONSUMER_CONFIG.put(consumerConfig.getId(), consumerConfig);
        RedisLifecycle lcc = (RedisLifecycle)LifecycleFactory.getNewInstance(instanceConfig);
        lcc.setInstanceConfig(instanceConfig);
        InstanceHolder.getInstanceHolder().registry(instanceConfig.getInstanceId(), lcc);
        Pool<Jedis> jedisPool = JedisClient.JedisPoolCreator.getInstance(instanceConfig);
        Field jedisPoolField = RedisLifecycle.class.getDeclaredField("holder");
        //RedisLifecycle.Holder改为public static
        RedisLifecycle.Holder holder = new RedisLifecycle.Holder(jedisPool);
        jedisPoolField.setAccessible(true);
        jedisPoolField.set(lcc, holder);
        //Set<Tuple> tuples = JedisClient.zrangeByScoreWithScores(lcc.getJedis(), "REDIS_MQ_producer.demo");
        Set<Tuple> tuples = lcc.getJedis().zrangeByScoreWithScores("REDIS_MQ_producer.demo", 0, Integer.MAX_VALUE);
        System.out.println(">>>>>>>>>>>>" + tuples);
        String s = JedisClient.get(lcc.getJedis(), "REDIS_MQ_producer.demo_TEST_CLAZZ");
        System.out.println(">>>>>" + s);
        //MQBootstrap bootstrap = new MQBootstrap();
        //bootstrap.startup();
        /*MQBootstrap bootstrap = SpringContextHolder.getBean(MQBootstrap.class);
        bootstrap.startup();
        ConsumerConfig consumerConfig = MQConfig.CONSUMER_CONFIG.get("test");*/
        /*ExtensionLoader extensionLoader = ExtensionLoader.getServiceLoader(IProducer.class);
        IProducer producer = (IProducer) extensionLoader.getInstance(consumerConfig.getProtocol());
        UserProto.User user = UserFactory.getUser();

        Message message = new Message();
        message.setBody(user);
        producer.publisher(consumerConfig.getInstanceId(), consumerConfig.getBean(), "TEST", consumerConfig.getSerializer(), message);
    */}

    @Test
    public void consume() throws NoSuchFieldException, IllegalAccessException {
        producer();
        ExtensionLoader extensionLoader = ExtensionLoader.getServiceLoader(IConsumer.class);
        ConsumerConfig consumerConfig = MQConfig.CONSUMER_CONFIG.get("test");
        IConsumer consumer = (IConsumer) extensionLoader.getInstance(consumerConfig.getProtocol());
        consumer.consume("m1", "wms-service.importTaskConsumerHandler", "TEST", consumerConfig.getSerializer());
    }
}
