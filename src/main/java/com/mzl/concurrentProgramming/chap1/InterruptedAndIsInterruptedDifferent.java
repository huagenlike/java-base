package com.mzl.concurrentProgramming.chap1;

/**
 * @program: java-base
 * @description: interrupted（）与isInterrupted（）方法的不同之处。
 * @author: may
 * @create: 2021-06-06 15:59
 * void interrupt（）方法：中断线程，
 * isInterrupted（）方法：检测当前线程是否被中断，如果是返回true，否则返回false。
 * boolean interrupted（）方法：检测当前线程是否被中断，如果是返回true，否则返回false。与isInterrupted不同的是，该方法如果发现当前线程被中断，则会清除中断标志，并且该方法是static方法，可以通过Thread类直接调用。
 **/
public class InterruptedAndIsInterruptedDifferent {
    public static void main(String[] args) throws InterruptedException {
        Thread threadOne = new Thread(new Runnable() {
            @Override
            public void run() {
                for (;;) {

                }
            }
        });

        // 启动线程
        threadOne.start();

        // 设置中断标志
        threadOne.interrupt();

        // 获取中断标志
        System.out.println("isInterrupted:" + threadOne.isInterrupted());

        // 获取中断标志并重置
        // 这里虽然调用了threadOne的interrupted()方法，但是获取的是主线程的中断标志，因为主线程是当前线程。
        System.out.println("isInterrupted:" + threadOne.interrupted());

        // 获取中断标志并重置
        // threadOne.interrupted() 和 Thread.interrupted()方法的作用是一样的，目的都是获取当前线程的中断标志。
        System.out.println("isInterrupted:" + Thread.interrupted());

        // 获取中断标志
        System.out.println("isInterrupted:" + threadOne.isInterrupted());

        threadOne.join();

        System.out.println("main thread is over");
    }
}
