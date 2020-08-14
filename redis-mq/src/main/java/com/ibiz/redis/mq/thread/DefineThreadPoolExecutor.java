package com.ibiz.redis.mq.thread;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义线程池
 *
 * @auther 喻场
 * @date 2020/7/2316:28
 */
public class DefineThreadPoolExecutor extends ThreadPoolExecutor {
    /**当前线程池总提交数量
     * BOSS线程需要通过 count来控制到redis取待消费任务
     * WORK线程需要通过 count 和 restrict 判断任务是否能入队列*/
    private final AtomicInteger count = new AtomicInteger(0);
    /**当线程池和队列都满了,线程休眠时间*/
    private long sleep;
    /**当前线程池最大容量 最大线程数 + 队列容量*/
    private final int restrict;
    /**当前线程池总提交数量
     * BOSS线程需要通过 count来控制到redis取待消费任务
     * WORK线程需要通过 count 和 restrict 判断任务是否能入队列*/
    public boolean nextSubmit() {
        return count.get() < restrict;
    }
    public void increment() {
        count.getAndIncrement();
    }
    public void decrement() {
        count.decrementAndGet();
    }
    /**
     * 根据消费者id创建线程池,如果已经创建则直接返回,反之新创建一个
     * @instanceId redis实例id
     * @param consumerId    消费者id
     * @param corePoolSize  核心线程数
     * @param maximumPoolSize   最大线程数
     * @param size 队列最大容量
     * @return  ThreadPoolExecutor
     */
    public DefineThreadPoolExecutor(int corePoolSize, int maximumPoolSize, int size, String consumerId, long sleep) {
        super(corePoolSize, maximumPoolSize, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(size), new BossThreadFactory(consumerId), new DefineRejectHandler());
        restrict = maximumPoolSize + size;
        this.sleep = sleep;
    }
    public long getSleep() {
        return sleep;
    }
    /**消费一个任务后提交数量减1*/
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        decrement();
    }
}
