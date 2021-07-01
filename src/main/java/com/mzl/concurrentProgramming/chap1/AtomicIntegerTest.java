package com.mzl.concurrentProgramming.chap1;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lihuagen
 * @version 1.0
 * @className: AtomicIntegerTest
 * @description: TODO
 * @date 2021/6/18 15:47
 */
public class AtomicIntegerTest {
    private static int n = 0;
    // 原子整数
    private static AtomicInteger n2 = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
//        nonAtomicAccumulation();
        AtomicAccumulation();
    }

    /**
     * @param null
     * @description: 非原子性累加，多线程下运行，n不是预期的值
     * @return:
     * @author: lihuagen
     * @time: 2021/6/18 15:52
     */
    public static void nonAtomicAccumulation() throws InterruptedException {
        Thread t1 = new Thread() {
            @Override
            public void run() {
            for (int i = 0; i < 1000; i++) {
                n++;
            }
            }
        };

        Thread t2 = new Thread() {
            @Override
            public void run() {
            for (int i = 0; i < 1000; i++) {
                n++;
            }
            }
        };

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("最终n的值为：" + n);
    }

    /**
     * @param null
     * @description: 原子性累加，多线程下运行，n都是预期的值
     * @return:
     * @author: lihuagen
     * @time: 2021/6/18 15:55
     */
    public static void AtomicAccumulation() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                n2.incrementAndGet();
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                n2.incrementAndGet();
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("最终n2的值为：" + n2.toString());
    }
}
