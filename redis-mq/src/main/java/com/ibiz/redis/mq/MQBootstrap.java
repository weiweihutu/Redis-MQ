package com.ibiz.redis.mq;

import com.ibiz.mq.common.config.MQConfig;
import com.ibiz.mq.common.lifecycle.Lifecycle;
import com.ibiz.redis.mq.context.InstanceHolder;
import com.ibiz.redis.mq.context.SpringContextHolder;
import com.ibiz.redis.mq.factory.LifecycleFactory;
import com.ibiz.redis.mq.parse.ConfigParse;


/**
 * @auther yc
 * @date 2020/7/2012:24
 */
public class MQBootstrap {

    public MQBootstrap() {
        ConfigParse parse = SpringContextHolder.getBean(ConfigParse.class);
        parse.loadConfigurer();
        parse.parse();
        startup();
    }

    public void startup() {
        //初始化所有实例
        MQConfig.INSTANCE_CONFIG.values().stream().forEach(cfg -> {
            Lifecycle lcc = LifecycleFactory.getNewInstance(cfg);
            lcc.setInstanceConfig(cfg);
            InstanceHolder.getInstanceHolder().registry(cfg.getInstanceId(), lcc);
            lcc.init();
            lcc.start();
        });
    }


}
