package com.mzl.concurrentProgramming.chap1;

/**
 * @program: java-base
 * @description:
 * @author: may
 * @create: 2021-06-06 15:49
 * 当线程为了等待一些特定条件的到来时，一般会调用sleep函数、wait系列函数或者join（）函数来阻塞挂起当前线程。
 * 比如一个线程调用了Thread.sleep（3000），那么调用线程会被阻塞，直到3s后才会从阻塞状态变为激活状态。但是有可能在3s内条件已被满足，如果一直等到3s后再返回有点浪费时间，这时候可以调用该线程的interrupt（）方法，强制sleep方法抛出InterruptedException异常而返回，线程恢复到激活状态。
 **/
public class InterruptSleepTest {

    // threadOne线程休眠了2000s，在正常情况下该线程需要等到2000s后才会被唤醒，但是本例通过调用threadOne.interrupt（）方法打断了该线程的休眠，该线程会在调用sleep方法处抛出InterruptedException异常后返回。
    public static void main(String[] args) throws InterruptedException {
        Thread threadOne = new Thread(() -> {
            try {
                System.out.println("theadOne begin sleep form 2000 seconds");
                Thread.sleep(2_000_000L);
                System.out.println("threadOne awaking");
            } catch (InterruptedException e) {
                System.out.println("threadOne is interrupted while sleeping");
                return;
            }
            System.out.println("threadOne-leaving normally");
        });

        // 启动线程
        threadOne.start();

        // 确保子线程进入休眠状态
        Thread.sleep(1000L);

        // 打断子线程的休眠，让子线程从sleep函数返回
        threadOne.interrupt();

        // 等待子线程执行完毕
        threadOne.join();

        System.out.println("main thread is over");
    }
}
