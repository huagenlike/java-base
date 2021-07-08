package com.mzl.concurrentProgramming.chap6;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ReentrantLockListTest
 * @description: 使用 ReentrantLockList 来实现一个简单的线程安全的List
 * @date 2021/7/8 10:49
 * 假如线程 Thread1、Thread2和Thread3同时尝试获取独占锁ReentrantLock,假设Thread1获取到了,则Thread2和Thread3就会被转换为Node节点并被放入ReentrantLock对应的AQS阻塞队列,而后被阻塞挂起,
 * 假设Thread1获取锁后调用了对应的锁创建的条件变量1,那么Thread1就会释放获取到的锁,然后当前线程就会被转换为Node节点插入条件变量1的条件队列。
 * 由于 Thread1释放了锁,所以阻塞到AQS队列里面的Thread2和Thread3就有机会获取到该锁,假如使用的是公平策略,那么这时候 Thread2会获取到该锁,从而从AQS队列里面移除Thread2对应的Node节点。
 */
public class ReentrantLockListTest {

    // 线程不安全的List
    private ArrayList<String> array = new ArrayList<>();
    // 独占锁
    private volatile ReentrantLock lock = new ReentrantLock();

    // 添加元素
    public void add(String e) {
        System.out.println("thread name is " + Thread.currentThread().getName());
        lock.lock();
        try {
            Thread.sleep(2000L);
            array.add(e);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    // 删除元素
    public void remove(String e) {
        System.out.println("thread name is " + Thread.currentThread().getName());
        lock.lock();
        try {
            array.remove(e);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    // 获取数据
    public String get(int index) {
        System.out.println("thread name is " + Thread.currentThread().getName());
        lock.lock();
        try {
            Thread.sleep(3000L);
            return array.get(index);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + array.size());
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReentrantLockListTest test = new ReentrantLockListTest();
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

        System.out.println(test.array);
    }

}
