package com.mzl.concurrentProgramming.chap9;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ScheduledMultiThreadTool
 * @description: 定时任务，并增加执行次数限制
 * @date 2021/7/13 17:00
 * 本以使用 AtomicInteger，不去使用锁，
 */
public class ScheduledMultiThreadTool1 {

    private static AtomicInteger count = new AtomicInteger(1);
    MyTimerTask myTimerTask = new MyTimerTask();
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(2);

    public void start() {
        try {
            //一秒执行一次
            scheduled.scheduleAtFixedRate(myTimerTask, 0, 1, TimeUnit.SECONDS);
            scheduled.scheduleAtFixedRate(myTimerTask, 0, 1, TimeUnit.SECONDS);
            scheduled.scheduleAtFixedRate(myTimerTask, 0, 1, TimeUnit.SECONDS);
            while (!scheduled.isTerminated()) {
                if (count.get() > 20) {
                    scheduled.shutdown();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Finished all threads");
    }

    private class MyTimerTask implements Runnable {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + "第 " + count.get() + " 次执行任务,count=" + count.get());
            count.incrementAndGet();
        }
    }

    public static void main(String[] args) {
        new ScheduledMultiThreadTool1().start();
    }
}
