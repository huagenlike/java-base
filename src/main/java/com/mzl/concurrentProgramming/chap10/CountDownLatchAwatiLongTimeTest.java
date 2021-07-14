package com.mzl.concurrentProgramming.chap10;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author lihuagen
 * @version 1.0
 * @className: Demo2
 * @description: 运动员赛跑实例
 * @date 2021/7/14 9:56
 */
public class CountDownLatchAwatiLongTimeTest {
    /**
     * implement
     * @param args
     */
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        // 围栏
        final CountDownLatch countDownLatch = new CountDownLatch(5);


        for (int i = 0;i< 4;i++){
            Runnable runnable = () -> {
                try {
                    System.out.println("线程"+Thread.currentThread().getName()+"等待5s");
                    Thread.sleep(5000);
                    countDownLatch.countDown();
                    // 获取当前计数器的值，也就是 AQS tate 值，一般在测试时使用该方法。
                    System.out.println(countDownLatch.getCount());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
            executorService.execute(runnable);
        }
        try {
            System.out.println("线程等待开始");
            // 当所有线程者调用了 countDownLatch 对象 countDown 方法后，也就是计数器值为0时，这时候会返回true ；
            // 设置的 timeout 时间到了，因为超时而返回false
            // 其他线程调用了当前线程的 interrupt()方法中断了当前线程，当前线程会抛出 InterruptedException 异常，然后返回。
            countDownLatch.await(4, TimeUnit.SECONDS);
            System.out.println("线程等待结束");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }
}
