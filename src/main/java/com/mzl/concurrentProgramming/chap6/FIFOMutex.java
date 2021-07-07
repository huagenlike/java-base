package com.mzl.concurrentProgramming.chap6;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

/**
 * @author lihuagen
 * @version 1.0
 * @className: FIFOMutex
 * @description: 先进先出的锁
 * @date 2021/7/7 14:05
 * 这是一个先进先出的锁,也就是只有队列的首元素可以获取锁。
 * 在代码(1)处,如果当前线程不是队首或者当前锁已经被其他线程获取,则调用park方法挂起自己。
 * 然后在代码(2)处判断,如果park方法是因为被中断而返回,则忽略中断,并且重置中断标志,做个标记,然后再次判断当前线程是不是队首元素或者当前锁是否己经被其他线程获取,如果是则继续调用park方法挂起自己。
 * 然后在代码(3)中,判断标记,如果标记为tue则中断该线程,这个怎么理解呢?其实就是其他线程中断了该线程,虽然我对中断信号不感兴趣,忽略它,但是不代表其他线程对该标志不感兴趣,所以要恢复下。
 *
 */
public class FIFOMutex {
    private final AtomicBoolean locked = new AtomicBoolean(false);
    // 线程先进先出队列
    private final Queue<Thread> waiters = new ConcurrentLinkedDeque<>();

    public void lock() {
        boolean wasInterrupted = false;
        Thread current = Thread.currentThread();
        //把当前线程加入队列
        waiters.add(current);

        // 只有队首的线程可以获取锁（1）
        //如果当前线程不是第一个:不是第一个肯定，肯定有其他线程在持有资源（保证公平性）
        //即使是第一个：加锁失败,即使是第一个线程，如果上一个线程没有释放锁 ，那么也应该进入等待
        while (waiters.peek() != current || !locked.compareAndSet(false, true)) {
            LockSupport.park(this);
            if (Thread.interrupted()) { // （2）
                wasInterrupted = true;
            }
        }

        //可以把第一个线程移除了，因为已经占用了资源
        waiters.remove();
        if (wasInterrupted) { // （3）
            current.interrupt();
        }
    }

    public void unlock() {
        //释放锁，其他的线程 此时也不能占用资源
        locked.set(false);
        //此时线程才可以真正执行
        LockSupport.unpark(waiters.peek());
    }

    public static void main(String[] args) {
        Queue<String> waiters = new ConcurrentLinkedDeque<>();

        waiters.add("1");
        waiters.add("2");
        waiters.add("3");
        waiters.add("4");

        System.out.println(waiters);
        System.out.println(waiters.remove());
        System.out.println(waiters.remove());
        System.out.println(waiters);
    }
}
