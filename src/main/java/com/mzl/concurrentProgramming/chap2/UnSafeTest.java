package com.mzl.concurrentProgramming.chap2;

import sun.misc.Unsafe;

/**
 * @author lihuagen
 * @version 1.0
 * @className: UnSafeTest
 * @description: TODO
 * @date 2021/7/2 11:13
 * 因为Unsafe可以直接操作内存，如果能让程序随意调用的话，那就不安全了
 */
public class UnSafeTest {
    // 获取Unsafe的实例（2.2.1）
    // 只有Bootstrap类加载器加载的才可以使用，本demo使用的是AppClassLoader加载器，所以会抛出异常
    static final Unsafe unsafe = Unsafe.getUnsafe();

    // 记录变量state在类UnSafeTest中的偏移值（2.2.2）
    static final long stateOffset;

    // 变量
    private volatile long state = 0;

    static {
        try {
             // 获取state变量在类UnSafeTest中的偏移值（2.2.4）
            stateOffset = unsafe.objectFieldOffset(UnSafeTest.class.getDeclaredField("state"));
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            throw new Error(e);
        }
    }

    public static void main(String[] args) {
        // 创建实例，并设置state值为1（2.2.5）
        UnSafeTest test = new UnSafeTest();
        // (2.2.6)
        boolean sucess = unsafe.compareAndSwapInt(test, stateOffset, 0, 1);
        System.out.println(sucess);
    }
}
