package com.mzl.concurrentProgramming.chap6;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ReentrantLockInterruptTest
 * @description: 重入锁响应中断
 * @date 2021/6/18 15:10
 * 在这里我们定义了两个锁lock1和lock2。然后使用两个线程thread1和thread2构造死锁场景。
 * 正常情况下，这两个线程相互等待获取资源而处于死循环状态。但是我们此时thread1中断，另外一个线程就可以获取资源，正常地执行了。
 */
public class ReentrantLockInterruptTest {
    static Lock lock1 = new ReentrantLock();
    static Lock lock2 = new ReentrantLock();

    public static void main(String[] args) {
        Thread thread1 = new Thread(new ThreadDemo(lock1, lock2), "线程1");
        Thread thread2 = new Thread(new ThreadDemo(lock2, lock1), "线程2");
        thread1.start();
        thread2.start();
        // 响应中断demo时，打开注释
//        thread1.interrupt();
    }

}

