package com.mzl.concurrentProgramming.chap10;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author lihuagen
 * @version 1.0
 * @className: SemaphoreTest2
 * @description: Semaphore 简单使用
 * @date 2021/7/14 16:50
 * 来模拟 CyclicBarrier复 用的功能
 */
public class SemaphoreTest2 {
    private static Semaphore semaphore = new Semaphore(0);

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        // 将线程A添加到线程池
        executorService.submit(() -> {
            try {
                System.out.println(Thread.currentThread() + " A task over");
                Thread.sleep(2000);
                semaphore.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        // 将线程B添加到线程池
        executorService.submit(() -> {
            try {
                System.out.println(Thread.currentThread() + " B task over");
                Thread.sleep(2000);
                semaphore.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // 等待子线程执行完毕，返回
        semaphore.acquire(2);

        // 将线程C添加到线程池
        executorService.submit(() -> {
            try {
                System.out.println(Thread.currentThread() + " C task over");
                Thread.sleep(2000);
                semaphore.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // 将线程D添加到线程池
        executorService.submit(() -> {
            try {
                System.out.println(Thread.currentThread() + " D task over");
                Thread.sleep(2000);
                semaphore.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // 等待子线程执行完毕，返回
        semaphore.acquire(2);

        System.out.println("task is over");

        // 关闭线程池
        executorService.shutdown();
    }
}
