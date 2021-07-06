package com.mzl.concurrentProgramming.chap6;

import java.util.concurrent.locks.LockSupport;

/**
 * @author lihuagen
 * @version 1.0
 * @className: MultInterruptParkDemo
 * @description: 中断后LockSupport.park()直接返回
 * @date 2021/7/6 17:58
 * Thread中断方法分析（包括HotSpot源码）中interrupt源码：
 *  如果线程未设置中断标志位，会进行设置。
 * LockSupport及HotSpot层Parker::park/unpark分析中park源码：
 *  只要线程设置了中断标志位，就直接返回，且这里的Thread::is_interrupted(thread, false)因为是false，所以不会清除中断标志位。
 *
 * t4.interrupt()会设置线程的中断标志位
 * LockSupport.park()会检查线程是否设置了中断标志位，如果设置了，则返回（这里并不会清除中断标志位）
 *
 * 链接地址:https://my.oschina.net/u/4261744/blog/3315922
 */
public class MultInterruptParkDemo {
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
            LockSupport.park();
            System.out.println("本打印出现在第二个park()之后");
        }
    }
}
