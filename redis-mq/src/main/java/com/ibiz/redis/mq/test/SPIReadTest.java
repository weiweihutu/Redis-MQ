package com.ibiz.redis.mq.test;


import com.google.gson.Gson;
import com.ibiz.mq.common.ExtensionLoader;
import com.ibiz.mq.common.lifecycle.Lifecycle;
import com.ibiz.redis.mq.config.RedisConfig;
import com.ibiz.redis.mq.strategy.ITopicStrategy;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @auther yc
 * @date 2020/7/179:52
 */
public class SPIReadTest {

    @Test
    public void loadClass() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        ExtensionLoader extensionLoader = ExtensionLoader.getServiceLoader(Lifecycle.class);
        Method method = ExtensionLoader.class.getDeclaredMethod("loadClass", null);
        method.setAccessible(true);
        method.invoke(extensionLoader, null);
        System.out.println(extensionLoader.getInstance("redis"));
        System.out.println(extensionLoader.getInstance("redis1"));
        ExtensionLoader strategyLoader = ExtensionLoader.getServiceLoader(ITopicStrategy.class);
        ITopicStrategy strategy = (ITopicStrategy)strategyLoader.getInstance("MORE_FIRST");
        strategy.execute(new ArrayList<>());
        strategy = (ITopicStrategy)strategyLoader.getInstance("LESS_FIRST");
        strategy.execute(new ArrayList<>());
        strategy = (ITopicStrategy)strategyLoader.getInstance("RANDOM_FIRST");
        strategy.execute(new ArrayList<>());
    }

    @Test
    public void parseConfig() {
        String str = "{password:\"123456\",type:\"redis\",hostname:\"127.0.0.1\",port:6379,timeout:3000,usePool:true,dbIndex:0,maxTotal:500,maxIdle:20,timeBetweenEvictionRunsMillis:30000,minEvictableIdleTimeMillis:30000,maxWaitMillis:5000,testOnCreate:true,testOnBorrow:false,testOnReturn:true,testWhileIdle:true}";
        Gson gson = new Gson();
        RedisConfig redisConfig = gson.fromJson(str, RedisConfig.class);
        System.out.println(redisConfig);
    }
}
