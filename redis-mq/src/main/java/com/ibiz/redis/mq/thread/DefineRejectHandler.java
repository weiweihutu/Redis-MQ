package com.ibiz.redis.mq.thread;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 自定义拒绝策略
 * 自定义线程池中记录了提交的任务数,判断是否能继续提交任务并且成功入列
 * 成功入列后要退出
 * 反之休眠等待
 * @auther 喻场
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
