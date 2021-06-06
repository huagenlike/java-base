package com.mzl.concurrentProgramming.chap1;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: java-base
 * @description:
 * @author: may
 * @create: 2021-06-06 14:40
 * 当一个执行中的线程调用了Thread的sleep方法后，调用线程会暂时让出指定时间的执行权，也就是在这期间不参与CPU的调度，但是该线程所拥有的监视器资源，比如锁还是持有不让出的。
 * 指定的睡眠时间到了后该函数会正常返回，线程就处于就绪状态，然后参与CPU的调度，获取到CPU资源后就可以继续运行了。
 * 如果在睡眠期间其他线程调用了该线程的interrupt（）方法中断了该线程，则该线程会在调用sleep方法的地方抛出InterruptedException异常而返回。
 **/
public class SleepTest {
    // 创建一个独创锁
    private static final Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        // 创建线程A
        Thread threadA = new Thread(() -> {
            // 获取独占锁
            lock.lock();
            try {
                System.out.println("child threadA is in sleep");
                Thread.sleep(10000L);
                System.out.println("child threadA is in awaked");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // 释放锁
                lock.unlock();
            }
        });

        Thread threadB = new Thread(() -> {
            // 获取独占锁
            lock.lock();
            try {
                System.out.println("child threadB is in sleep");
                Thread.sleep(10000L);
                System.out.println("child threadB is in awaked");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // 释放锁
                lock.unlock();
            }
        });

        threadA.start();
        threadB.start();
    }

}
