package com.ibiz.redis.mq.topic;

import com.ibiz.mq.common.ExtensionLoader;
import com.ibiz.mq.common.constant.StrategyType;
import com.ibiz.redis.mq.lifecycle.RedisLifecycle;
import com.ibiz.redis.mq.strategy.ITopicStrategy;
import com.ibiz.redis.mq.client.JedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Tuple;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @auther 喻场
 * @date 2020/7/2317:33
 */
public class TopicHandler {
    private static Logger logger = LoggerFactory.getLogger(TopicHandler.class);

    /**
     * 从redis抓取group下所有member
     * @param rlc
     * @param strategy  消费策略
     * @param groupId 组
     * @return
     */
    public static List<Topic> pop(RedisLifecycle rlc, StrategyType strategy, String groupId) {
        List<Topic> topics = new ArrayList<>();
        Set<Tuple> tuples = JedisClient.zrangeByScoreWithScores(rlc.getJedis(), groupId);
        if (null != tuples && !tuples.isEmpty()) {
            topics = tuples.stream().map(t -> new Topic(t.getElement(), t.getScore())).collect(Collectors.toList());
        }
        logger.debug("topics :{}", topics);
        ExtensionLoader<ITopicStrategy> extensionLoader = ExtensionLoader.getServiceLoader(ITopicStrategy.class);
        ITopicStrategy topicStrategy = extensionLoader.getInstance(strategy.name());
        topics = topicStrategy.execute(topics);
        logger.debug("strategy:{},  topics :{}", strategy, topics);
        return topics;
    }
}
