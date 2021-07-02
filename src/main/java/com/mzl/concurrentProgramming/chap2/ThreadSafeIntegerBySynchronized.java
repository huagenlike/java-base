package com.mzl.concurrentProgramming.chap2;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ThreadSafeInteger
 * @description: 使用synchronized 关键字进行同步的方式
 * @date 2021/7/2 10:04
 */
public class ThreadSafeIntegerBySynchronized {

    private int value;

    public synchronized int get() {
        return value;
    }

    public synchronized void set(int value) {
        this.value = value;
    }
}
