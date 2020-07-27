package com.ibiz.mq.common;

import com.ibiz.mq.common.message.Message;

/**
 * 消费MQ接口
 * 其他消费者必须实现此接口
 */
public interface Invocation {

    void invoke(Message message);
}
