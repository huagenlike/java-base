package com.mzl.concurrentProgramming.chap1;

/**
 * @program: java-base
 * @description:
 * @author: may
 * @create: 2021-06-06 15:19
 * 当一个线程调用yield方法时，实际就是在暗示线程调度器当前线程请求让出自己的CPU使用，但是线程调度器可以无条件忽略这个暗示。
 **/
public class YieldTest implements Runnable {

    YieldTest() {
        // 创建并启动线程
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        for (int i = 0; i < 5; i++) {
            // 当i=0时，让出cpu执行权，放弃时间片，进行下一轮调度
            if (i % 5 ==0) {
                System.out.println(Thread.currentThread() + "yield cpu...");
                // 当前线程让出cpu执行权，放弃时间片，进行下一轮调度
                // 解开Thread.yield（）注释再执行，Thread.yield（）方法生效了，三个线程分别在i=0时调用了Thread.yield（）方法，所以三个线程自己的两行输出没有在一起，因为输出了第一行后当前线程让出了CPU执行权。
                // Thread.yield();
            }
        }
        System.out.println(Thread.currentThread() + " is over");
    }

    public static void main(String[] args) {
        new YieldTest();
        new YieldTest();
        new YieldTest();
    }
}
