package com.mzl.java8.wangwenjun;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

import static com.mzl.java8.wangwenjun.CollectorsAction.menu;

/***************************************
 * @author:Alex Wang
 * @Date:2016/10/28 QQ:532500648
 * QQ交流群:286081824
 ***************************************/
public class CollectorsAction2 {

    public static void main(String[] args) {
        testGroupingByConcurrentWithFunction();
        testGroupingByConcurrentWithFunctionAndCollector();
        testGroupingByConcurrentWithFunctionAndSupplierAndCollector();
        testJoining();
        testJoiningWithDelimiter();
        testJoiningWithDelimiterAndPrefixAndSuffix();
        testMapping();
        testMaxBy();
        testMinBy();
    }

    /**
     * 根据类型对菜进行分类，key是类型，value是List<Dish>（并发）
     **/
    private static void testGroupingByConcurrentWithFunction() {
        System.out.println("testGroupingByConcurrentWithFunction");

        ConcurrentMap<Dish.Type, List<Dish>> collect = menu.stream().collect(Collectors.groupingByConcurrent(Dish::getType));
        Optional.ofNullable(collect.getClass()).ifPresent(System.out::println);
        Optional.ofNullable(collect).ifPresent(System.out::println);
    }

    /**
     * 根据类型对菜进行分类，key是类型，value是平均卡路里（并发）
     **/
    private static void testGroupingByConcurrentWithFunctionAndCollector() {
        System.out.println("testGroupingByConcurrentWithFunctionAndCollector");
        ConcurrentMap<Dish.Type, Double> collect = menu.stream()
                .collect(Collectors.groupingByConcurrent(Dish::getType, Collectors.averagingInt(Dish::getCalories)));
        Optional.ofNullable(collect).ifPresent(System.out::println);
    }

    /**
     * 根据类型对菜进行分类，key是类型，value是平均卡路里，并指定特定的数据类型，这里指定的是ConcurrentSkipListMap（并发）
     **/
    private static void testGroupingByConcurrentWithFunctionAndSupplierAndCollector() {
        System.out.println("testGroupingByConcurrentWithFunctionAndSupplierAndCollector");
        ConcurrentMap<Dish.Type, Double> collect = menu.stream()
                .collect(Collectors.groupingByConcurrent(Dish::getType, ConcurrentSkipListMap::new, Collectors.averagingInt(Dish::getCalories)));
        Optional.of(collect.getClass()).ifPresent(System.out::println);
        Optional.ofNullable(collect).ifPresent(System.out::println);
    }

    /**
     * joining 收集器 支持灵活的参数配置，可以指定字符串连接时的 分隔符，前缀 和 后缀 字符串。
     * 对菜的名字进行拼接
     **/
    private static void testJoining() {
        System.out.println("testJoining");
        Optional.of(menu.stream().map(Dish::getName).collect(Collectors.joining()))
                .ifPresent(System.out::println);
    }

    /**
     * joining 收集器 支持灵活的参数配置，可以指定字符串连接时的 分隔符，前缀 和 后缀 字符串。
     * 对菜的名字进行拼接，并用逗号分隔
     **/
    private static void testJoiningWithDelimiter() {
        System.out.println("testJoiningWithDelimiter");
        Optional.of(menu.stream().map(Dish::getName).collect(Collectors.joining(",")))
                .ifPresent(System.out::println);
    }

    /**
     * joining 收集器 支持灵活的参数配置，可以指定字符串连接时的 分隔符，前缀 和 后缀 字符串。
     * 对菜的名字进行拼接，并用逗号分隔，在最开始加 Names[，最后加 ]
     **/
    private static void testJoiningWithDelimiterAndPrefixAndSuffix() {
        System.out.println("testJoiningWithDelimiterAndPrefixAndSuffix");
        Optional.of(menu.stream().map(Dish::getName).collect(Collectors.joining(",", "Names[", "]")))
                .ifPresent(System.out::println);
    }

    /**
     * mapping方法：Function类型的mapper和Collector类型的downstream。
     * 通过注释可以看到方法是通过参数mapper函数来处理List中的每一个数据，然后用downstream来将处理后的数据收集起来。
     * 这里是处理菜单中每一个菜名，并用逗号进行拼接
     **/
    private static void testMapping() {
        System.out.println("testMapping");
        Optional.of(menu.stream().collect(Collectors.mapping(Dish::getName, Collectors.joining(","))))
                .ifPresent(System.out::println);
    }

    /**
     * 通过比较卡路里，获取最大值的菜
     **/
    private static void testMaxBy() {
        System.out.println("testMaxBy");
        menu.stream().collect(Collectors.maxBy(Comparator.comparingInt(Dish::getCalories)))
                .ifPresent(System.out::println);
    }

    /**
     * 通过比较卡路里，获取最小值的菜
     **/
    private static void testMinBy() {
        System.out.println("testMinBy");
        menu.stream().collect(Collectors.minBy(Comparator.comparingInt(Dish::getCalories)))
                .ifPresent(System.out::println);
    }
}
