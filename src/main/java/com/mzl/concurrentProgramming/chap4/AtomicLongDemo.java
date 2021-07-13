package com.mzl.concurrentProgramming.chap4;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author lihuagen
 * @version 1.0
 * @className: AtomicLongDemo
 * @description: TODO
 * @date 2021/7/13 17:17
 */
public class AtomicLongDemo {
    public static void main(String[] args) throws InterruptedException {
        AtomicLong counter = new AtomicLong(0);
        ExecutorService service = Executors.newFixedThreadPool(16);

        for (int i = 0; i < 100; i++) {
            service.submit(new Task(counter));
        }

        Thread.sleep(2000);
        System.out.println(counter.get());
    }

    static class Task implements Runnable {

        private final AtomicLong counter;

        public Task(AtomicLong counter) {
            this.counter = counter;
        }

        @Override
        public void run() {
            counter.incrementAndGet();
        }
    }
}
