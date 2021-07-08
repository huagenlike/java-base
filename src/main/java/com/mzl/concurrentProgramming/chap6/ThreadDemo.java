package com.mzl.concurrentProgramming.chap6;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import static com.mzl.concurrentProgramming.chap6.ReentrantLockInterruptTest.lock1;
import static com.mzl.concurrentProgramming.chap6.ReentrantLockInterruptTest.lock2;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ThreadDemo
 * @description: 重入锁响应中断/限时等待
 * @date 2021/6/18 15:12
 */
public class ThreadDemo implements Runnable{
    Lock firstLock;
    Lock secondLock;

    public ThreadDemo(Lock firstLock, Lock secondLock) {
        this.firstLock = firstLock;
        this.secondLock = secondLock;
    }

    @Override
    public void run() {
        // 响应中断
        /*try {
            firstLock.lockInterruptibly();
            TimeUnit.MILLISECONDS.sleep(50);
            secondLock.lockInterruptibly();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            firstLock.unlock();
            secondLock.unlock();
            System.out.println(Thread.currentThread().getName()+"获取到了资源，正常结束");
        }*/

        // 限时等待
        // 你好，限时等待这块代码中是个if判断 而不是while，怎么还不停的执行了呢
        // 这是因为两个线程都是休眠10秒，可能休眠十秒之后两个线程又发生死锁，就这样不断的去运行一直等到有一个时间差，此时一个线程休眠十秒之后正好可以获取到另外一个线程的资源。 你可以想象成两匹马再跑，休眠十秒之后可能两匹马还在并行跑，就这样不断的去跑，一直等两匹马不并行
        try {
            if (!lock1.tryLock()) {
                TimeUnit.MILLISECONDS.sleep(10);
            }
            if (!lock2.tryLock()) {
                TimeUnit.MILLISECONDS.sleep(10);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            firstLock.unlock();
            secondLock.unlock();
            System.out.println(Thread.currentThread().getName() + "正常结束");
        }
    }
}
