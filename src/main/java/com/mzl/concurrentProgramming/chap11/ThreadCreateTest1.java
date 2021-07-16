package com.mzl.concurrentProgramming.chap11;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ThreadTest1
 * @description: 线程创建正确示范
 * @date 2021/7/16 11:46
 * 从运行结果就可以定位到是保存订单模块抛出了NPE异常，一下子就可以找到问题所在。
 */
public class ThreadCreateTest1 {
    static final String THREAD_SAVE_ORDER = "THREAD_SAVE_ORDER";
    static final String THREAD_SAVE_ADD= "THREAD_SAVE_ADD";

    public static void main(String[] args) {
        Thread threadOne = new Thread(() -> {
            System.out.println("保存订单的线程");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            throw new NullPointerException();
        }, THREAD_SAVE_ORDER);

        Thread threadTwo = new Thread(() -> {
            System.out.println("保存收货地址的线程");
        }, THREAD_SAVE_ADD);

        threadOne.start();
        threadTwo.start();
    }
}
