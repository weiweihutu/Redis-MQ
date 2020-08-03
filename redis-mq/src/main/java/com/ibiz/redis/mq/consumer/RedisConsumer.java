package com.ibiz.redis.mq.consumer;

import com.ibiz.mq.common.ExtensionLoader;
import com.ibiz.mq.common.Invocation;
import com.ibiz.mq.common.constant.ErrorCode;
import com.ibiz.mq.common.consumer.IConsumer;
import com.ibiz.mq.common.exception.ServiceException;
import com.ibiz.mq.common.message.Message;
import com.ibiz.mq.common.serializ.ISerializerHandler;
import com.ibiz.mq.common.util.ClassUtil;
import com.ibiz.mq.common.util.RuntimeError;
import com.ibiz.redis.mq.constant.Constant;
import com.ibiz.redis.mq.context.InstanceHolder;
import com.ibiz.redis.mq.context.SpringContextHolder;
import com.ibiz.redis.mq.lifecycle.RedisLifecycle;
import com.ibiz.redis.mq.client.JedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Objects;

import static com.ibiz.mq.common.util.StringUtil.toByteUtf8;
import static com.ibiz.redis.mq.constant.Constant.*;

/**
 * @auther yc
 * @date 2020/7/2219:18
 */
public class RedisConsumer implements IConsumer {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public void consume(String instanceId, String bean, String topic, String serialize) {
        RedisLifecycle lifecycle = (RedisLifecycle) InstanceHolder.getInstanceHolder().getLifecycle(instanceId);
        if (Objects.isNull(lifecycle)) {
            throw new ServiceException(ErrorCode.COMMON_CODE.getCode(), "mq instanceId:" + instanceId + " not exist");
        }
        Object handler = SpringContextHolder.getBean(bean);
        if (null == handler || !Invocation.class.isAssignableFrom(handler.getClass())) {
            RuntimeError.creator("instanceId :" + instanceId +" bean :" + bean + " not instance of Invocation");
        }
        Invocation invoke = (Invocation)handler;
        bean = Constant.REDIS_KEY_PREFIX + bean;
        String classNameKey = topic + REDIS_KEY_MESSAGE_CLAZZ_SUFFIX;
        Class messageBodyClass = null;
        String className = null;
        try {
            className = JedisClient.get(lifecycle.getJedis(), classNameKey);
            messageBodyClass = ClassUtil.getClass(className);
        } catch (Exception e) {
            RuntimeError.creator("instanceId :" + instanceId +" rpop produce classNameKey :" + classNameKey +" , className:" + className + " class for className error", e);
        }
        byte[] result = JedisClient.evalsha(lifecycle.getJedis(), toByteUtf8(bean), toByteUtf8(topic), toByteUtf8(REDIS_MQ_EVERY_PRODUCE_POP_QUEUE_QUANTITY), toByteUtf8(REDIS_MQ_TOTAL_PRODUCE_POP_QUEUE_QUANTITY), toByteUtf8(REDIS_MQ_TOTAL_PRODUCE_POP_QUANTITY_TIME));
        byte[] buf = result;
        if (null == buf) {
            logger.debug("instanceId :{} rpop produce topic :{} data is null", instanceId, topic);
            return;
        }
        ExtensionLoader extensionLoader = ExtensionLoader.getServiceLoader(ISerializerHandler.class);
        ISerializerHandler serializerHandler = (ISerializerHandler)extensionLoader.getInstance(serialize,"protobuf");
        Message message = serializerHandler.deserializer(buf, messageBodyClass);
        logger.info("instanceId :{} rpop produce topic :{} , messageCLass:{}", instanceId, topic, message.getClazz());
        try {
            invoke.invoke(message);
        } catch (Exception e) {
            RuntimeError.creator("instanceId: " + instanceId +" consumer business :" + topic + " throw exception", e);
        }
    }


}
