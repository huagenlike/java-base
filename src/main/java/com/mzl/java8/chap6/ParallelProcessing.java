package com.mzl.java8.chap6;

import java.util.function.Function;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * @program: java-base
 * @description: 并行处理
 * @author: may
 * @create: 2021-05-22 20:21
 **/
public class ParallelProcessing {

    public static void main(String[] args) {
        // 获取cpu核数
        System.out.println(Runtime.getRuntime().availableProcessors());
        // 并发流使用的默认线程数等于你机器的处理器核心数。
        // 通过这个方法可以修改这个值，这是全局属性。
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "20");
        System.out.println("The best process time(normalAdd) =>" + measureSumPerformance(ParallelProcessing::normalAdd, 100_000_000) + "MS");
//        System.out.println("The best process time(iterateStream) =>" + measureSumPerformance(ParallelProcessing::iterateStream, 10_000_000) + "MS");
//        System.out.println("The best process time(parallelStream) =>" + measureSumPerformance(ParallelProcessing::parallelStream, 10_000_000) + "MS");
//        System.out.println("The best process time(parallelStream2) =>" + measureSumPerformance(ParallelProcessing::parallelStream2, 10_000_000) + "MS");
        System.out.println("The best process time(parallelStream3) =>" + measureSumPerformance(ParallelProcessing::parallelStream3, 100_000_000) + "MS");
    }

    private static long measureSumPerformance(Function<Long, Long> adder, long limit) {
        long fasteat = Long.MAX_VALUE;
        for (int i = 0; i < 10; i++) {
            long startTimestamp = System.currentTimeMillis();
            long result = adder.apply(limit);
            long duration = System.currentTimeMillis() - startTimestamp;
            // System.out.println("The result of sum =>" + result);
            if (duration < fasteat) fasteat = duration;
        }
        return fasteat;
    }

    private static long iterateStream(long limit) {
        return Stream.iterate(1L, i -> i + 1).limit(limit).reduce(0L, Long::sum);
    }

    private static long parallelStream(long limit) {
        return Stream.iterate(1L, i -> i + 1).parallel().limit(limit).reduce(0L, Long::sum);
    }

    private static long parallelStream2(long limit) {
        // 拆箱提高效率
        return Stream.iterate(1L, i -> i + 1).mapToLong(Long::longValue).parallel().limit(limit).reduce(0L, Long::sum);
    }

    private static long parallelStream3(long limit) {
        return LongStream.rangeClosed(1, limit).parallel().reduce(0L, Long::sum);
        // return LongStream.rangeClosed(1, limit).parallel().sum();
    }

    private static long normalAdd(long limit) {
        long result = 0L;
        for (long i = 1L; i < limit; i++) {
            result += i;
        }
        return result;
    }
}
