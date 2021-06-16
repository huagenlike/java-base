package com.mzl.java8.chap3;

import java.util.function.Predicate;

/**
 * @author lihuagen
 * @version 1.0
 * @className: PredicateNegate
 * @description: 逻辑非
 * @date 2021/6/16 15:11
 * (t) -> !test(t),不用想的太复杂,就是咱们的逻辑非
 */
public class PredicateNegate {
    public static void main(String[] args) {
        PredicateNegate predicateNegate = new PredicateNegate();
        // 不是zhangsan的
        System.out.println(predicateNegate.testNegateMethod("zhangsan", value -> value.equals("zhangsan")));
        System.out.println(predicateNegate.testNegateMethod("lisi", value -> value.equals("zhangsan")));
    }

    public boolean testNegateMethod(String value, Predicate<String> predicate) {
        return predicate.negate().test(value);
    }
}
