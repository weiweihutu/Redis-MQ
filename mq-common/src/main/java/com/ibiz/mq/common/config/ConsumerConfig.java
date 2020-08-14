package com.ibiz.mq.common.config;

import com.ibiz.mq.common.constant.StrategyType;
import com.ibiz.mq.common.util.GsonUtil;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.BeanUtils;

/**
 * @auther 喻场
 * @date 2020/7/1519:08
 */
public class ConsumerConfig {
    //mq 类型
    private String protocol;
    private String instanceId;
    private String bean;
    private int corePoolSize;
    private int maximumPoolSize;
    private int queueSize;
    private String id;
    private long sleep = 500L;
    private String serializer = "gson";
    /**默认使用最多待消费任务优先*/
    private StrategyType strategy = StrategyType.MORE_FIRST;

    public String getSerializer() {
        return serializer;
    }

    public void setSerializer(String serializer) {
        this.serializer = serializer;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getBean() {
        return bean;
    }

    public void setBean(String bean) {
        this.bean = bean;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getSleep() {
        return sleep;
    }

    public void setSleep(long sleep) {
        this.sleep = sleep;
    }

    public StrategyType getStrategy() {
        return strategy;
    }

    public void setStrategy(StrategyType strategy) {
        this.strategy = strategy;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public void init(String config) {
        ConsumerConfig consumerConfig = GsonUtil.fromJson(config, this.getClass());
        BeanUtils.copyProperties(consumerConfig, this);
    }
}
