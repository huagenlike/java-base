package com.mzl.concurrentProgramming.chap6;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ReentrantLockDemo
 * @description: 公平锁
 * @date 2021/7/8 11:29
 */
public class FairSyncReentrantLockDemo implements Runnable {
    // 设置公平锁，默认是非公平
    private static ReentrantLock lock = new ReentrantLock(true);

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
        FairSyncReentrantLockDemo demo = new FairSyncReentrantLockDemo();
        Thread thread1 = new Thread(demo);
        Thread thread2 = new Thread(demo);

        thread1.start();
        thread2.start();
    }
}
