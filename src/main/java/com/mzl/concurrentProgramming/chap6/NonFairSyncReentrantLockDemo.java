package com.mzl.concurrentProgramming.chap6;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ReentrantLockDemo
 * @description: 非公平锁
 * @date 2021/7/8 11:29
 */
public class NonFairSyncReentrantLockDemo implements Runnable {
    // 默认是非公平
    private static ReentrantLock lock = new ReentrantLock();

    @Override
    public void run() {
        while (true) {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " get lock");
                Thread.sleep(1000L);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        NonFairSyncReentrantLockDemo demo = new NonFairSyncReentrantLockDemo();
        Thread thread1 = new Thread(demo);
        Thread thread2 = new Thread(demo);
        Thread thread3 = new Thread(demo);
        Thread thread4 = new Thread(demo);

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
    }
}
