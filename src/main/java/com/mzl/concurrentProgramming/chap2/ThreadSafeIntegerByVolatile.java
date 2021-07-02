package com.mzl.concurrentProgramming.chap2;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ThreadSafeIntegerByVolatile
 * @description: 使用volatile 进行同步
 * @date 2021/7/2 10:06
 * volatile虽然提供了可见性保证，但是不能保证操作的原子性
 */
public class ThreadSafeIntegerByVolatile {

    private volatile int value;

    public int get() {
        return value;
    }

    public void set(int value) {
        this.value = value;
    }
}
