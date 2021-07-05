package com.mzl.concurrentProgramming.chap4;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author lihuagen
 * @version 1.0
 * @className: AtomicTest
 * @description: AtomicLong 样例
 * @date 2021/7/5 14:13
 * 如上代码中的两个线程各自统计自己所持数据中0的个数，每当找到一个0就会调用 AtomicLong 原子性递增方法。
 * 在没有原子类的情况下， 实现计数器需要使用一定的同步措施， 如使用 synchronized 关键字等，但是这些都是阻塞算法，对性能有一定损耗，
 * 而本章介绍的这些原子操作使用 CAS 非阻塞算法，性能更好，但是在高并发情况下AtomicLong 还会存在性能问题 JDK 提供了一个在高并发下性能更好的 LongAdder 类
 */
public class AtomicTest {
    // 创建Long型原子计数器
    private static AtomicLong atomicLong = new AtomicLong();
    // 创建数据源
    private static Integer[] arrayOne = new Integer[]{0,1,2,3,0,5,6,0,56,0};
    private static Integer[] arrayTwo = new Integer[]{10,1,2,3,0,5,6,0,56,0};

    public static void main(String[] args) throws InterruptedException {
        //线程one统计数组arrayOne中的0个数
        Thread threadOne = new Thread(() -> {
            int size = arrayOne.length;
            for (int i = 0; i < size; i++) {
                if (arrayOne[i].intValue() == 0){
                    atomicLong.incrementAndGet();
                }
            }
        });

        // 线程two统计数据arrayTwo中0的个数
        Thread threadTwo = new Thread(() -> {
            int size = arrayTwo.length;
            for (int i = 0; i < size; i++) {
                if (arrayTwo[i].intValue() == 0) {
                    atomicLong.incrementAndGet();
                }
            }
        });

        // 启动子线程
        threadOne.start();
        threadTwo.start();

        // 等待线程执行完毕
        threadOne.join();
        threadTwo.join();

        System.out.println("count 0:" + atomicLong.get());
    }
}
