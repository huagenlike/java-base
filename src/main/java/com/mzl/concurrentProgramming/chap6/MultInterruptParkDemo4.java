package com.mzl.concurrentProgramming.chap6;

import java.util.concurrent.locks.LockSupport;

/**
 * @author lihuagen
 * @version 1.0
 * @className: MultInterruptParkDemo4
 * @description: sleep清除中断标志位但不会更改_counter
 * @date 2021/7/6 17:57
 * Thread中断方法分析（包括HotSpot源码）中interrupt源码：
 *  会调用Parker::unpark()，那么_counter会设置为1.
 * 参考阻塞线程的相关方法分析（包括HotSpot层源码）中关于sleep源码：
 *  如果线程已中断，则清除中断标记并抛出中断异常，直接返回，但是并没有更改_counter。
 *
 * interrupt会调用((JavaThread*)thread)->parker()->unpark()，将_counter设置为1，也即后面调用park不会阻塞
 * sleep如果检测到中断会直接清除中断标志，并抛出异常。
 * 因此两个Thread.interrupted()都返回false，且LockSupport.park()不会阻塞。
 */
public class MultInterruptParkDemo4 {
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
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("本打印出现在第一个sleep()之后");
            System.out.println(Thread.interrupted());
            System.out.println(Thread.interrupted());
            LockSupport.park();
            System.out.println("本打印出现在第二个park()之后");
        }
    }
}
