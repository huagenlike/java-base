package com.mzl.java8.chap6;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;

/**
 * @program: java-base
 * @description:
 * @author: may
 * @create: 2021-05-22 20:11
 **/
public class CustomerCollectorAction {
    public static void main(String[] args) {
        Collector<String, List<String>, List<String>> collector = new ToListCollector<>();

        String[] arrs = {"aa", "hello", "world", "lisa", "Lambda", "Collector"};

        //List<String> result = Arrays.stream(arrs).filter(s -> s.length() >= 5).collect(collector);
        List<String> result = Arrays.asList("aa", "hello", "world", "lisa", "Lambda", "Collector").parallelStream().filter(s -> s.length() >= 5).collect(collector);

    }
}
