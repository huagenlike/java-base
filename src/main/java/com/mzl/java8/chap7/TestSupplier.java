package com.mzl.java8.chap7;

import java.util.function.Supplier;

/**
 * @author lihuagen
 * @version 1.0
 * @className: TestSupplier
 * @description: TODO
 * @date 2021/5/31 16:16
 * 根据代码和官方注释，我的个人理解：
 *
 * 1.supplier是个接口，有一个get()方法
 * 2.语法 ：
 *  Supplier<TestSupplier> sup= TestSupplier::new;
 * 3.每次调用get()方法时都会调用构造方法创建一个新对象。
 */
public class TestSupplier {
    private int age;

    TestSupplier(){
        System.out.println(age);
    }
    public static void main(String[] args) {
        //创建Supplier容器，声明为TestSupplier类型，此时并不会调用对象的构造方法，即不会创建对象
        Supplier<TestSupplier> sup= TestSupplier::new;
        System.out.println("--------");
        //调用get()方法，此时会调用对象的构造方法，即获得到真正对象
        sup.get();
        //每次get都会调用构造方法，即获取的对象不同
        sup.get();
    }
}
