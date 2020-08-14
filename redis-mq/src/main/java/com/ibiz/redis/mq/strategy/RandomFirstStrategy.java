package com.ibiz.redis.mq.strategy;

import com.ibiz.redis.mq.topic.Topic;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther 喻场
 * @date 2020/7/2319:30
 */
public class RandomFirstStrategy implements ITopicStrategy {
    @Override
    public List<Topic> execute(List<Topic> topics) {
        return null != topics && !topics.isEmpty() ? topics : new ArrayList<>();
    }
}