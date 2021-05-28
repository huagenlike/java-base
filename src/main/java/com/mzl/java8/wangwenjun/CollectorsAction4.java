package com.mzl.java8.wangwenjun;

/***************************************
 * @author:Alex Wang
 * @Date:2016/10/29 QQ:532500648
 * QQ交流群:286081824
 ***************************************/

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

import static com.mzl.java8.wangwenjun.CollectorsAction.menu;

public class CollectorsAction4 {

    public static void main(String[] args) {
        testSummingDouble();
        testSummingLong();
        testSummingInt();
        testToCollection();
        testToConcurrentMap();
        testToConcurrentMapWithBinaryOperator();
        testToConcurrentMapWithBinaryOperatorAndSupplier();

        testToList();
        testToSet();

        testToMap();
        testToMapWithBinaryOperator();
        testToMapWithBinaryOperatorAndSupplier();
    }

    /**
     * 求卡路里总和，返回Double
     **/
    private static void testSummingDouble() {
        System.out.println("testSummingDouble");
        Optional.of(menu.stream().collect(Collectors.summingDouble(Dish::getCalories))).ifPresent(System.out::println);

        Optional.of(menu.stream().map(Dish::getCalories).mapToInt(Integer::intValue).sum()).ifPresent(System.out::println);
    }

    /**
     * 求卡路里总和，返回Long
     **/
    private static void testSummingLong() {
        System.out.println("testSummingLong");
        Optional.of(menu.stream().collect(Collectors.summingLong(Dish::getCalories)))
                .ifPresent(System.out::println);
    }

    /**
     * 求卡路里总和，返回Integer
     **/
    private static void testSummingInt() {
        System.out.println("testSummingInt");
        Optional.of(menu.stream().collect(Collectors.summingInt(Dish::getCalories)))
                .ifPresent(System.out::println);
    }

    /**
     * 过滤出卡路里大于600的，并用LinkedList的数据结构收集
     **/
    private static void testToCollection() {
        System.out.println("testToCollection");
        Optional.of(menu.stream().filter(d -> d.getCalories() > 600).collect(Collectors.toCollection(LinkedList::new)))
                .ifPresent(System.out::println);
    }

    /**
     * toConcurrentMap – >是一个并发收集器
     * 将流转换成一个ConcurrentHashMap数据结构，key是名称，value是卡路里
     **/
    private static void testToConcurrentMap() {
        System.out.println("testToConcurrentMap");
        Optional.of(menu.stream()
                .collect(Collectors.toConcurrentMap(Dish::getName, Dish::getCalories)))
                .ifPresent(v -> {
                    System.out.println(v);
                    System.out.println(v.getClass());
                });
    }

    /**
     * Type:Total
     * 类型为map的key，并计算每个类型有多少菜
     */
    private static void testToConcurrentMapWithBinaryOperator() {
        System.out.println("testToConcurrentMapWithBinaryOperator");
        Optional.of(menu.stream()
                .collect(Collectors.toConcurrentMap(Dish::getType, v -> 1L, (a, b) -> a + b)))
                .ifPresent(v -> {
                    System.out.println(v);
                    System.out.println(v.getClass());
                });
    }

    /**
     * Type:Total
     * 类型为map的key，并计算每个类型有多少菜，同时指定ConcurrentSkipListMap的数据结构收集
     */
    private static void testToConcurrentMapWithBinaryOperatorAndSupplier() {
        Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();

        System.out.println("testToConcurrentMapWithBinaryOperatorAndSupplier");
        Optional.of(menu.stream()
                .collect(Collectors.toConcurrentMap(Dish::getType, v -> 1L, (a, b) -> a + b, ConcurrentSkipListMap::new)))
                .ifPresent(v -> {
                    System.out.println(v);
                    System.out.println(v.getClass());
                });
    }

    /**
     * 过滤出是素食的菜，将List转成List数据结构收集，默认是ArrayList
     */
    private static void testToList() {
        Optional.of(menu.stream().filter(Dish::isVegetarian).collect(Collectors.toList()))
                .ifPresent(r -> {
                    System.out.println(r.getClass());
                    System.out.println(r);
                });
    }

    /**
     * 过滤出是素食的菜，将List转成Set数据结构收集，默认是HashSet
     */
    private static void testToSet() {
        Optional.of(menu.stream().filter(Dish::isVegetarian).collect(Collectors.toSet()))
                .ifPresent(r -> {
                    System.out.println(r.getClass());
                    System.out.println(r);
                });
    }

    /**
     * 将List 转成map
     * Collectors.toMap 有三个重载方法：
     *  keyMapper：Key 的映射函数
     *  valueMapper：Value 的映射函数
     *  mergeFunction：当 Key 冲突时，调用的合并方法
     *  mapSupplier：Map 构造器，在需要返回特定的 Map 时使用
     * collectingAndThen，将map 转换为 synchronizedMap()返回由指定映射支持的同步（线程安全的）映射
     */
    private static void testToMap() {
        System.out.println("testToMap");
        Optional.of(menu.stream().collect(
                Collectors.collectingAndThen(Collectors.toMap(Dish::getName, Dish::getCalories), Collections::synchronizedMap)))
                .ifPresent(v -> {
                    System.out.println(v);
                    System.out.println(v.getClass());
                });

        Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
        for (Map.Entry<Thread, StackTraceElement[]> entry : allStackTraces.entrySet()) {
            Thread key = entry.getKey();
            StackTraceElement[] value = entry.getValue();

            if (key.getId() != Thread.currentThread().getId())
                continue;
            System.out.println("=========="+key.getName());
            for (StackTraceElement ste : value) {
                if(ste.isNativeMethod())
                    continue;
                System.out.println(ste.getClassName());
                System.out.println("isNativeMethod>" + ste.isNativeMethod());
                System.out.println(ste.getMethodName());
                System.out.println(ste.getLineNumber());
                System.out.println(ste.getFileName());
            }
        }
    }

    /**
     * Type:Total
     */
    private static void testToMapWithBinaryOperator() {
        System.out.println("testToMapWithBinaryOperator");
        Optional.of(menu.stream()
                .collect(Collectors.toMap(Dish::getType, v -> 1L, (a, b) -> a + b)))
                .ifPresent(v -> {
                    System.out.println(v);
                    System.out.println(v.getClass());
                });
    }

    /**
     * Type:Total
     */
    private static void testToMapWithBinaryOperatorAndSupplier() {
        System.out.println("testToMapWithBinaryOperatorAndSupplier");
        Optional.of(menu.stream()
                .collect(Collectors.toMap(Dish::getType, v -> 1L, (a, b) -> a + b, Hashtable::new)))
                .ifPresent(v -> {
                    System.out.println(v);
                    System.out.println(v.getClass());
                });
    }

}
