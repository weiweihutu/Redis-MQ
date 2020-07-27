package com.ibiz.redis.mq.thread;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 自定义拒绝策略
 * 虽然在BOSS线程会通过线程池count和restrict控制取待处理任务
 * 不排除临界点情况会进入这里,为防止任务丢失,再尝试把任务入列
 * 成功入列后要退出
 * @auther yc
 * @date 2020/7/2316:36
 */
public class DefineRejectHandler implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        for (;;) {
            DefineThreadPoolExecutor exe = (DefineThreadPoolExecutor) executor;
            try {
                /**成功入队 break*/
                if (exe.nextSubmit() && exe.getQueue().add(r)) {
                    break;
                }
                Thread.sleep(exe.getSleep());
            } catch (Exception e) {
            }
        }
    }
}
