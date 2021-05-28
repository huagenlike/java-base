package com.mzl.java8.wangwenjun;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/***************************************
 * @author:Alex Wang
 * @Date:2016/10/27 QQ:532500648
 * QQ交流群:286081824
 * reduce 操作可以实现从Stream中生成一个值，其生成的值不是随意的，而是根据指定的计算模型。比如，之前提到count、min和max方
 * 法，因为常用而被纳入标准库中。事实上，这些方法都是reduce操作。
 ***************************************/
public class CollectorReduce {
    public static void main(String[] args) {
        List<Dish> menu = Arrays.asList(
                new Dish("pork", false, 800, Dish.Type.MEAT),
                new Dish("beef", false, 700, Dish.Type.MEAT),
                new Dish("chicken", false, 400, Dish.Type.MEAT),
                new Dish("french fries", true, 530, Dish.Type.OTHER),
                new Dish("rice", true, 350, Dish.Type.OTHER),
                new Dish("season fruit", true, 120, Dish.Type.OTHER),
                new Dish("pizza", true, 550, Dish.Type.OTHER),
                new Dish("prawns", false, 300, Dish.Type.FISH),
                new Dish("salmon", false, 450, Dish.Type.FISH));

        // 统计素食的总数
        long count = menu.stream().filter(d -> d.isVegetarian()).count();
        // 统计素食的总数
        Long collect = menu.stream().filter(d -> d.isVegetarian()).collect(Collectors.counting());

        // 获取菜单里面卡路里最高值
        Optional<Integer> maxCalories = menu.stream().map(Dish::getCalories).reduce(Integer::max);
        maxCalories.ifPresent(System.out::println);

        // reduce 操作可以实现从Stream中生成一个值，其生成的值不是随意的，而是根据指定的计算模型
        // 获取菜单里面卡路里最高的菜
        Optional<Dish> maxCaloriesDish = menu.stream().reduce((d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2);
        maxCaloriesDish.ifPresent(System.out::println);

        // 获取菜单里面卡路里最高的菜
        Optional<Dish> maxCaloriesCollect = menu.stream().collect(Collectors.maxBy(Comparator.comparingInt(Dish::getCalories)));
        maxCaloriesCollect.ifPresent(System.out::println);

        // collectingAndThen 此方法是在进行归纳动作结束之后，对归纳的结果进行二次处理。
        // 这里的二次处理是进行菜的总数统计，计算出菜的总数
        Integer collect1 = menu.stream().collect(Collectors.collectingAndThen(toList(), t -> t.size()));

        // 根据菜的类型进行分组
        Map<Dish.Type, List<Dish>> collect2 = menu.stream().collect(Collectors.groupingBy(Dish::getType));

        // 根据猜的类型进行分组，并算出平均值
        Map<Dish.Type, Double> collect3 = menu.stream().collect(Collectors.groupingBy(Dish::getType, Collectors.averagingInt(Dish::getCalories)));

    }
}
