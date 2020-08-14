package com.ibiz.redis.mq.strategy;

import com.ibiz.redis.mq.topic.Topic;

import java.util.List;

/**
 * 消费任务策略
 *  * 获取到要消费的topic之后，根据score排序
 *  * 根据具体策略再对topic进行排序
 * @see StrategyType
 * @auther 喻场
 * @date 2020/7/2319:14
 */
public interface ITopicStrategy {
    /**
     * 获取到要消费的topic之后，根据score排序
     * 根据具体策略再对topic进行排序
     * @param topics
     * @return
     */
    List<Topic> execute(List<Topic> topics);
}
