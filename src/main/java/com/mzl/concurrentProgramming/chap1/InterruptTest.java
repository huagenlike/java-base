package com.mzl.concurrentProgramming.chap1;

/**
 * @program: java-base
 * @description: 线程中断，Interrupted优雅退出的经典例子
 * @author: may
 * @create: 2021-06-06 15:35
 * Java中的线程中断是一种线程间的协作模式，通过设置线程的中断标志并不能直接终止该线程的执行，而是被中断的线程根据中断状态自行处理。
 * 子线程thread通过检查当前线程中断标志来控制是否退出循环，主线程在休眠1s后调用thread的interrupt（）方法设置了中断标志，所以线程thread退出了循环。
 **/
public class InterruptTest {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println(Thread.currentThread() + "hello");
            }
        });

        // 启动子线程
        thread.start();

        // 主线程中断1s，以便中断前让子线程输出
        Thread.sleep(1000L);

        // 中断子线程
        System.out.println("main thread interrupt thread");
        thread.interrupt();

        // 等待子线程执行完毕
        thread.join();
        System.out.println("main is over");
    }
}
