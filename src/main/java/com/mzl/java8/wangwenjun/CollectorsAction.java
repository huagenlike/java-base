package com.mzl.java8.wangwenjun;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/***************************************
 * @author:Alex Wang
 * @Date:2016/10/27 QQ:532500648
 * QQ交流群:286081824
 ***************************************/
public class CollectorsAction {


    public  static List<Dish> menu = Arrays.asList(
            new Dish("pork", false, 800, Dish.Type.MEAT),
            new Dish("beef", false, 700, Dish.Type.MEAT),
            new Dish("chicken", false, 400, Dish.Type.MEAT),
            new Dish("french fries", true, 530, Dish.Type.OTHER),
            new Dish("rice", true, 350, Dish.Type.OTHER),
            new Dish("season fruit", true, 120, Dish.Type.OTHER),
            new Dish("pizza", true, 550, Dish.Type.OTHER),
            new Dish("prawns", false, 300, Dish.Type.FISH),
            new Dish("salmon", false, 450, Dish.Type.FISH));

    public static void main(String[] args) {
        testAveragingDouble();
        testAveragingInt();
        testAveragingLong();
        testCollectingAndThen();
        testCounting();
        testGroupingByFunction();
        testGroupingByFunctionAndCollector();
        testGroupingByFunctionAndSupplierAndCollector();
        testSummarizingInt();
    }

    /**
     * 计算平均值
     **/
    private static void testAveragingDouble() {
        System.out.println("testAveragingDouble");
        Optional.ofNullable(menu.stream().collect(Collectors.averagingDouble(Dish::getCalories)))
                .ifPresent(System.out::println);
    }

    /**
     * 计算平均值
     **/
    private static void testAveragingInt() {
        System.out.println("testAveragingInt");
        Optional.ofNullable(menu.stream().collect(Collectors.averagingInt(Dish::getCalories)))
                .ifPresent(System.out::println);
    }

    /**
     * 计算平均值
     **/
    private static void testAveragingLong() {
        System.out.println("testAveragingLong");
        Optional.ofNullable(menu.stream().collect(Collectors.averagingLong(Dish::getCalories)))
                .ifPresent(System.out::println);
    }

    /**
     * collectingAndThen 此方法是在进行归纳动作结束之后，对归纳的结果进行二次处理。
     * 这里是计算平均值后，打印一段话
     **/
    private static void testCollectingAndThen() {
        System.out.println("testCollectingAndThen");
        Optional.ofNullable(menu.stream().collect(Collectors.collectingAndThen(Collectors.averagingInt(Dish::getCalories), a -> "The Average Calories is->" + a)))
                .ifPresent(System.out::println);
/*
        List<Dish> list = menu.stream().filter(d -> d.getType().equals(Dish.Type.MEAT))
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
        list.add(new Dish("", false, 100, Dish.Type.OTHER));
        System.out.println(list);*/
    }

    /**
     * 计算菜单内猜的总数
     **/
    private static void testCounting() {
        System.out.println("testCounting");
        Optional.of(menu.stream().collect(Collectors.counting())).ifPresent(System.out::println);
    }

    /**
     * 根据类型对菜进行分类，key是类型，value是List<Dish>
     **/
    private static void testGroupingByFunction() {
        System.out.println("testGroupingByFunction");
        Optional.of(menu.stream().collect(Collectors.groupingBy(Dish::getType))).ifPresent(System.out::println);
    }

    /**
     * 根据类型对菜进行分类，key是类型，value是平均卡路里
     **/
    private static void testGroupingByFunctionAndCollector() {
        System.out.println("testGroupingByFunctionAndCollector");
        Optional.of(menu.stream().collect(Collectors.groupingBy(Dish::getType, Collectors.averagingInt(Dish::getCalories))))
                .ifPresent(System.out::println);
    }

    /**
     * 根据类型对菜进行分类，key是类型，value是平均卡路里，并指定特定的数据类型，这里指定的是TreeMap
     **/
    private static void testGroupingByFunctionAndSupplierAndCollector() {
        System.out.println("testGroupingByFunctionAndSupplierAndCollector");
        Map<Dish.Type, Double> map = menu.stream().collect(Collectors.groupingBy(Dish::getType, TreeMap::new, Collectors.averagingInt(Dish::getCalories)));

        Optional.of(map.getClass()).ifPresent(System.out::println);
        Optional.of(map).ifPresent(System.out::println);
    }

    /**
     * 总结诠释，获取菜总数、总卡路里、最小卡路里、平均卡路里、最大卡路里
     **/
    private static void testSummarizingInt() {
        System.out.println("testSummarizingInt");
        IntSummaryStatistics result = menu.stream().collect(Collectors.summarizingInt(Dish::getCalories));
        Optional.of(result).ifPresent(System.out::println);
    }
}