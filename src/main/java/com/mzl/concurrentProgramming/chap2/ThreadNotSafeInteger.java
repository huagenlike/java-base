package com.mzl.concurrentProgramming.chap2;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ThreadNotSafeInteger
 * @description: value 是线程不安全的
 * @date 2021/7/2 10:03
 */
public class ThreadNotSafeInteger {

    private int value;

    public int get() {
        return value;
    }

    public void set(int value) {
        this.value = value;
    }
}
