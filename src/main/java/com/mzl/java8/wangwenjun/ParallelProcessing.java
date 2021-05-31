package com.mzl.java8.wangwenjun;

import java.util.function.Function;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/***************************************
 * @author:Alex Wang
 * @Date:2016/10/30 QQ:532500648
 * QQ交流群:286081824
 ***************************************/
public class ParallelProcessing {

    public static void main(String[] args) {
        System.out.println("The best process time(normalAdd)=>" + measureSumPerformance(ParallelProcessing::normalAdd, 100_000_000) + " MS");
        System.out.println("The best process time(iterateStream)=>" + measureSumPerformance(ParallelProcessing::iterateStream, 10_000_000) + " MS");
        System.out.println("The best process time(parallelStream)=>" + measureSumPerformance(ParallelProcessing::parallelStream, 10_000_000) + " MS");
        System.out.println("The best process time(parallelStream2)=>" + measureSumPerformance(ParallelProcessing::parallelStream2, 10_000_000) + " MS");
        System.out.println("The best process time(parallelStream3)=>" + measureSumPerformance(ParallelProcessing::parallelStream3, 100_000_000) + " MS");
    }

    /**
     * 测量对前 n 个自然数求和的函数的性能
     * 这个方法接受一个函数和一个long作为参数。它会对传给方法的long应用函数10次，记录每次执行的时间（以毫秒为单位），并返回最短的一次执行时间。
     * 假设你把先前开发的所有方法都放进了一个名为ParallelStreams的类，你就可以用这个框架来测试顺序加法器函数对前一千万个自然数求和要用多久：
     */
    private static long measureSumPerformance(Function<Long, Long> adder, long limit) {
        long fastest = Long.MAX_VALUE;
        for (int i = 0; i < 10; i++) {
            long startTimestamp = System.currentTimeMillis();
            long result = adder.apply(limit);
            long duration = System.currentTimeMillis() - startTimestamp;
//            System.out.println("The result of sum=>" + result);
            if (duration < fastest) fastest = duration;
        }

        return fastest;
    }

    /**
     * 顺序版求和
     */
    private static long iterateStream(long limit) {
        return Stream.iterate(1L, i -> i + 1)
                .limit(limit).reduce(0L, Long::sum);
    }

    /**
     * 并行流求和
     * 求和方法的并行版本比顺序版本要慢很多。你如何解释这个意外的结果呢？这里实际上有两个问题：
     *  iterate生成的是装箱的对象，必须拆箱成数字才能求和；
     *  我们很难把iterate分成多个独立块来并行执行。
     */
    private static long parallelStream(long limit) {
        return Stream.iterate(1L, i -> i + 1).parallel()
                .limit(limit).reduce(0L, Long::sum);
    }

    private static long parallelStream2(long limit) {
        return Stream.iterate(1L, i -> i + 1).mapToLong(Long::longValue).parallel()
                .limit(limit).reduce(0L, Long::sum);
    }

    /**
     * 并行流，
     */
    private static long parallelStream3(long limit) {
        return LongStream.rangeClosed(1, limit).parallel().reduce(0L, Long::sum);
    }

    /**
     * 正常添加
     */
    private static long normalAdd(long limit) {
        long result = 0L;
        for (long i = 1L; i < limit; i++) {
            result += i;
        }
        return result;
    }
}