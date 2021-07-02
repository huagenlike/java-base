package com.mzl.concurrentProgramming.chap2;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ReflexUnSafeTest
 * @description: 用反射获取 Unsafe，进行使用
 * @date 2021/7/2 11:25
 */
public class ReflexUnSafeTest {

    // 获取Unsafe的实例
    static final Unsafe unsafe;

    // 记录变量state在类ReflexUnSafeTest中的偏移值
    static final long stateOffset;

    // 变量
    private volatile long state = 0;

    static {
        try {
            // 使用反射获取Unsafe的成员变量 theUnsafe
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            // 设置为可存取
            field.setAccessible(true);
            // 获取该变量的值
            unsafe = (Unsafe) field.get(null);
            // 获取state在ReflexUnSafeTest中的偏移量
            stateOffset = unsafe.objectFieldOffset(ReflexUnSafeTest.class.getDeclaredField("state"));
        }catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            throw new Error(e);
        }
    }

    public static void main(String[] args) {
        ReflexUnSafeTest test = new ReflexUnSafeTest();
        boolean sucess = unsafe.compareAndSwapInt(test, stateOffset, 0, 1);
        System.out.println(sucess);
    }
}
