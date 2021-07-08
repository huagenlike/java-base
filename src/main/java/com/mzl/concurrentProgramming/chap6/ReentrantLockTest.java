package com.mzl.concurrentProgramming.chap6;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ReentrantLockTest
 * @description: 重入锁
 * @date 2021/6/18 11:43
 * （1）synchronized是独占锁，加锁和解锁的过程自动进行，易于操作，但不够灵活。ReentrantLock也是独占锁，加锁和解锁的过程需要手动进行，不易操作，但非常灵活。
 * （2）synchronized可重入，因为加锁和解锁自动进行，不必担心最后是否释放锁；ReentrantLock也可重入，但加锁和解锁需要手动进行，且次数需一样，否则其他线程无法获得锁。
 * （3）synchronized不可响应中断，一个线程获取不到锁就一直等着；ReentrantLock可以相应中断。
 * ReentrantLock好像比synchronized关键字没好太多，我们再去看看synchronized所没有的，一个最主要的就是ReentrantLock还可以实现公平锁机制。
 * 什么叫公平锁呢？也就是在锁上等待时间最长的线程将获得锁的使用权。通俗的理解就是谁排队时间最长谁先执行获取锁。
 */
public class ReentrantLockTest {
    // 默认是非公平锁，不需要传false
     private static final Lock nonfairSyncLock = new ReentrantLock();
    // 公平锁
    private static final Lock fairSyncLock = new ReentrantLock(true);

    public static void main(String[] args) {
        // 非公平锁
        /*new Thread(() -> nonfairSyncLockTest(), "线程A").start();
        new Thread(() -> nonfairSyncLockTest(), "线程B").start();
        new Thread(() -> nonfairSyncLockTest(), "线程C").start();
        new Thread(() -> nonfairSyncLockTest(), "线程D").start();
        new Thread(() -> nonfairSyncLockTest(), "线程E").start();*/
        // 公平锁
        new Thread(() -> fairSyncTest(), "线程A").start();
        new Thread(() -> fairSyncTest(), "线程B").start();
        new Thread(() -> fairSyncTest(), "线程C").start();
        new Thread(() -> fairSyncTest(), "线程D").start();
        new Thread(() -> fairSyncTest(), "线程E").start();

    }

    /**
     * @description: 简单使用
     * @param null
     * @return:
     * @author: lihuagen
     * @time: 2021/6/18 14:49
     */
    public static void test() {
        try {
            nonfairSyncLock.lock();
            System.out.println(Thread.currentThread().getName() + "获取了锁");
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName() + "释放了锁");
            nonfairSyncLock.unlock();
        }
    }

    /**
     * @description: 公平锁实现
     * @param null
     * @return:
     * @author: lihuagen
     * @time: 2021/6/18 14:49
     */
    public static void fairSyncTest() {
        for (int i =0; i < 2; i++) {
            try {
                fairSyncLock.lock();
                System.out.println(Thread.currentThread().getName() + "获取了锁");
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                fairSyncLock.unlock();
            }

        }
    }

    /**
     * @description: 公平锁实现
     * @param null
     * @return:
     * @author: lihuagen
     * @time: 2021/6/18 14:49
     */
    public static void nonfairSyncLockTest() {
        for (int i =0; i < 2; i++) {
            try {
                nonfairSyncLock.lock();
                System.out.println(Thread.currentThread().getName() + "获取了锁");
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                nonfairSyncLock.unlock();
            }

        }
    }


}
