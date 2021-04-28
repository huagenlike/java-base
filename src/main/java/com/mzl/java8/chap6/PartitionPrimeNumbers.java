package com.mzl.java8.chap6;

import org.springframework.util.comparator.Comparators;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.mzl.java8.chap6.Dish.menu;

/**
 * @program: java-base
 * @description: 分区
 * @author: may
 * @create: 2021-04-28 20:30
 **/
public class PartitionPrimeNumbers {
    public static void main(String[] args) {
        // 分区
        Map<Boolean, List<Dish>> partitionedMenu = menu.stream().collect(Collectors.partitioningBy(Dish::isVegetarian)); //←─分区函数
        System.out.println(partitionedMenu);

        List<Dish> vegetarianDishes = partitionedMenu.get(true);
        System.out.println(vegetarianDishes);

        List<Dish> vegetarianDishes1 = menu.stream().filter(Dish::isVegetarian).collect(Collectors.toList());
        System.out.println(vegetarianDishes1);

        // 素食和非素食中再根据类型再分组
        Map<Boolean, Map<Dish.Type, List<Dish>>> vegetarianDishesByType =
                menu.stream().collect(
                        Collectors.partitioningBy(Dish::isVegetarian, // ←─分区函数
                                Collectors.groupingBy(Dish::getType))); // ←─第二个收集器
        System.out.println(vegetarianDishesByType);

        // 素食和非素食中热量最高的菜
        Map<Boolean, Dish> mostCaloricPartitionedByVegetarian =
                menu.stream().collect(
                        Collectors.partitioningBy(Dish::isVegetarian,
                                Collectors.collectingAndThen(
                                        Collectors.maxBy(Comparator.comparingInt(Dish::getCalories)),
                                        Optional::get)));
        System.out.println(mostCaloricPartitionedByVegetarian);

        // 根据是否素食分区，根据卡路里大于500进行分区
        Map<Boolean, Map<Boolean, List<Dish>>> collect = menu.stream().collect(Collectors.partitioningBy(Dish::isVegetarian,
                Collectors.partitioningBy(d -> d.getCalories() > 500)));
        System.out.println(collect);

        // 根据是否素食分区，统计每个分区的总数
        Map<Boolean, Long> collect1 = menu.stream().collect(Collectors.partitioningBy(Dish::isVegetarian,
                Collectors.counting()));
        System.out.println(collect1);

        // 质数和非质数
        PartitionPrimeNumbers partitionPrimeNumbers = new PartitionPrimeNumbers();
        Map<Boolean, List<Integer>> booleanListMap = partitionPrimeNumbers.partitionPrimes(10);
        System.out.println(booleanListMap);

        // 把流中所有项目收集到一个List
        // menu Arrays.asList，实质上还是数组，Dish的toString方法是打印name，所以打印dishes是一个name的数组
        List<Dish> dishes = menu.stream().collect(Collectors.toList());
        System.out.println(dishes);

        // 把流中所有项目收集到一个Set，删除重复项
        Set<Dish> disheSet = menu.stream().collect(Collectors.toSet());
        System.out.println(disheSet);

        // 把流中所有项目收集到给定的供应源创建的集合
        List<String> list = Arrays.asList("java", "python", "C++","php","java");
        //用LinkedList收集
        List<String> linkedListResult = list.stream().collect(Collectors.toCollection(LinkedList::new));
        System.out.println(linkedListResult);

        // 计算流中元素的个数使用示例：
        long howManyDishes = menu.stream().collect(Collectors.counting());
        System.out.println(howManyDishes);

        // 对流中项目的一个整数属性求和
        int totalCalories = menu.stream().collect(Collectors.summingInt(Dish::getCalories));
        System.out.println(totalCalories);

        // 计算流中项目Integer属性的平均值
        double avgCalories = menu.stream().collect(Collectors.averagingInt(Dish::getCalories));
        System.out.println(avgCalories);

        // 收集关于流中项目Integer属性的统计值，例如最大、最小、总和与平均值
        IntSummaryStatistics menuStatistics = menu.stream().collect(Collectors.summarizingInt(Dish::getCalories));
        System.out.println(menuStatistics);

        // 连接对流中每个项目调用toString方法所生成的字符串
        String shortMenu = menu.stream().map(Dish::getName).collect(Collectors.joining(", "));
        System.out.println(shortMenu);

        // 一个包裹了流中按照给定比较器选出的最大元素的Optional，或如果流为空则为Optional.empty()
        Optional<Dish> fattest = menu.stream().collect(Collectors.maxBy(Comparator.comparingInt(Dish::getCalories)));
        System.out.println(fattest);

        // 一个包裹了流中按照给定比较器选出的最小元素的Optional，或如果流为空则为Optional.empty()使用示例：
        Optional<Dish> lightest = menu.stream().collect(Collectors.minBy(Comparator.comparingInt(Dish::getCalories)));
        System.out.println(lightest);

        // 归约操作产生的类型
        // 从一个作为累加器的初始值开始，利用BinaryOperator与流中的元素逐个结合，从而将流归约为单个值
        int totalCalories1 = menu.stream().collect(Collectors.reducing(0, Dish::getCalories, Integer::sum));
        System.out.println(totalCalories1);

        // 转换函数返回的类型
        // 包裹另一个收集器，对其结果应用转换函数
        int howManyDishes1 = menu.stream().collect(Collectors.collectingAndThen(Collectors.toList(), List::size));
        System.out.println(howManyDishes1);

        // 根据项目的一个属性的值对流中的项目作问组，并将属性值作为结果`Map`的键
        Map<Dish.Type,List<Dish>> dishesByType = menu.stream().collect(Collectors.groupingBy(Dish::getType));
        System.out.println(dishesByType);

        // 根据对流中每个项目应用谓词的结果来对项目进行分区
        Map<Boolean,List<Dish>> vegetarianDishes2 = menu.stream().collect(Collectors.partitioningBy(Dish::isVegetarian));
        System.out.println(vegetarianDishes2);
    }

    // 质数又被称为素数，是指一个大于1的自然数，除了1和它自身外，不能被其它自然数整除，且其个数是无穷的，具有许多独特的性质，现如今多被用于密码学上。
    // 假设你要写一个方法，它接受参数int n，并将前 n 个自然数分为质数和非质数。
    // 但首先，找出能够测试某一个待测数字是否是质数的谓词会很有帮助：
    /*public boolean isPrime(int candidate) {
        return IntStream.range(2, candidate) // ←─产生一个自然数范围，从2开始，直至但不包括待测数
                .noneMatch(i -> candidate % i == 0); // ←─如果待测数字不能被流中任何数字整除则返回true
    }*/

    // 一个简单的优化是仅测试小于等于待测数平方根的因子
    public boolean isPrime(int candidate) {
        int candidateRoot = (int) Math.sqrt((double) candidate);
        return IntStream.rangeClosed(2, candidateRoot)
                .noneMatch(i -> candidate % i == 0);
    }

    public Map<Boolean, List<Integer>> partitionPrimes(int n) {
        return IntStream.rangeClosed(2, n).boxed().collect(Collectors.partitioningBy(candidate -> isPrime(candidate)));
    }
}
