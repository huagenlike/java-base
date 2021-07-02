package com.mzl.concurrentProgramming.chap2;

/**
 * @author lihuagen
 * @version 1.0
 * @className: CountTest
 * @description: 计数器，共享资源的安全性问题
 * @date 2021/7/2 9:17
 */
public class CountTest {

    static int count = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread threadA = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                count++;
            }
        });

        Thread threadB = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                count++;
            }
        });

        Thread threadC = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                count++;
            }
        });

        Thread threadD = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                count++;
            }
        });

        threadA.start();
        threadB.start();
        threadC.start();
        threadD.start();

        threadA.join();
        threadB.join();
        threadC.join();
        threadD.join();

        System.out.println(count);

    }

    /*public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 20; i++) {
            new Thread(() -> count++).start();
        }

        System.out.println(count);
    }*/

}
