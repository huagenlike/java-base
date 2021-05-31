package com.mzl.java8.chap5;

import com.mzl.java8.chap4.*;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.mzl.java8.chap4.Dish.menu;

/**
 * 数字流
 */
public class NumericStreams {

    public static void main(String... args) {

        List<Integer> numbers = Arrays.asList(3, 4, 5, 1, 2);

        Arrays.stream(numbers.toArray()).forEach(System.out::println);
        // 求菜单总的卡路里
        int calories = menu.stream()
                .mapToInt(Dish::getCalories)
                .sum();
        System.out.println("Number of calories:" + calories);


        // max and OptionalInt
        OptionalInt maxCalories = menu.stream()
                .mapToInt(Dish::getCalories)
                .max();

        int max;
        if (maxCalories.isPresent()) {
            max = maxCalories.getAsInt();
        } else {
            // we can choose a default value
            max = 1;
        }
        // 可以直接这样定义
        // int max = maxCalories.orElse(1);
        System.out.println(max);

        // numeric ranges（范围是1-100）
        IntStream evenNumbers = IntStream.rangeClosed(1, 100)
                .filter(n -> n % 2 == 0);

        System.out.println(evenNumbers.count());
        System.out.println("-----------------------------------");
        // IntStream：是int类型的流。stream<Integer 是Integer类型的流。
        // IntStream是存的是int类型的stream,而Steam是一个存了Integer的stream。boxed的作用就是将int类型的stream转成了Integer类型的Stream。
        Stream<int[]> pythagoreanTriples =
                IntStream.rangeClosed(1, 100).boxed()
                        .flatMap(a -> IntStream.rangeClosed(a, 100)
                                // Math.sqrt() 函数返回一个数的平方根
                                .filter(b -> Math.sqrt(a * a + b * b) % 1 == 0).boxed()
                                .map(b -> new int[]{a, b, (int) Math.sqrt(a * a + b * b)}));

        pythagoreanTriples.forEach(t -> System.out.println(t[0] + ", " + t[1] + ", " + t[2]));

        List<Integer> numbers1 = Arrays.asList(1, 2, 3, 3, 4, 5);
        IntStream intStream = numbers1.stream().mapToInt(i -> i);  //转成IntStream
        Stream<Integer> boxed = intStream.boxed();                //转成Stream<Integer>
    }

    public static boolean isPerfectSquare(int n) {
        return Math.sqrt(n) % 1 == 0;
    }

}
