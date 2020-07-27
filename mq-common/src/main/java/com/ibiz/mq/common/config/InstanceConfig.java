package com.ibiz.mq.common.config;

import com.ibiz.mq.common.util.GsonUtil;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.BeanUtils;

/**
 * mq实例配置
 * @auther yc
 * @date 2020/7/1612:31
 */
public class InstanceConfig {
    //{type:"redis",hostname:"127.0.0.1",port:6379,timeout:3000,usePool:true,dbIndex:0,maxTotal:500,maxIdle:20,timeBetweenEvictionRunsMillis:30000,minEvictableIdleTimeMillis:30000,maxWaitMillis:5000,testOnCreate:true,testOnBorrow:false,testOnReturn:true,testWhileIdle:true}
    //mq 类型
    protected String protocol;
    protected String instanceId;
    protected String userName;
    protected String password;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void init(String config) {
        InstanceConfig instanceConfig = GsonUtil.fromJson(config, this.getClass());
        BeanUtils.copyProperties(instanceConfig, this);
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


}

