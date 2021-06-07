package com.mzl.concurrentProgramming.chap1;

/**
 * @program: java-base
 * @description: ThreadLocal 不支持继承性
 * @author: may
 * @create: 2021-06-07 22:30
 **/
public class ThreadLocalTest1 {
    // 1.创建线程变量
    public static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void main(String[] args) {
        // 2.设置线程变量
        threadLocal.set("hello world");
        // 3.启动子线程
        Thread thread = new Thread(() -> {
            // 4.子线程输出线程变量的值
            System.out.println("thread:" + threadLocal.get());
        });

        thread.start();
        //5.主线程输出线程变量的值
        System.out.println("main:" + threadLocal.get());
    }
}
