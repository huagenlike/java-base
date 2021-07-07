package com.mzl.concurrentProgramming.chap6;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lihuagen
 * @version 1.0
 * @className: AbstractQueuedSynchronizerTest
 * @description: TODO
 * @date 2021/7/7 16:05
 * 代码(1)创建了一个独占锁 ReentrantLock对象，ReentrantLock是基于AQS实现的锁。
 * 代码(2)使用创建的Lock对象的 new Condition()方法创建了一个ConditionObject变量,这个变量就是Lock锁对应的一个条件变量。需要注意的是,一个Lock对象可以创建多个条件变量。
 * 代码(3)首先获取了独占锁,
 * 代码(4)则调用了条件变量的await()方法阻塞挂起了当前线程。当其他线程调用条件变量的 signal方法时,被阻塞的线程才会从 await处返回。需要注意的是,和调用 Object的wait方法一样,如果在没有获取到锁前调用了条件变量的await方法则会抛出 java.lang.IllegalMonitorStateException异常代码
 * 代码(5)则释放了获取的锁。
 */
public class AbstractQueuedSynchronizerTest {

    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock(); // 1
        // lock.newCondition()的作用其实是 new了一个在AQS内部声明的ConditionObject对象，ConditionObject是AQS的内部类，可以访问AQS内部的变量（例如状态变 state ）和方法。
        // 在每个条件变量内部都维护了一个条件队列，用来存放调用条件变await()方法时被阻塞的线程。注意这个条件队列和AQS队列不是一回事。
        Condition condition = lock.newCondition(); // 2

        // 其实这里的 Lock 对象等价于 synchronized 加上共享变量，调用lock.lock()方法就相当于进入了synchronized块（获取了共享变量的内置锁）
        lock.lock(); // 3

        try {
            System.out.println("begin wait");
            condition.await(); // 4
            System.out.println("end wait");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 调用 lock.unlock()方法就相当于退出synchronized块，
            // 调用条件变await()方法就相当于调用共享变wait()方法，
            // 调用条件变量的signal()方法就相当于调用共享变量的notify()方法。
            // 调用条件变量signalAll()方法就相当于调用共享变量notifyAll()方法。
            lock.unlock(); // 5
        }

        lock.lock(); // 6
        try {
            System.out.println("begin signal");
            condition.signal(); // 7
            System.out.println("end signal");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock(); // 8
        }
    }
}
