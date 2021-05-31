package com.mzl.java8.chap5;

import com.mzl.java8.chap4.Dish;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import static com.mzl.java8.chap4.Dish.menu;

/**
 * @program: java-base
 * @description:
 * @author: may
 * @create: 2021-04-26 20:36
 **/
public class StreamTest {

    public static void main(String[] args) {
        // Java 中如何获取核心线程数？
        System.out.println(Runtime.getRuntime().availableProcessors());

        List<Integer> integers = Arrays.asList(1, 5, 8, 11, 14, 16, 21, 25, 29, 31);
        List<Integer> collect = integers.stream().filter(num -> num > 28).limit(1).collect(Collectors.toList());
        System.out.println(collect);
        List<Integer> collect1 = integers.stream().filter(num -> num > 28).skip(2).limit(1).collect(Collectors.toList());
        System.out.println(collect1);
        List<Integer> collect2 = integers.stream().filter(num -> num > 28).limit(1).skip(2).collect(Collectors.toList());
        System.out.println(collect2);

        List<Integer> disNameLengths = menu.stream().map(Dish::getName).map(String::length).collect(Collectors.toList());
        System.out.println(disNameLengths);
        List<String> collect3 = menu.stream().map(Dish::getName).collect(Collectors.toList());
        System.out.println(collect3);

        List<String> words = Arrays.asList("Java 8", "Lambdas", "In", "Action");
        List<String[]> collect5 = words.stream().map(world -> world.split("")).distinct().collect(Collectors.toList());
        System.out.println(collect5);

        words = Arrays.asList("Java 8", "Lambdas", "In", "In", "Action");
        collect5 = words.stream().map(world -> world.split("")).distinct().collect(Collectors.toList());
        System.out.println(collect5);

        List<Object> collect4 = words.stream().flatMap(world -> Arrays.stream(world.split(""))).distinct().collect(Collectors.toList());
        System.out.println(collect4);
    }
}
