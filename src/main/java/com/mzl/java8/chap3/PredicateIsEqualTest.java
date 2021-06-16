package com.mzl.java8.chap3;

import java.util.function.Predicate;

/**
 * @author lihuagen
 * @version 1.0
 * @className: PredicateIsEqualTest
 * @description: 判断两个对象是否相等
 * @date 2021/6/16 15:20
 * 使用的是Objects里面的equals()方法进行比较
 * 先判断对象是否为NULL—> 这个由Objects里面的isNull进行判断,
 * 如果，不为Null的话,那么接下来用java.lang.object里面的equals()方法进行比较,下面我们去演练这个方法
 */
public class PredicateIsEqualTest {

    public static void main(String[] args) {
        PredicateIsEqualTest predicate = new PredicateIsEqualTest();
        String strNull = null;
        String strNull1 = null;
        System.out.println(predicate.testIsEqualMethod("zhangsan","zhangsan"));
        System.out.println("~~~   ~~~   ~~~   ~~~");
        System.out.println(predicate.testIsEqualMethod("zhangsan","lisi"));
        System.out.println("~~~   ~~~   ~~~   ~~~");
        System.out.println(predicate.testIsEqualMethod(strNull,"zhangsan")); /* 我们来Debug一下这个程序*/
        System.out.println("~~~   ~~~   ~~~   ~~~");
        System.out.println(predicate.testIsEqualMethod(strNull,strNull1)); /* 我们来Debug一下这个程序*/
    }

    public boolean testIsEqualMethod(String value, String value1) {
        return Predicate.isEqual(value).test(value1);
    }


}
