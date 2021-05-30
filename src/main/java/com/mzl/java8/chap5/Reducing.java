package com.mzl.java8.chap5;

import com.mzl.java8.chap4.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.mzl.java8.chap4.Dish.menu;

/**
 * Stream.reduce()合并流的元素并产生单个值。
 */
public class Reducing{

    public static void main(String...args){

        List<Integer> numbers = Arrays.asList(3,4,5,1,2);
        // 获取总和
        int sum = numbers.stream().reduce(0, (a, b) -> a + b);
        System.out.println(sum);

        // 获取总和
        int sum2 = numbers.stream().reduce(0, Integer::sum);
        System.out.println(sum2);

        // 获取最大数
        int max = numbers.stream().reduce(0, (a, b) -> Integer.max(a, b));
        System.out.println(max);

        // 获取最小数
        Optional<Integer> min = numbers.stream().reduce(Integer::min);
        min.ifPresent(System.out::println);

        // 获取卡路里的总和
        int calories = menu.stream()
                           .map(Dish::getCalories)
                           .reduce(0, Integer::sum);
        System.out.println("Number of calories:" + calories);
    }
}
