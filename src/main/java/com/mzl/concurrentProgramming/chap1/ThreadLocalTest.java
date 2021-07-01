package com.mzl.concurrentProgramming.chap1;

/**
 * @program: java-base
 * @description:
 * @author: may
 * @create: 2021-06-06 19:51
 * ThreadLocal是JDK包提供的，它提供线程本地变量，如果创建一乐ThreadLocal变量，那么访问这个变量的每个线程都会有这个变量的一个副本，在实际多线程操作的时候，操作的是自己本地内存中的变量，从而规避了线程安全问题
 * Thread类中有两个变量threadLocals和inheritableThreadLocals，二者都是ThreadLocal内部类ThreadLocalMap类型的变量，我们通过查看内部内ThreadLocalMap可以发现实际上它类似于一个HashMap。
 * 在默认情况下，每个线程中的这两个变量都为null，只有当线程第一次调用ThreadLocal的set或者get方法的时候才会创建他们（后面我们会查看这两个方法的源码）。
 **/
public class ThreadLocalTest {
    // （1）print函数
    static void print(String str) {
        // 1.1 打印当前线程本地内存中localVariable
        System.out.println(str + ":" + localVariable.get());
        // 1.2 清除当前线程本地内存中的localVariable变量
         localVariable.remove();
    }

    // （2）创建ThreadLocal变量
    static ThreadLocal<String> localVariable = new ThreadLocal<>();

    public static void main(String[] args) {
        // （3）创建线程one
        Thread threadOne = new Thread(() -> {
            // 3.1 设置线程One中本地变量localVariable的值
            localVariable.set("threadOne local variable");
            // 3.2 调用打印函数
            print("threadOne");
            // 3.3 打印本地变量值
            System.out.println("threadOne remove after" + ":" + localVariable.get());
        });

        // (4) 创建线程two
        Thread threadTwo = new Thread(() -> {
            // 4.1 设置线程One中本地变量localVariable的值
            localVariable.set("threadTwo local variable");
            // 4.2 调用打印函数
            print("threadTwo");
            // 4.3 打印本地变量值
            System.out.println("threadTwo remove after" + ":" + localVariable.get());
        });

        // （5）启动线程
        threadOne.start();
        threadTwo.start();

    }
}
