package com.mzl.concurrentProgramming.chap1;

/**
 * @program: java-base
 * @description:
 * @author: may
 * @create: 2021-06-06 14:21
 * 线程A调用线程B的join方法后会被阻塞，当其他线程调用了线程A的interrupt（）方法中断了线程A时，线程A会抛出InterruptedException异常而返回。下面通过一个例子来加深理解。
 **/
public class JoinInterruptTest {
    public static void main(String[] args) throws InterruptedException {
        Thread threadOne = new Thread(() -> {
            System.out.println("threadOne begin run!");
            for (; ; ) {

            }
        });

        // 获取主线程
        final Thread mainThread = Thread.currentThread();

        // 线程two
        Thread threadTwo = new Thread(() -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 中断主线程
            mainThread.interrupt();
        });

        threadOne.start();
        threadTwo.start();

        try {
            // 主线程调用threadOne的join方法阻塞自己等待线程threadOne执行完毕
            threadOne.join();
        } catch (InterruptedException e) {
            System.out.println("main thread:" + e);
        }
    }
}
