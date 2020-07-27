package com.ibiz.redis.mq.thread;

import com.ibiz.redis.mq.utils.DateUtil;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadFactory;

public class  BossThreadFactory implements ThreadFactory {

    private final ThreadGroup group;
    private final String name;

    public BossThreadFactory(String consumer) {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() :
                Thread.currentThread().getThreadGroup();
        name = "[" + consumer + "-" + DateUtil.formatterNowTime() + "]";
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r,
                name,
                0);
        if (t.isDaemon())
            t.setDaemon(false);
        if (t.getPriority() != Thread.NORM_PRIORITY)
            t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }
}
