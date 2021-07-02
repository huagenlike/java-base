package com.mzl.concurrentProgramming.chap2;

/**
 * @author lihuagen
 * @version 1.0
 * @className: Hello
 * @description: 可重入锁
 * @date 2021/7/2 17:32
 */
public class Hello {
    public synchronized void helloA() {
        System.out.println("hello");
    }

    public synchronized void helloB() {
        System.out.println("hello B");
        helloA();
    }

    public static void main(String[] args) {
        Hello hello = new Hello();
        Thread threadA = new Thread(() -> {
            hello.helloA();
        });

        Thread threadB = new Thread(() -> {
            hello.helloB();
        });

        threadA.start();
        threadB.start();
    }
}
