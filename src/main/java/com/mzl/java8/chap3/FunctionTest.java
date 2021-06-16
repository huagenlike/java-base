package com.mzl.java8.chap3;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author lihuagen
 * @version 1.0
 * @className: FunctionTest
 * @description: 将给定的参数应用到这个函数上,传入的参数类型为T返回类型为R
 * @date 2021/6/16 15:36
 * Function：将给定的参数应用到这个函数上,传入的参数类型为T返回类型为R
 * compose：接收一个function类型的函数作为参数
 * andThen：就是现将传过来的参数执行apply(T t)方法,之后把apply(T t)里面返回的结果再去执行第二个Function函数
 * identity：输入是什么。输出就是什么
 */
public class FunctionTest {

    public static void main(String[] args) {
        System.out.println(testFunction(2, i -> i * 2 + 1));
        System.out.println(testFunctionDouble(2, i -> Double.valueOf(i * 2)));
        // compose的测试demo
        System.out.println(testFunctionCompose(2,i -> i * 2 + 1,j -> j * j));
        // andThen的测试demo
        System.out.println(testFunctionAndThen(2,i -> i * 2 + 1,j -> j * j));

        Function<String,String> function = Function.identity();
        String strValue = testIdentity(function);
        System.out.println(strValue);

        testIdentityByStream();
    }

    public static int testFunction(int i, Function<Integer, Integer> function) {
        return function.apply(i);
    }

    public static Double testFunctionDouble(int i, Function<Integer, Double> function) {
        return function.apply(i);
    }

    /**
     * @description: compose的测试demo
     * @param i
     * @param function1
     * @param function2
     * @return: int
     * @author: lihuagen
     * @time: 2021/6/16 15:43
     * 将 function2 计算出来，然后将结果放到 function1 中计算
     */
    public static int testFunctionCompose(int i, Function<Integer,Integer> function1,Function<Integer,Integer> function2) {
        return function1.compose(function2).apply(i);
    }

    /**
     * @description: andThen的测试demo
     * @param i
     * @param function1
     * @param function2
     * @return: int
     * @author: lihuagen
     * @time: 2021/6/16 15:45
     * 先计算 function1，返回的结果再去执行 function2 中执行
     *
     */
    public static int testFunctionAndThen(int i, Function<Integer,Integer> function1,Function<Integer,Integer> function2) {
        return function1.andThen(function2).apply(i);
    }

    /**
     * @description: identity的测试demo
     * @param function
     * @return: java.lang.String
     * @author: lihuagen
     * @time: 2021/6/16 16:02
     */
    public static String testIdentity(Function<String,String> function) {
        return function.apply("hello world");
    }

    /**
     * @description: identity的测试demo
     * @param
     * @return: void
     * @author: lihuagen
     * @time: 2021/6/16 16:02
     */
    public static void testIdentityByStream() {
        List<Person> personList = new ArrayList<>();
        personList.add(new Person("hepengju", 28, 20000.0));
        personList.add(new Person("lisi"    , 44, 40000.0));
        personList.add(new Person("wangwu"  , 55, 50000.0));
        personList.add(new Person("zhaoliu" , 66, 60000.0));
        personList.add(new Person("zhangsan", 33, 33333.0));
        personList.add(new Person("wgr", 23, 10000.0));
        Map<String, Person> collect = personList.stream().collect(Collectors.toMap(Person::getName, Function.identity()));
        collect.forEach((name,p) ->{
            System.out.println(name + ":"+p);
        });
    }
}

@Data
class Person{
    private String name;
    private Integer age;
    private Double worth;

    public Person() {
    }

    public Person(String name, Integer age, Double worth) {
        this.name = name;
        this.age = age;
        this.worth = worth;
    }
}
