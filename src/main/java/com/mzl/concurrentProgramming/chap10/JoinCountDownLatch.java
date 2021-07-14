package com.mzl.concurrentProgramming.chap10;

import java.util.concurrent.CountDownLatch;

/**
 * @author lihuagen
 * @version 1.0
 * @className: JoinCountDownLatch
 * @description: 简单用法
 * @date 2021/7/14 9:37
 */
public class JoinCountDownLatch {
    private static CountDownLatch countDownLatch = new CountDownLatch(2);

    public static void main(String[] args) throws InterruptedException {
        Thread threadOne = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                countDownLatch.countDown();
            }
            System.out.println("child threadOne over!");
        });
        Thread threadTwo = new Thread(() -> {
            try {
                Thread.sleep(1001);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                countDownLatch.countDown();
            }
            System.out.println("child threadTwo over!");
        });
        threadOne.start();
        threadTwo.start();
        System.out.println("all child thread start!");
        countDownLatch.await();
        System.out.println("all child thread over!");
    }
}
