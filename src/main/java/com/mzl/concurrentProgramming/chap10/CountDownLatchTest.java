package com.mzl.concurrentProgramming.chap10;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lihuagen
 * @version 1.0
 * @className: Demo2
 * @description: 运动员赛跑实例
 * @date 2021/7/14 9:56
 */
public class CountDownLatchTest {
    /**
     * implement
     * @param args
     */
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        // 围栏
        final CountDownLatch countDownLatch1 = new CountDownLatch(1);
        // 游戏结束时的围栏
        final CountDownLatch countDownLatch2 = new CountDownLatch(3);

        for (int i = 0;i< 3;i++){
            Runnable runnable = () -> {
                try {
                    System.out.println("运动员"+Thread.currentThread().getName()+"等待信号枪");
                    // Block the thread before running. Wait until count of countDownLatch1 is 0
                    countDownLatch1.await();
                    System.out.println("运动员"+Thread.currentThread().getName()+"跑");
                    Thread.sleep(10);
                    System.out.println("运动员"+Thread.currentThread().getName()+"跑完");

                    countDownLatch2.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
            executorService.execute(runnable);
        }

        try {
            Thread.sleep(5000);

            System.out.println("裁判"+Thread.currentThread().getName()+"即将发射信号枪");
            // 递归减一直到计数为0
            countDownLatch1.countDown();
            System.out.println("裁判"+Thread.currentThread().getName()+"鸣响信号枪，等待运动员跑完");

            Thread.sleep(3000);
            // 继续执行以下代码之前，等待 countDownLatch2 的计数减少到 0
            countDownLatch2.await();
            System.out.println("运动员跑完，裁判"+Thread.currentThread().getName()+"统计排名");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }
}
