package com.mzl.concurrentProgramming.chap2;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ThreadNotSafeCount
 * @description: TODO
 * @date 2021/7/2 10:32
 */
public class ThreadNotSafeCount {
    private Long value;

    public Long getValue() {
        return value;
    }

    public void inc() {
        ++value;
    }
}
