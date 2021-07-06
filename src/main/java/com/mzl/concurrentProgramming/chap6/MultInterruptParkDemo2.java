package com.mzl.concurrentProgramming.chap6;

import java.util.concurrent.locks.LockSupport;

/**
 * @author lihuagen
 * @version 1.0
 * @className: MultInterruptParkDemo2
 * @description: unpark不会累积许可（最多为1）
 * @date 2021/7/6 18:01
 * LockSupport及HotSpot层Parker::park/unpark分析中park源码：
 *  每次调用park都会将_counter直接置为0。
 *  每次调用unpark都会将_counter直接置为1.
 *
 * 因此，总的许可数总是保持在1，无论调用多少次unpark，都只会将_counter置为1。
 * 每次park都会将_counter置为0，如果之前为1，则直接返回。后面的park调用就会阻塞。
 */
public class MultInterruptParkDemo2 {
    public static volatile boolean flag = true;
    public static void main(String[] args) {
        ThreadDemo04 t4 = new ThreadDemo04();
        t4.start();
        LockSupport.unpark(t4);
        LockSupport.unpark(t4);
        LockSupport.unpark(t4);
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
