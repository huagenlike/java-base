package com.mzl.concurrentProgramming.chap2;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ThreadSafeCount
 * @description: TODO
 * @date 2021/7/2 11:01
 */
public class ThreadSafeCount {

    static Long value;

    // 可以去掉synchronized 关键字吗？
    // 不能去掉，需要通过 sunchronized 实现value 的内存可见性
    // 更好的方法，使用非阻塞CAS算法实现的原子性操作类 AtomicLong
    public synchronized Long getValue() {
        return value;
    }

    public synchronized void inc() {
        ++value;
    }
}
