package com.ibiz.redis.mq.producer;

import com.ibiz.mq.common.ExtensionLoader;
import com.ibiz.mq.common.constant.ErrorCode;
import com.ibiz.mq.common.exception.ServiceException;
import com.ibiz.mq.common.message.Message;
import com.ibiz.mq.common.producer.IProducer;
import com.ibiz.mq.common.serializ.ISerializerHandler;
import com.ibiz.redis.mq.constant.Constant;
import com.ibiz.redis.mq.context.InstanceHolder;
import com.ibiz.redis.mq.lifecycle.RedisLifecycle;
import com.ibiz.redis.mq.client.JedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import static com.ibiz.mq.common.util.StringUtil.toByteUtf8;
import static com.ibiz.redis.mq.constant.Constant.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @auther yc
 * @date 2020/7/229:40
 */
public class RedisProducer implements IProducer {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public void publisher(String instanceId, String bean, String businessKey, String serialize, Message message) {
        try {
            RedisLifecycle lifecycle = (RedisLifecycle)InstanceHolder.getInstanceHolder().getLifecycle(instanceId);
            if (Objects.isNull(lifecycle)) {
                throw new RuntimeException("mq instanceId:" + instanceId + " not exist");
            }
            logger.debug("instanceId:{}, bean:{}, businessKey:{} publisher", instanceId, bean, businessKey);
            bean = Constant.REDIS_KEY_PREFIX + bean;
            Jedis jedis = lifecycle.getJedis();
            String topic = bean + "_" + businessKey;
            String messageCLazz = topic + REDIS_KEY_MESSAGE_CLAZZ_SUFFIX;
            List<byte[]> keys = Arrays.asList(toByteUtf8(bean),
                    toByteUtf8(topic), toByteUtf8(REDIS_MQ_EVERY_PRODUCE_PULL_QUEUE_QUANTITY),
                    toByteUtf8(REDIS_MQ_TOTAL_PRODUCE_PULL_QUEUE_QUANTITY),
                    toByteUtf8(REDIS_MQ_TOTAL_PRODUCE_PULL_QUANTITY_TIME),
                    toByteUtf8(messageCLazz),
                    toByteUtf8(message.getClazz()));
            ExtensionLoader extensionLoader = ExtensionLoader.getServiceLoader(ISerializerHandler.class);
            ISerializerHandler serializer = (ISerializerHandler)extensionLoader.getInstance(serialize,"protobuf");
            byte[] messageByte = serializer.serializer(message);
            JedisClient.evalSha(instanceId, message, jedis, topic, keys, messageByte);
        } catch (RuntimeException e) {
            logger.error("producer task error", e);
            throw e;
        }
    }


}
