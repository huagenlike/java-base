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
public class InterruptedAndIsInterruptedDifferentChange {
    public static void main(String[] args) throws InterruptedException {
        Thread threadOne = new Thread(() -> {
            System.out.println(Thread.currentThread().interrupted());
            // 中断标志为true时会退出循环，并且清除中断标志
            while (!Thread.currentThread().interrupted()) {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("....");
            }
            System.out.println("threadOne isInterrupted:" + Thread.currentThread().isInterrupted());
        });

        // 启动线程
        threadOne.start();

        // 设置中断标志
        // 调用interrupted()方法后中断标志被清除了
        threadOne.interrupt();

        threadOne.join();

        System.out.println("main thread is over");
    }
}
