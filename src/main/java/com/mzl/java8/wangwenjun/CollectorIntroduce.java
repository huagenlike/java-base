package com.mzl.java8.wangwenjun;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/***************************************
 * @author:Alex Wang
 * @Date:2016/10/25 QQ:532500648
 * QQ交流群:286081824
 ***************************************/
public class CollectorIntroduce {

    public static void main(String[] args) {
        List<Apple> list = Arrays.asList(new Apple("green", 150)
                , new Apple("yellow", 120)
                , new Apple("green", 170)
                , new Apple("green", 150)
                , new Apple("yellow", 120)
                , new Apple("green", 170));

        // 查询出绿色苹果
        List<Apple> greenList = list.stream().filter(a -> a.getColor().equals("green")).collect(Collectors.toList());
        // 使用Optional，ofNullable()方法，当参数为null时，不会抛出异常
        Optional.ofNullable(greenList).ifPresent(System.out::println);
        Optional.ofNullable(groupByNormal(list)).ifPresent(System.out::println);
        System.out.println("===================================================");
        Optional.ofNullable(groupByFunction(list)).ifPresent(System.out::println);
        System.out.println("===================================================");
        Optional.ofNullable(groupByCollector(list)).ifPresent(System.out::println);
    }

    /**
     * 苹果按颜色分组，结果是map，颜色为key，List<Apple>为value
     * 正常分组，不使用Optional
     **/
    private static Map<String, List<Apple>> groupByNormal(List<Apple> apples) {
        Map<String, List<Apple>> map = new HashMap<>();
        for (Apple a : apples) {
            List<Apple> list = map.get(a.getColor());
            if (null == list) {
                list = new ArrayList<>();
                map.put(a.getColor(), list);
            }
            list.add(a);
        }
        return map;
    }
    /**
     * 苹果按颜色分组，结果是map，颜色为key，List<Apple>为value
     * java8新特性，并行流，使用Optional判空
     **/
    private static Map<String, List<Apple>> groupByFunction(List<Apple> apples) {
        Map<String, List<Apple>> map = new HashMap<>();
        apples.parallelStream().forEach(a -> {
            List<Apple> colorList = Optional.ofNullable(map.get(a.getColor())).orElseGet(() -> {
                List<Apple> list = new ArrayList<>();
                map.put(a.getColor(), list);
                return list;
            });
            colorList.add(a);
        });
        return map;
    }
    /**
     * 苹果按颜色分组，结果是map，颜色为key，List<Apple>为value
     * java8新特性，并行流，直接使用分组
     **/
    private static Map<String, List<Apple>> groupByCollector(List<Apple> apples) {
        return apples.parallelStream().collect(groupingBy(Apple::getColor));
    }
}