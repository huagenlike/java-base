package com.mzl.concurrentProgramming.chap6;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ReentrantReadWriteLockTest
 * @description: 读写锁
 * @date 2021/7/9 15:37
 * (实例类：ReentrantLockListTest)上节介绍了如何使用 ReentrantLock 实现线程安全的 list ，但是由于 ReentrantLock是独占锁，所以在读多写少的情况下性能很差。下面使用 ReentrantReadWri teLock 来改造它，代码如下。
 */
public class ReentrantReadWriteLockTest {
    // 线程不安全的list
    private ArrayList<String> array = new ArrayList<>();
    // 独占锁
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    // 添加元素
    public void add(String e) {
        writeLock.lock();
        try {
            Thread.sleep(1000L);
            array.add(e);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            writeLock.unlock();
        }
    }

    // 删除元素
    public void remove(String e) {
        writeLock.lock();
        try {
            array.remove(e);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            writeLock.unlock();
        }
    }

    // 获取数据
    public String get(int index) {
        readLock.lock();
        try {
            Thread.sleep(1000L);
            return array.get(index);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + array.size());
        } finally {
            readLock.unlock();
        }
    }

    // 执行完耗时【6088】：
    // 因为ReentrantReadWriteLock 中readLock是共享锁，writeLock是排他锁，所以主要耗时在写入（add()）方法
    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        ReentrantReadWriteLockTest test = new ReentrantReadWriteLockTest();
        Thread threadOne = new Thread(() -> {
            System.out.println("begin threadOne");
            test.add("One");
            System.out.println("end threadOne");
        }, "threadOne");

        Thread threadTwo = new Thread(() -> {
            System.out.println("begin threadTwo");
            test.add("Two");
            System.out.println("end threadTwo");
        }, "threadTwo");

        Thread threadThree = new Thread(() -> {
            System.out.println("begin threadThree");
            test.add("Three");
            System.out.println("end threadThree");
        }, "threadThree");

        Thread threadGet = new Thread(() -> {
            System.out.println("begin threadGet");
            System.err.println(test.get(2));
            System.out.println("end threadGet");
        }, "threadGet");

        Thread threadFour = new Thread(() -> {
            System.out.println("begin threadFour");
            test.add("Four");
            System.out.println("end threadFour");
        }, "threadFour");

        // 这样会一个个执行
        /*threadOne.start();
        threadOne.join();

        threadTwo.start();
        threadTwo.join();

        threadThree.start();
        threadThree.join();

        threadGet.start();
        threadGet.join();*/

        // 这种运行方式，线程是同时执行的，多线程下没办法到保证执行顺序，线程 threadGet 可能出现 java.lang.IndexOutOfBoundsException 异常（执行较早，元素还没添加够）
        threadOne.start();
        threadTwo.start();
        threadThree.start();
        threadGet.start();
        threadFour.start();

        threadOne.join();
        threadTwo.join();
        threadThree.join();
        threadGet.join();
        threadFour.join();

        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i ++) {
            threads[i] = new Thread(() -> {
                System.out.println("begin " + Thread.currentThread().getName());
                System.err.println(test.get(1));
                System.out.println("end readThread" + Thread.currentThread().getName());
            }, "readThread" + i);

            threads[i].start();
        }
        for (int i = 0; i < 10; i++) {
            threads[i].join();
        }
        System.out.println(test.array);
        System.out.println(System.currentTimeMillis() - startTime);
    }
}
