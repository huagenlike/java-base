package com.mzl.java8.chap5;

import com.mzl.java8.chap4.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static com.mzl.java8.chap4.Dish.menu;

/**
 * map的用法
 * map：返回一个流，包含给定函数应用在流中每一个元素后的结果
 * flatmap：返回一个流，包含将此流中的每个元素替换为通过给定函数映射应用于每个元素而生成的映射流的内容
 */
public class Mapping{

    public static void main(String...args){

        /**
         * 获取对象字段的集合，返回List<T>
         */
        List<String> dishNames = menu.stream()
                                     .map(Dish::getName)
                                     .collect(toList());
        System.out.println(dishNames);

        // map
        List<String> words = Arrays.asList("Hello", "World");
        List<Integer> wordLengths = words.stream()
                                         .map(String::length)
                                         .collect(toList());
        System.out.println(wordLengths);

        // flatmap返回的是包含结果集的Observable
        // flatMap扁平化
        words.stream()
                 .flatMap((String line) -> Arrays.stream(line.split("")))
                 .distinct()
                 .forEach(System.out::println);

        // Stream<Stream<String>> streamStream = words.stream().map((String line) -> Arrays.stream(line.split("")));
        // Stream<String> stringStream = words.stream().flatMap((String line) -> Arrays.stream(line.split("")));

        // flatMap
        List<Integer> numbers1 = Arrays.asList(1,2,3,4,5);
        List<Integer> numbers2 = Arrays.asList(6,7,8);
        List<int[]> pairs =
                        numbers1.stream()
                                .flatMap((Integer i) -> numbers2.stream()
                                                       .map((Integer j) -> new int[]{i, j})
                                 )
                                .filter(pair -> (pair[0] + pair[1]) % 3 == 0)
                                .collect(toList());
        pairs.forEach(pair -> System.out.println("(" + pair[0] + ", " + pair[1] + ")"));

        System.out.println("-----------------------------------------");

        // map 和 flatMap的区别实例验证
        mapDemo();
        flatMapDemo();

    }

    // 自增生成组编号
    static int group = 1;
    // 自增生成学生编号
    static int student = 1;
    static List<String[]> eggs = new ArrayList<>();
    static {
        // 第一箱鸡蛋
        eggs.add(new String[]{"鸡蛋_1", "鸡蛋_1", "鸡蛋_1", "鸡蛋_1", "鸡蛋_1"});
        // 第二箱鸡蛋
        eggs.add(new String[]{"鸡蛋_2", "鸡蛋_2", "鸡蛋_2", "鸡蛋_2", "鸡蛋_2"});
    }

    // 有二箱鸡蛋，每箱5个，现在要把鸡蛋加工成煎蛋，然后分给学生。
    /**
     * map做的事情：把二箱鸡蛋分别加工成煎蛋，还是放成原来的两箱，分给2组学生；
     */
    public static void mapDemo() {
        eggs.stream()
                .map(x -> Arrays.stream(x).map(y -> y.replace("鸡", "煎")))
                .forEach(x -> System.out.println("组" + group++ + ":" + Arrays.toString(x.toArray())));
    }

    /**
     * 扁平化处理，flatMap做的事情：把二箱鸡蛋分别加工成煎蛋，然后放到一起【10个煎蛋】，分给10个学生；
     */
    public static void flatMapDemo() {
        eggs.stream()
                .flatMap(x -> Arrays.stream(x).map(y -> y.replace("鸡", "煎")))
                .forEach(x -> System.out.println("学生" + student++ + ":" + x));
    }
}
