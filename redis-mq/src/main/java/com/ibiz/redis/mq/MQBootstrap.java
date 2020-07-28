package com.ibiz.redis.mq;

import com.ibiz.mq.common.config.MQConfig;
import com.ibiz.mq.common.lifecycle.Lifecycle;
import com.ibiz.redis.mq.context.InstanceHolder;
import com.ibiz.redis.mq.factory.LifecycleFactory;
import com.ibiz.redis.mq.parse.ConfigParse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * @auther yc
 * @date 2020/7/2012:24
 */
public class MQBootstrap {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ConfigParse parse;
    private Timer timer = new Timer();
    private volatile boolean _start = Boolean.FALSE;
    private Lock lock = new ReentrantLock();
    public MQBootstrap(ConfigParse parse) {
        this.parse = parse;
        init();
    }

    public void init() {
        parse.loadConfigurer();
        parse.parse();
        timer.schedule(new DefineTimerTask(), MQConfig.DEPLOY);
    }

    public void startup() {
        lock.lock();
        try {
            if (_start) {
                InstanceHolder.getInstanceHolder().deploy();
                _start = Boolean.FALSE;
            }
            _start();
            //已启动
            _start = Boolean.TRUE;
        } finally {
            lock.unlock();
        }
    }

    private void _start() {
        //初始化所有实例
        MQConfig.INSTANCE_CONFIG.values().stream().forEach(cfg -> {
            Lifecycle lcc = LifecycleFactory.getNewInstance(cfg);
            lcc.setInstanceConfig(cfg);
            InstanceHolder.getInstanceHolder().registry(cfg.getInstanceId(), lcc);
            lcc.init();
            lcc.start();
        });
    }

    private class DefineTimerTask extends TimerTask {
        @Override
        public void run() {
            logger.info("MQ_timer_task start");
            MQBootstrap.this.startup();
        }
    }
}
