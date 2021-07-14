package com.mzl.concurrentProgramming.chap10;

import java.util.concurrent.Semaphore;

/**
 * @author lihuagen
 * @version 1.0
 * @className: SemaphoreTest
 * @description: 信号量 Semaphore
 * @date 2021/7/14 15:21
 * 假若一个工厂有5台机器，但是有8个工人，一台机器同时只能被一个工人使用，只有使用完了，其他工人才能继续使用。那么我们就可以通过Semaphore来实现：
 */
public class SemaphoreTest {
    public static void main(String[] args) {
        int N = 8;            //工人数
        Semaphore semaphore = new Semaphore(5); //机器数目
        for (int i = 0; i < N; i++) {
            new Worker(i, semaphore).start();
        }
    }

    static class Worker extends Thread {
        private int num;
        private Semaphore semaphore;

        public Worker(int num, Semaphore semaphore) {
            this.num = num;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                // 从此信号量获取一个许可，在提供一个许可前一直将线程阻塞，否则线程被中断。
                semaphore.acquire();
                System.out.println("工人" + this.num + "占用一个机器在生产...");
                Thread.sleep(2000);
                System.out.println("工人" + this.num + "释放出机器");
                // 释放一个许可，将其返回给信号量。
                semaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
