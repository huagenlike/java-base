package com.mzl.concurrentProgramming.chap9;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ScheduledMultiThreadTool
 * @description: 定时任务，并增加执行次数限制
 * @date 2021/7/13 17:00
 * 限制程序执行的次数：
 * 如果是单线程，那么可以直接定义一个静态变量count，每执行一次，count加一，如果count大于某个值就调用shutdown或者shutdownNow函数；
 * 如果是多线程，稍微要复杂一点，但是原理也是一样的。定义一个静态变量count，每执行一个也是count加一，只不过在执行加一操作之前需要加锁，执行完之后需要解锁
 */
public class ScheduledMultiThreadTool {

    private static Integer count =1;
    MyTimerTask myTimerTask = new MyTimerTask();
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(2);

    public void start(){
        try {
            //一秒执行一次
            scheduled.scheduleAtFixedRate(myTimerTask, 0,1, TimeUnit.SECONDS);
            scheduled.scheduleAtFixedRate(myTimerTask, 0,1, TimeUnit.SECONDS);
            scheduled.scheduleAtFixedRate(myTimerTask, 0,1, TimeUnit.SECONDS);
            while (!scheduled.isTerminated()){
                lock.readLock().lock();
                if (count >20){
                    scheduled.shutdown();
                }
                lock.readLock().unlock();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("Finished all threads");
    }
    private class MyTimerTask implements Runnable {
        @Override
        public void run(){
            lock.writeLock().lock();
            System.out.println("第 "+count+ " 次执行任务,count="+count);
            count ++;
            lock.writeLock().unlock();
        }
    }

    public static void main(String[] args) {
        new ScheduledMultiThreadTool().start();
    }
}
