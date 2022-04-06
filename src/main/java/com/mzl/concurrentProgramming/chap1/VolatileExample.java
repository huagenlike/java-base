package com.mzl.concurrentProgramming.chap1;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: java-base
 * @description:
 * @author: may
 * @create: 2021-08-09 21:01
 * Happens-Before ，它真正要表达的是：前面一个操作的结果对后续操作是可见的。
 * Happens-Before 规则:
 * 1. 程序的顺序性规则
 * 这条规则是指在一个线程中，按照程序顺序，前面的操作 Happens-Before 于后续的任意操作。
 * 2. volatile 变量规则
 * 这条规则是指对一个 volatile 变量的写操作， Happens-Before 于后续对这个 volatile 变量的读操作
 * 3. 传递性
 * 这条规则是指如果 A Happens-Before B，且 B Happens-Before C，那么 A Happens-Before C。
 * 4. 管程中锁的规则
 * 一个锁的解锁 Happens-Before 于后续对这个锁的加锁
 * 5. 线程 start() 规则
 *  这条是关于线程启动的。它是指主线程 A 启动子线程 B 后，子线程 B 能够看到主线程在启动子线程 B 前的操作。
 * 6. 线程 join() 规则
 *  它是指主线程 A 等待子线程 B 完成（主线程 A 通过调用子线程 B 的 join() 方法实现），当子线程 B 完成后（主线程 A 中 join() 方法返回），主线程能够看到子线程的操作。当然所谓的“看到”，指的是对共享变量的操作。
 **/
public class VolatileExample {
    int x = 0;
    volatile boolean v = false;

    public void writer() {
        x = 42;
        v = true;
    }

    public void reader() {
        if (v == true) {
            // 这里x会是多少呢？
        }
    }

    public static void main(String[] args) {
        VolatileExample volatileExample = new VolatileExample();
//        volatileExample.demo();
        volatileExample.demo1();
    }

    // “x=42” Happens-Before 写变量 “v=true” ，这是规则 1 的内容；
    // 写变量“v=true” Happens-Before 读变量 “v=true”，这是规则 2 的内容 。
    // 如果线程 B 读到了“v=true”，那么线程 A 设置的“x=42”对线程 B 是可见的。也就是说，线程 B 能看到 “x == 42” ，有没有一种恍然大悟的感觉？
    // 这就是 1.5 版本对 volatile 语义的增强，这个增强意义重大，1.5 版本的并发工具包（java.util.concurrent）就是靠 volatile 语义来搞定可见性的，这个在后面的内容中会详细介绍。
    public void demo() {
        VolatileExample volatileExample = new VolatileExample();
        Thread threadA = new Thread(() -> {
            synchronized (this) { //此处自动加锁
                // x是共享变量,初始值=10
                if (this.x < 12) {
                    this.x = 12;
                } //此处自动解锁
            }
            volatileExample.writer();
            System.out.println("threadA:" + volatileExample.x);
        });
        Thread threadB = new Thread(() -> {
                System.out.println(this.x);
            volatileExample.reader();
            System.out.println("threadB:" + volatileExample.x);
        });

        // 假设 x 的初始值是 0，线程 A 执行完代码块后 x 的值会变成 12（执行完自动释放锁），线程 B 进入代码块时，能够看到线程 A 对 x 的写操作，也就是线程 B 能够看到 x==12
        threadA.start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threadB.start();
    }

    // Lambda表达式规则
    // 只能引用标记了 final 的外层局部变量，这就是说不能在 lambda 内部修改定义在域外的局部变量，否则会编译错误。
    // 局部变量可以不用声明为 final，但是必须不可被后面的代码修改（即隐性的具有 final 的语义）
    // 不允许声明一个与局部变量同名的参数或者局部变量。
    public void demo1() {
        final Integer[] var = {0};
        Thread threadB = new Thread(() -> {
            System.out.println("进入线程B");
            // 主线程调用B.start()之前
            // 所有对共享变量的修改，此处皆可见
            // 此例中，var==77
            System.out.println("var：" + var[0]);
        });

        Thread threadA = new Thread(() -> {
            System.out.println("进入线程A");
            // 此处对共享变量var修改
            var[0] = 77;
            // 主线程启动子线程
            threadB.start();
        });

        threadA.start();
    }
}
