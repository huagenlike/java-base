package com.mzl.java8.chap3;

import java.util.function.Predicate;

/**
 * @author lihuagen
 * @version 1.0
 * @className: PredicateOrTest
 * @description: 逻辑或
 * @date 2021/6/16 15:15
 * 返回值一样需要注意，是Predicate,上面已经验证过了,可以进行嵌套
 */
public class PredicateOrTest {

    public static void main(String[] args) {
        PredicateOrTest predicateOrTest = new PredicateOrTest();
        // 查找是张三或者字符串长度大于15的
        System.out.println(predicateOrTest.testOrMethod("zhangsan", value -> value.length() > 15, value -> value.equals("zhangsan")));
        System.out.println(predicateOrTest.testOrMethod("wangwu", value -> value.length() > 15, value -> value.equals("zhangsan")));
        System.out.println(predicateOrTest.testOrMethod("this is a happy day", value -> value.length() > 15, value -> value.equals("zhangsan")));
    }

    /**
     *
     * @param stringOne         待判断的字符串
     * @param predicateOne      断定表达式1
     * @param predicateTwo      断定表达式2
     * @return                    是否满足两个条件
     */
    public boolean testOrMethod(String stringOne, Predicate<String> predicateOne, Predicate<String> predicateTwo) {
        return predicateOne.or(predicateTwo).test(stringOne);
    }
}
