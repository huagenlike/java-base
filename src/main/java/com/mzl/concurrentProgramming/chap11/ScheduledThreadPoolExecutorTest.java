package com.mzl.concurrentProgramming.chap11;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ScheduledThreadPoolExecutorTest
 * @description: TimerTest 问题的解决方案
 * @date 2021/7/15 10:04
 */
public class ScheduledThreadPoolExecutorTest {
    static ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);

    public static void main(String[] args) {
        scheduledThreadPoolExecutor.schedule(() -> {
            System.out.println("--- one task ---");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            throw new RuntimeException("error");
        }, 500, TimeUnit.MILLISECONDS);

        scheduledThreadPoolExecutor.schedule(() -> {
            for (int i = 0; i < 2; ++i) {
                System.out.println("--- two task ---");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 1000, TimeUnit.MILLISECONDS);

        scheduledThreadPoolExecutor.shutdown();
    }
}
