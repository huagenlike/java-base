package com.mzl.concurrentProgramming.chap6;

import java.util.concurrent.locks.LockSupport;

/**
 * @author lihuagen
 * @version 1.0
 * @className: MultInterruptParkDemo3
 * @description: Thread.interrupted()会清除中断标志
 * @date 2021/7/6 18:02
 * Thread中断方法分析（包括HotSpot源码）中Thread.interrupted()源码：
 * 可以看到如果中断了并且clear_interrupted为true，则会清除中断标志位。
 *
 * Thread.interrupted()清除中断标志位后，第二个LockSupport.park()就不会直接返回，而是正常地阻塞。
 */
public class MultInterruptParkDemo3 {
    public static volatile boolean flag = true;
    public static void main(String[] args) {
        ThreadDemo04 t4 = new ThreadDemo04();
        t4.start();
        t4.interrupt();
        flag = false;
    }
    public static class ThreadDemo04 extends Thread {
        @Override
        public void run() {
            while (flag) {
            }
            LockSupport.park();
            System.out.println("本打印出现在第一个park()之后");
            System.out.println(Thread.interrupted());
            System.out.println(Thread.interrupted());
            LockSupport.park();
            System.out.println("本打印出现在第二个park()之后");
        }
    }
}
