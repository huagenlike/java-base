package com.mzl.java8.wangwenjun;

/***************************************
 * @author:Alex Wang
 * @Date:2016/10/29 QQ:532500648
 * QQ交流群:286081824
 ***************************************/

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

import static com.mzl.java8.wangwenjun.CollectorsAction.menu;


public class CollectorsAction3 {

    public static void main(String[] args) {
        testPartitioningByWithPredicate();
        testPartitioningByWithPredicateAndCollector();
        testReducingBinaryOperator();
        testReducingBinaryOperatorAndIdentiy();
        testReducingBinaryOperatorAndIdentiyAndFunction();
        testSummarizingDouble();
        testSummarizingLong();
        testSummarizingInt();
    }

    /**
     * 根据条件进行分组，key为true 或 false
     **/
    private static void testPartitioningByWithPredicate() {
        System.out.println("testPartitioningByWithPredicate");
        Map<Boolean, List<Dish>> collect = menu.stream().collect(Collectors.partitioningBy(Dish::isVegetarian));
        Optional.of(collect).ifPresent(System.out::println);

    }

    /**
     * 根据条件进行分组，key为true 或 false，并计算出每组的平均值
     * partitioningBy(Predicate<? super T> predicate, Collector<? super T, A, D> downstream)：
     * 第一个参数：由一个谓词（返回一个布尔值的函数）作为分类函数，它称分区函数。分区函数返回一个布尔值，这意味着得到的分组Map的键类型是Boolean，于是它最多可以分为两组——true是一组， false是一组。
     * 第二个参数：接口Collector<T，A，R>：一种可变的归约运算，它将输入元素累积到一个可变结果容器中，在处理完所有输入元素之后，可以有选择地将累积的结果转换为最终表示形式。还原操作可以顺序或并行执行。
     *  T:归约运算的输入元素的类型。
     *  A:归约运算的可变累积类型。
     *  R:归约运算的结果类型。
     **/
    private static void testPartitioningByWithPredicateAndCollector() {
        System.out.println("testPartitioningByWithPredicateAndCollector");
        Map<Boolean, Double> collect = menu.stream().collect(Collectors.partitioningBy(Dish::isVegetarian, Collectors.averagingInt(Dish::getCalories)));
        Optional.of(collect).ifPresent(System.out::println);
    }

    /**
     * 通过比较卡路里，获取最大值的菜
     **/
    private static void testReducingBinaryOperator() {
        System.out.println("testReducingBinaryOperator");
        menu.stream().collect(
                Collectors.reducing(
                        BinaryOperator.maxBy(
                                Comparator.comparingInt(Dish::getCalories)
                        )
                )
        ).ifPresent(System.out::println);
    }

    /**
     * 求卡路里总和
     **/
    private static void testReducingBinaryOperatorAndIdentiy() {
        System.out.println("testReducingBinaryOperatorAndIdentiy");
        Integer result = menu.stream().map(Dish::getCalories).collect(Collectors.reducing(0, (d1, d2) -> d1 + d2));
        System.out.println(result);
    }

    /**
     * 求卡路里总和
     **/
    private static void testReducingBinaryOperatorAndIdentiyAndFunction() {
        System.out.println("testReducingBinaryOperatorAndIdentiyAndFunction");
        Integer result = menu.stream().collect(Collectors.reducing(0, Dish::getCalories, (d1, d2) -> d1 + d2));
        System.out.println(result);
    }

    /**
     * 总结诠释，获取菜总数、总卡路里、最小卡路里、平均卡路里、最大卡路里，返回DoubleSummaryStatistics
     **/
    private static void testSummarizingDouble() {
        System.out.println("testSummarizingDouble");
        Optional.of(menu.stream().collect(Collectors.summarizingDouble(Dish::getCalories)))
                .ifPresent(System.out::println);
    }

    /**
     * 总结诠释，获取菜总数、总卡路里、最小卡路里、平均卡路里、最大卡路里，返回LongSummaryStatistics
     **/
    private static void testSummarizingLong() {
        System.out.println("testSummarizingLong");
        Optional.of(menu.stream().collect(Collectors.summarizingLong(Dish::getCalories)))
                .ifPresent(System.out::println);
    }

    /**
     * 总结诠释，获取菜总数、总卡路里、最小卡路里、平均卡路里、最大卡路里，返回IntSummaryStatistics
     **/
    private static void testSummarizingInt() {
        System.out.println("testSummarizingLong");
        Optional.of(menu.stream().collect(Collectors.summarizingInt(Dish::getCalories)))
                .ifPresent(System.out::println);
    }
}