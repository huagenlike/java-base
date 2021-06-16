package com.mzl.java8.chap3;

import java.util.function.BiFunction;

/**
 * @author lihuagen
 * @version 1.0
 * @className: BiFunctionTest
 * @description: TODO
 * @date 2021/6/16 16:22
 * 是Function函数的拓展吧,function函数appy()方法接受一个参数,返回一个参数,而Bifunction是接收两个参数,返回一个参数
 * BiFunction<T, U, R>
 *     R apply(T t, U u);
 */
public class BiFunctionTest {
    public static void main(String[] args) {

        System.out.println(TestBiFunction(3,4,(i,j) -> i + j));
        System.out.println(TestBiFunction(3,4,(i,j) -> i - j));
        System.out.println(TestBiFunction(3,4,(i,j) -> i * j));
        System.out.println(TestBiFunction(3,4,(i,j) -> i / j));

    }
    /**
     *
     * @param i 待操作的数字1
     * @param j 待操作的数字2
     * @param biFunction 即将调用的函数式接口
     * @return 传入的函数
     */
    public static int TestBiFunction(int i, int j, BiFunction<Integer,Integer,Integer> biFunction) {

        return biFunction.apply(i,j);
    }
}
