package com.mzl.concurrentProgramming.chap11;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ThreasTest
 * @description: 线程创建错误示范
 * @date 2021/7/16 11:43
 * 线程报错的话，不能直观体现具体哪个线程
 */
public class ThreadCreateTest {
    public static void main(String[] args) {
        Thread threadOne = new Thread(() -> {
            System.out.println("保存订单的线程");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            throw new NullPointerException();
        });

        Thread threadTwo = new Thread(() -> {
            System.out.println("保存收货地址的线程");
        });

        threadOne.start();
        threadTwo.start();
    }
}
