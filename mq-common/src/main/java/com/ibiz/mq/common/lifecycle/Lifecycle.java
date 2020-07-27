package com.ibiz.mq.common.lifecycle;

import com.ibiz.mq.common.config.InstanceConfig;

/**
 * @auther yc
 * @date 2020/7/1612:42
 */
public interface Lifecycle {
    /**设置实例配置*/
    void setInstanceConfig(InstanceConfig config);
    /**初始化*/
    void init();
    /**启动*/
    void start();
    /**销毁*/
    void deploy();
}
