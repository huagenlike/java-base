package com.mzl.concurrentProgramming.chap1;

/**
 * @program: java-base
 * @description: 守护线程
 * @author: may
 * @create: 2021-06-06 19:31
 * 守护线程是依赖于用户线程，用户线程退出了，守护线程也就会退出，典型的守护线程如垃圾回收线程。
 * 用户线程是独立存在的，不会因为其他用户线程退出而退出。
 * 只要任何非守护线程还在运行，守护线程就不会终止。
 **/
public class DaemonThreadTest {
    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            while (true) {
                try {
                    Thread currentthread1 = Thread.currentThread();
                    System.out.println("守护线程" + currentthread1.getName() + "心跳一次");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        //将该线程设置为守护线程
        thread1.setDaemon(true);
        thread1.start();

        Thread thread2 = new Thread(() -> {
            while (true) {
                try {
                    Thread currentthread2 = Thread.currentThread();
                    System.out.println("用户线程" + currentthread2.getName() + "心跳一次");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread2.start();
        try {
            Thread.sleep(10000);
            Thread currentthread = Thread.currentThread();
            System.out.println("主线程" + currentthread.getName() + "退出！");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
