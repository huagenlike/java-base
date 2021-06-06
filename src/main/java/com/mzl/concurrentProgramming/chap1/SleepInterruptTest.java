package com.mzl.concurrentProgramming.chap1;

/**
 * @program: java-base
 * @description:
 * @author: may
 * @create: 2021-06-06 15:14
 **/
public class SleepInterruptTest {
    public static void main(String[] args) throws InterruptedException {
        Thread threadA = new Thread(() -> {
            try {
                System.out.println("child thread is in sleep");
                Thread.sleep(10000L);
                System.out.println("child thread is in awaked");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        // 启动线程
        threadA.start();
        // 主线程休眠2秒
        // 如果在调用Thread.sleep（long millis）时为millis参数传递了一个负数，则会抛出IllegalArgumentException异常
        Thread.sleep(2000L);
        // 主线程中断子线程
        // 子线程在睡眠期间，主线程中断了它，所以子线程在调用sleep方法处抛出了InterruptedException异常。
        threadA.interrupt();
    }
}
