package com.ibiz.mq.common.message;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @auther yc
 * @date 2020/7/2116:19
 */
public class Message {
    /**实体对象*/
    private Object body;
    /**class全路径,用于反序列化*/
    private String clazz;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
        this.clazz = body.getClass().getName();
    }

    public String getClazz() {
        return clazz;
    }

    public static final class MessageBuilder {
        public static Message creator(Object body) {
            Message msg = new Message();
            msg.setBody(body);
            return msg;
        }
    }
}

