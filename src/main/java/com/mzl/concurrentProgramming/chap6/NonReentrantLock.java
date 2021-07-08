package com.mzl.concurrentProgramming.chap6;

import java.io.Serializable;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author lihuagen
 * @version 1.0
 * @className: NonReentrantLock
 * @description: 基于AQS实现自定义同步器
 * @date 2021/7/7 16:26
 */
public class NonReentrantLock implements Lock, Serializable {

    // 内部帮助类
    private static class Sync extends AbstractQueuedSynchronizer {
        // 是否锁已经被持有
        @Override
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        // 如果state为0，则尝试获取锁
        @Override
        protected boolean tryAcquire(int acquires) {
            assert acquires == 1;
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        // 尝试释放锁，设置state为0
        @Override
        protected boolean tryRelease(int releases) {
            assert releases == 1;
            if (getState() == 0) {
                throw new IllegalMonitorStateException();
            }
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        // 提供条件变量接口
        Condition newCondition() {
            return new ConditionObject();
        }
    }

    // 创建一个Sync来做具体的工作
    private final Sync sync = new Sync();

    @Override
    public void lock() {
        sync.acquire(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override
    public void unlock() {
        sync.release(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }

    public boolean isLocked() {
        return sync.isHeldExclusively();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireSharedNanos(1, unit.toNanos(timeout));
    }

    final static NonReentrantLock lock = new NonReentrantLock();
    final static Condition notFull = lock.newCondition();
    final static Condition notEmpty = lock.newCondition();

    final static Queue<String> queue = new LinkedBlockingQueue<>();
    final static int queueSize = 10;

    // 如上代码首先创建了 Nonreentrantlock的一个对象lock,然后调用lock.newCondition创建了两个条件变量,用来进行生产者和消费者线程之间的同步。
    // 在main函数里面,首先创建了 producer生产线程,在线程内部首先调用lock.lock()获取独占锁,然后判断当前队列是否已经满了,如果满了则调用 notEmpty.await()阻塞挂起当前线程。
    // 需要注意的是,这里使用 while而不是if是为了避免虚假唤醒。
    // 如果队列不满则直接向队列里面添加元素,然后调用 notFull.signalAll()唤醒所有因为消费元素而被阻塞的消费线程,最后释放获取的锁。
    //
    // 然后在main函数里面创建了 consumer生产线程,在线程内部首先调用lock.lock()获取独占锁,然后判断当前队列里面是不是有元素,如果队列为空则调用 notFull.await()阻塞挂起当前线程。
    // 需要注意的是,这里使用 while而不是if是为了避免虚假唤醒。
    // 如果队列不为空则直接从队列里面获取并移除元素,然后唤醒因为队列满而被阻塞的生产线程,最后释放获取的锁。
    public static void main(String[] args) {
        Thread producer = new Thread(() -> {
            // 获取独占锁
            lock.lock();
            try {
                // 如果队列满了，则等待
                while (queue.size() == queueSize) {
                    notEmpty.await();
                }
                // 2 添加元素到队列
                queue.add("ele");
                // 3 唤醒消费线程
                notFull.signalAll();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 释放锁
                lock.unlock();
            }
        });

        Thread consumer = new Thread(() -> {
            // 获取独占锁
            lock.lock();
            try {
                // 队列空，则等待
                while (queue.size() == 0) {
                    notFull.await();
                }
                // 2 消费一个元素
                String ele = queue.poll();
                // 3 唤醒生产线程
                notEmpty.signalAll();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 释放锁
                lock.unlock();
            }
        });

        producer.start();
        consumer.start();
    }
}
