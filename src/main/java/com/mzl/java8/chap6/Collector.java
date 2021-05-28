package com.mzl.java8.chap6;


import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author may
 * @version 1.0
 * @className: Collector
 * @description: 收集器
 * @Param [T：是流中要收集的项目的泛型 A：是累加器的类型，累加器是在收集过程中用于累积部分结果的对象, R：是收集操作得到的对象（通常但并不一定是集合）的类型]
 * @date 2021/4/29 14:08
 */
public interface Collector <T, A, R> {
    /**
     * @Author lhg
     * @Description 提供者
     * @Date 14:13 2021/4/29
     * @Param []
     * @return java.util.function.BiConsumer<A,T>
     **/
    Supplier <A> supplier();
    /**
     * @Author lhg
     * @Description 累加器
     * @Date 14:13 2021/4/29
     * @Param []
     * @return java.util.function.BiConsumer<A,T>
     **/
    BiConsumer<A, T> accumulator();
    /**
     * @Author lhg
     * @Description 整理器
     * @Date 14:14 2021/4/29
     * @Param []
     * @return java.util.function.Function<A,R>
     **/
    Function<A, R> finisher();
    /**
     * @Author lhg
     * @Description 结合
     * @Date 14:14 2021/4/29
     * @Param []
     * @return java.util.function.Function<A,R>
     **/
    BinaryOperator<A> combiner();
    /**
     * @Author lhg
     * @Description 特征
     * @Date 14:14 2021/4/29
     * @Param []
     * @return java.util.function.Function<A,R>
     **/
    Set<java.util.stream.Collector.Characteristics> characteristics();
}
