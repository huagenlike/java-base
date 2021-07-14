package com.mzl.concurrentProgramming.chap10;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author lihuagen
 * @version 1.0
 * @className: SemaphoreTest1
 * @description: Semaphore 简单使用
 * @date 2021/7/14 16:45
 * 同样下面的例子也是在主线程中开启两个子线程让它们执行，等所有子线程执行完毕后主线程再继续向下运行。
 */
public class SemaphoreTest1 {
    // 创建一个Semaphore实例
    private static Semaphore semaphore = new Semaphore(0);

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        // 将线程A添加到线程池
        executorService.submit(() -> {
            try {
                System.out.println(Thread.currentThread() + "over");
                Thread.sleep(2000);
                semaphore.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        // 将线程B添加到线程池
        executorService.submit(() -> {
            try {
                System.out.println(Thread.currentThread() + "over");
                Thread.sleep(2000);
                semaphore.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        // 等待子线程执行完毕，返回
        semaphore.acquire(2);
        System.out.println("all child thread over!");

        // 关闭线程池
        executorService.shutdown();
    }
}
