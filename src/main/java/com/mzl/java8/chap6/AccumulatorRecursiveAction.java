package com.mzl.java8.chap6;

import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: java-base
 * @description: 分而治之（手动），无返回值
 * @author: may
 * @create: 2021-05-22 21:15
 **/
public class AccumulatorRecursiveAction extends RecursiveAction {

    private final int start;
    private final int end;
    private final int[] data;
    private final int LIMIT = 3;

    public AccumulatorRecursiveAction(int start, int end, int[] data) {
        this.start = start;
        this.end = end;
        this.data = data;
    }

    @Override
    protected void compute() {
        if ((end - start) <= LIMIT) {
            for (int i = start; i < end; i++) {
                // 用于解决没有返回值
                AccumulatorHelper.accumulate(data[i]);
            }
        } else {
            int mid = (start+ end) / 2;
            AccumulatorRecursiveAction left = new AccumulatorRecursiveAction(start, mid, data);
            AccumulatorRecursiveAction right = new AccumulatorRecursiveAction(mid, end, data);
            left.fork();
            right.fork();
            left.join();
            right.join();
        }
    }

    /**
     * 用于解决没有返回值
     */
    static class AccumulatorHelper {
        private static final AtomicInteger result = new AtomicInteger(0);

        static void accumulate(int value) {
            result.getAndAdd(value);
        }

        public static int getResult() {
            return result.get();
        }

        // 用于重复利用
        static void rest() {
            result.set(0);
        }
    }
}
