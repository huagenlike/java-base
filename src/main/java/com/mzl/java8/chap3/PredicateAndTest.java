package com.mzl.java8.chap3;

import java.util.function.Predicate;

/**
 * @author lihuagen
 * @version 1.0
 * @className: PredicateAndTest
 * @description: 逻辑与&&,存在短路特性
 * @date 2021/6/16 14:47
 */
public class PredicateAndTest {

    public static void main(String[] args) {

        PredicateAndTest predicateAndTest = new PredicateAndTest();

        // 判断是名字长度大于10，并且是zhangxiaofeng的
        System.out.println(predicateAndTest.testAndMethod("zhangsan", value -> value.length() > 10, value -> value.equals("zhangxiaofeng")));
        System.out.println(predicateAndTest.testAndMethod("zhangxiaofeng", value -> value.length() > 10, value -> value.equals("zhangxiaofeng")));
        System.out.println(predicateAndTest.testAndMethod("ouyangjiayi", value -> value.length() > 10, value -> value.equals("zhangxiaofeng")));
    }

    /**
     * @param stringOne         待判断的字符串
     * @param predicateOne      断定表达式1
     * @param predicateTwo      断定表达式2
     * @return                    是否满足两个条件
     */
    public boolean testAndMethod(String stringOne, Predicate<String> predicateOne, Predicate<String> predicateTwo) {
        return predicateOne.and(predicateTwo).test(stringOne);
    }

    /**
     * @description: and方法返回的是Predicate，可以无限嵌套下去，本实例论证
     * @param stringOne 待判断的字符串
     * @param predicateOne 断定表达式1
     * @param predicateTwo 断定表达式2
     * @param predicateThree 断定表达式3
     * @return: boolean 是否满足三个条件
     * @author: lihuagen
     * @time: 2021/6/16 15:10
     */
    public boolean testAndMethod(String stringOne, Predicate<String> predicateOne, Predicate<String> predicateTwo, Predicate<String> predicateThree) {
        // and方法必须接在test方法前面
        // return predicateOne.and(predicateTwo).test(stringOne).and(predicateThree);
        return predicateOne.and(predicateTwo).and(predicateThree).test(stringOne);
    }
}
