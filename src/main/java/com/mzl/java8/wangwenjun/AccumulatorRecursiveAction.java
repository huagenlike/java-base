package com.mzl.java8.wangwenjun;

import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicInteger;

/***************************************
 * @author:Alex Wang
 * @Date:2016/11/2 QQ:532500648
 * QQ交流群:286081824
 * 没有返回结果
 ***************************************/
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
                AccumulatorHelper.accumulate(data[i]);
            }
        } else {
            // 进行拆分
            int mid = (start + end) / 2;
            AccumulatorRecursiveAction left = new AccumulatorRecursiveAction(start, mid, data);
            AccumulatorRecursiveAction right = new AccumulatorRecursiveAction(mid, end, data);
            left.fork();
            right.fork();
            left.join();
            right.join();
        }
    }

    /**
     * 因为没有返回结果，所以定义了一个类，来用于返回结果
     **/
    static class AccumulatorHelper {

        private static final AtomicInteger result = new AtomicInteger(0);

        static void accumulate(int value) {
            result.getAndAdd(value);
        }

        public static int getResult() {
            return result.get();
        }

        static void rest() {
            result.set(0);
        }
    }
}
