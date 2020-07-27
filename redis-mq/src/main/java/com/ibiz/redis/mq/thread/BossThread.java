package com.ibiz.redis.mq.thread;

import com.ibiz.mq.common.config.ConsumerConfig;
import com.ibiz.mq.common.lifecycle.Lifecycle;
import com.ibiz.redis.mq.constant.Constant;
import com.ibiz.redis.mq.context.InstanceHolder;
import com.ibiz.redis.mq.lifecycle.RedisLifecycle;
import com.ibiz.redis.mq.topic.Topic;
import com.ibiz.redis.mq.topic.TopicHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class BossThread implements Runnable {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ConsumerConfig consumerConfig;
    /**线程是否运行 调用shutdown 则停止运行*/
    public final AtomicBoolean isRunning = new AtomicBoolean(Boolean.TRUE);
    public BossThread(ConsumerConfig consumerConfig) {
        this.consumerConfig = consumerConfig;
    }
    @Override
    public void run() {
        //mq 实例id
        String instanceId = consumerConfig.getInstanceId();
        logger.info("instanceId : {} consumer : {} start ", instanceId, consumerConfig);
        while (isRunning.get()) {
            try {
                logger.info("BOSS THREAD RUN ................");
                //消费者id
                final String id = consumerConfig.getBean();
                DefineThreadPoolExecutor pte = WorkThreadPoolManager.getInstance().createExecutor(id, consumerConfig.getCorePoolSize(), consumerConfig.getMaximumPoolSize(), consumerConfig.getQueueSize(), consumerConfig.getSleep());
                //TODOWork线程池注册到BOSS线程，产生关联
                //BossThreadManager.getInstance().registry(instanceId, pte);
                Lifecycle lifecycle = InstanceHolder.getInstanceHolder().getLifecycle(instanceId);
                //此处可以使用抽象方法扩展其他mq类型,让子类去执行
                //由于现在只有redis,直接强转
                RedisLifecycle rlc = (RedisLifecycle)lifecycle;
                List<Topic> topics = TopicHandler.pop(rlc, consumerConfig.getStrategy(), Constant.REDIS_KEY_PREFIX + consumerConfig.getBean());
                if (topics.isEmpty()) {
                    try {
                        //增加休眠是为了停止线程一直循环
                        Thread.currentThread().sleep(consumerConfig.getSleep());
                    } catch (Exception e) {
                        logger.error("redis mq BossThread sleep :{} error", consumerConfig.getSleep());
                    }
                }
                topics.stream().forEach(tpc -> {
                    pte.increment();
                    try {
                        pte.submit(new WorkThread(tpc.getElement(), consumerConfig));
                    } catch (Exception e) {
                        logger.error("WorkThreadPoolExecutors submit error", e);
                        pte.decrement();
                    }
                });
            } catch (Exception e) {
                logger.error("redis mq BossThread run error", e);
            } catch (Throwable th) {
                logger.error("redis mq BossThread run error", th);
            }
        }

    }

    public void shutdown() {
        isRunning.set(false);
        logger.info("consumer : {} shutdown ", consumerConfig);
    }
}
