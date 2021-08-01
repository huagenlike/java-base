package com.mzl.concurrentProgramming.chap11;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ThreadLocalTest
 * @description: 使用ThreadLocal不当可能会导致内存泄漏
 * @date 2021/7/16 13:58
 */
public class ThreadLocalTest {
    static class LocalVariable {
        private Long[] a = new Long[1024 * 1024];
    }

    // 1
    final static ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5, 5, 1, TimeUnit.MINUTES, new LinkedBlockingDeque<>());

    // 2
    final static ThreadLocal<LocalVariable> localVariable = new ThreadLocal<>();

    public static void main(String[] args) {
        for (int i = 0; i < 50; ++i) {
            poolExecutor.execute(() -> {
                // 4
                localVariable.set(new LocalVariable());

                // 5
                System.out.println("use local varaible");

                 localVariable.remove();
            });
        // 6
            System.out.println("pool execute over");
        }
    }
}
