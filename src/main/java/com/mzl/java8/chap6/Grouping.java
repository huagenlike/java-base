package com.mzl.java8.chap6;

import java.util.*;
import java.util.stream.Collectors;

import static com.mzl.java8.chap6.Dish.menu;

/**
 * @program: java-base
 * @description: 分组
 * @author: may
 * @create: 2021-04-27 21:35
 **/
public class Grouping {
    public static void main(String[] args) {
        // 分组
        Map<Dish.Type, List<Dish>> dishesByType = menu.stream().collect(Collectors.groupingBy(Dish::getType));
        System.out.println(dishesByType);

        // 在分组过程中对流中的项目进行分类
        Map<CaloricLevel, List<Dish>> dishesByCaloricLevel = menu.stream().collect(Collectors.groupingBy(dish -> {
            if (dish.getCalories() <= 400) {
                return CaloricLevel.DIET;
            } else if (dish.getCalories() <= 700) {
                return CaloricLevel.NORMAL;
            } else {
                return CaloricLevel.FAT;
            }
        }));
        System.out.println(dishesByCaloricLevel);

        // 多级分组
        Map<Dish.Type, Map<CaloricLevel, List<Dish>>> dishesByTypeCaloricLevel = menu.stream().collect(
            Collectors.groupingBy(Dish::getType, // ←─一级分类函数
                Collectors.groupingBy(dish -> {// ←─二级分类函数
                    if (dish.getCalories() <= 400) {
                        return CaloricLevel.DIET;
                    } else if (dish.getCalories() <= 700) {
                        return CaloricLevel.NORMAL;
                    } else {
                        return CaloricLevel.FAT;
                    }
                } )
            )
        );
        System.out.println(dishesByTypeCaloricLevel);

        // 要数一数菜单中每类菜有多少个，可以传递counting收集器作为groupingBy收集器的第二个参数
        // 还要注意，普通的单参数groupingBy(f)（其中f是分类函数）实际上是groupingBy(f, toList())的简便写法。
        Map<Dish.Type, Long> typesCount = menu.stream().collect(Collectors.groupingBy(Dish::getType, Collectors.counting()));
        System.out.println(typesCount);

        // 把前面用于查找菜单中热量最高的菜肴的收集器改一改，按照菜的类型分类，每个分类中热量最高的菜
        // 这个Map中的值是Optional，因为这是maxBy工厂方法生成的收集器的类型，但实际上，如果菜单中没有某一类型的Dish，这个类型就不会对应一个Optional. empty()值，而且根本不会出现在Map的键中。
        // groupingBy收集器只有在应用分组条件后，第一次在流中找到某个键对应的元素时才会把键加入分组Map中。这意味着Optional包装器在这里不是很有用，因为它不会仅仅因为它是归约收集器的返回类型而表达一个最终可能不存在却意外存在的值。
        Map<Dish.Type, Optional<Dish>> mostCaloricByType = menu.stream()
                .collect(Collectors.groupingBy(Dish::getType,
                        Collectors.maxBy(Comparator.comparingInt(Dish::getCalories))));
        System.out.println(mostCaloricByType);

        // 1.把收集器的结果转换为另一种类型
        // 你可以使用Collectors.collectingAndThen工厂方法返回的收集器，这个工厂方法接受两个参数——要转换的收集器以及转换函数，并返回另一个收集器。
        // reducing收集器永远都不会返回Optional.empty()。其结果是下面的Map：
        Map<Dish.Type, Dish> mostCaloricByType1 = menu.stream()
                .collect(Collectors.groupingBy(Dish::getType, // ←─分类函数
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparingInt(Dish::getCalories)), // ←─包装后的收集器
                                        Optional::get))); // ←─转换函数
        System.out.println(mostCaloricByType1);

        // 2.与groupingBy联合使用的其他收集器的例子
        // 通过groupingBy工厂方法的第二个参数传递的收集器将会对分到同一组中的所有流元素执行进一步归约操作。
        // 例如，你还重用求出所有菜肴热量总和的收集器，不过这次是对每一组Dish求和
        Map<Dish.Type, Integer> totalCaloriesByType =
                menu.stream().collect(Collectors.groupingBy(Dish::getType,
                        Collectors.summingInt(Dish::getCalories)));
        System.out.println(totalCaloriesByType);

        // 然而常常和groupingBy联合使用的另一个收集器是mapping方法生成的。
        // 这个方法接受两个参数：一个函数对流中的元素做变换，另一个则将变换的结果对象收集起来。其目的是在累加之前对每个输入元素应用一个映射函数，这样就可以让接受特定类型元素的收集器适应不同类型的对象。
        // 我们来看一个使用这个收集器的实际例子。比方说你想要知道，对于每种类型的Dish，菜单中都有哪些CaloricLevel。我们可以把groupingBy和mapping收集器结合起来，如下所示：
        Map<Dish.Type, Set<CaloricLevel>> caloricLevelsByType =
                menu.stream().collect(
                        Collectors.groupingBy(Dish::getType, Collectors.mapping(
                                dish -> {
                                    if (dish.getCalories() <= 400) {
                                        return CaloricLevel.DIET;
                                    } else if (dish.getCalories() <= 700) {
                                        return CaloricLevel.NORMAL;
                                    } else {
                                        return CaloricLevel.FAT;
                                    }
                                },
                                Collectors.toSet())));
        System.out.println(caloricLevelsByType);

        // 请注意在上一个示例中，对于返回的Set是什么类型并没有任何保证。但通过使用toCollection，你就可以有更多的控制。例如，你可以给它传递一个构造函数引用来要求HashSet：
        Map<Dish.Type, Set<CaloricLevel>> caloricLevelsByType1 =
                menu.stream().collect(
                        Collectors.groupingBy(Dish::getType, Collectors.mapping(
                                dish -> {
                                    if (dish.getCalories() <= 400) {
                                        return CaloricLevel.DIET;
                                    } else if (dish.getCalories() <= 700) {
                                        return CaloricLevel.NORMAL;
                                    } else {
                                        return CaloricLevel.FAT;
                                    }
                                },
                                Collectors.toCollection(HashSet::new) ))); // 可以是你想要的Set类型
        System.out.println(caloricLevelsByType1);
    }

    /**
     * 热量不到400卡路里的菜划分为“低热量”（diet），
     * 热量400到700卡路里的菜划为“普通”（normal），
     * 高于700卡路里的划为“高热量”（fat）。
     */
    public enum CaloricLevel { DIET, NORMAL, FAT }
}
