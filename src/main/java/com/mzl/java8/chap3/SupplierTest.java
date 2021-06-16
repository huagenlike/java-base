package com.mzl.java8.chap3;

import java.util.function.Supplier;

/**
 * @author lihuagen
 * @version 1.0
 * @className: SupplierTest
 * @description: 供应商测试
 * @date 2021/6/16 16:03
 */
public class SupplierTest implements StudentEntity{
    public static void main(String[] args) {
        /**
         * 所遇到的问题:
         *      1. 不会生成对象了 ---> 搞出来两种实现方法
         *          a. Supplier<StudentEntity> supplier = StudentEntity::new; //第一种实现方法
         *          b. Supplier<StudentEntity> supplier = () -> new StudentEntity(); // 第二种实现方法
         *
         *          本质上属于一种
         */
        //第一种实现方法
        Supplier<StudentEntity> supplier = SupplierTest::new;
        //第一种实现方法
        Supplier<StudentEntity> supplier1 = SupplierTest::new;
        // 判断，我们上面所说的不存在的对象或者新生成的对象
        if (supplier == supplier1) { // 直接比较地址就好
            System.out.println("1");
        } else{
            System.out.println("0");
        }
        // 结果确实是不存在的，新的。

    }
}
