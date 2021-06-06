package com.mzl.concurrentProgramming.chap1;

/**
 * @program: java-base
 * @description: 非守护线程
 * @author: may
 * @create: 2021-06-06 19:39
 **/
public class UnDaemonThreadTest {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            for (;;) {

            }
        });
        // 设置为守护线程
        // thread.setDaemon(true);
        // 启动子线程
        thread.start();

        System.out.println("main thread is over");
    }
}
