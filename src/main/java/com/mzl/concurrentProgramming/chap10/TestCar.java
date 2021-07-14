package com.mzl.concurrentProgramming.chap10;

import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * @author lihuagen
 * @version 1.0
 * @className: TestCar
 * @description: Semaphore 实现停车场提示牌功能
 * @date 2021/7/14 16:37
 * 业务场景 ：
 * 1、停车场容纳总停车量10。
 * 2、当一辆车进入停车场后，显示牌的剩余车位数响应的减1.
 * 3、每有一辆车驶出停车场后，显示牌的剩余车位数响应的加1。
 * 4、停车场剩余车位不足时，车辆只能在外面等待。
 */
public class TestCar {
    //停车场同时容纳的车辆10
    private static Semaphore semaphore = new Semaphore(10);

    public static void main(String[] args) {
        //模拟100辆车进入停车场
        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("====" + Thread.currentThread().getName() + "来到停车场");
                        // 返回可用的令牌数量。
                        if (semaphore.availablePermits() == 0) {
                            System.out.println("车位不足，请耐心等待");
                        }
                        // 获取一个令牌，在获取到令牌、或者被其他线程调用中断之前线程一直处于阻塞状态。
                        semaphore.acquire();//获取令牌尝试进入停车场
                        System.out.println(Thread.currentThread().getName() + "成功进入停车场");
                        Thread.sleep(new Random().nextInt(10000));//模拟车辆在停车场停留的时间
                        System.out.println(Thread.currentThread().getName() + "驶出停车场");
                        // 释放一个令牌，唤醒一个获取令牌不成功的阻塞线程。
                        semaphore.release();//释放令牌，腾出停车场车位
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, i + "号车");
            thread.start();
        }
    }
}
