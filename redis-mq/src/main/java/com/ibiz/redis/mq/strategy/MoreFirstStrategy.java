package com.ibiz.redis.mq.strategy;

import com.ibiz.redis.mq.topic.Topic;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @auther yc
 * @date 2020/7/2319:30
 */
public class MoreFirstStrategy implements ITopicStrategy {
    @Override
    public List<Topic> execute(List<Topic> topics) {
        return null != topics && !topics.isEmpty() ?
                topics.stream().sorted(Comparator.comparing(Topic::getScore).reversed()).collect(Collectors.toList())
                : new ArrayList<>();
    }
}
