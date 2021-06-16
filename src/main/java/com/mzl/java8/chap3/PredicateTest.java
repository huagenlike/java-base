package com.mzl.java8.chap3;

import java.util.function.Predicate;

/**
 * @author lihuagen
 * @version 1.0
 * @className: PredicateTest
 * @description: Java8之Predicate函数
 * @date 2021/6/16 14:34
 * 给定一个参数判断为true或者false
 */
public class PredicateTest {
    public static void main(String[] args) {
        PredicateTest predicateTest = new PredicateTest();
        Predicate<String> predicate = new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return s.equals("may");
            }
        };

        System.out.println(predicate.test("lisi"));
        System.out.println("------------------------");
        System.out.println(predicate.test("may"));

        // 传统方法
        System.out.println(predicateTest.judgeStringLength("hello"));
        System.out.println(predicateTest.judgenumbersOdds(4));
        System.out.println(predicateTest.judgeSpecialNumbers(-1));

        System.out.println("------------------------");
        // lambda方法
        /** - 1.判断传入的字符串的长度是否大于5 */
        System.out.println(predicateTest.judgeConditionByFunction(12345, value -> String.valueOf(value).length() > 5));
        /** - 2.判断传入的参数是否是奇数 */
        System.out.println(predicateTest.judgeConditionByFunction(4, value -> value % 2 == 0));
        /** - 3.判断数字是否大于10 */
        System.out.println(predicateTest.judgeConditionByFunction(10, value -> value > 10));
    }

    /**
     * - 1.判断传入的字符串的长度是否大于5
     * @param judgeString 待判断字符串
     * @return
     */
    public boolean judgeStringLength(String judgeString) {
        return judgeString.length() > 5 ? true:false;
    }

    /**
     *  - 2.判断传入的参数是否是奇数
     * @param number        待判断的数字
     * @return               1 代表偶数， 0代表奇数
     */
    public int judgenumbersOdds(int number) {
        return number % 2 == 0 ? 1 : 0;
    }

    /**
     * - 3.判断数字是否大于10
     * @param number        待判断的数字
     * @return               1. 代表大于10 ， 0 代表小于10
     */
    public int judgeSpecialNumbers(int number) {
        return number > 10 ? 1 : 0;
    }

    /**
     * @Author lhg
     * @Description 按功能判断条件
     * @Date 14:39 2021/6/16
     * @Param [value, predicate]
     * @return boolean
     **/
    public boolean judgeConditionByFunction(int value,Predicate<Integer> predicate) {
        return predicate.test(value);
    }
}
