package com.mzl.concurrentProgramming.chap9;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ScheduledThreadPoolExecutorTest
 * @description: ScheduledThreadPoolExecutor的使用
 * @date 2021/7/13 16:32
 */
public class ScheduledThreadPoolExecutorTest {

    public static void main(String[] args) {
        demo();
    }

    /**
     * 简单使用 ScheduledThreadPoolExecutor
     **/
    private static void demo() {
        ScheduledThreadPoolExecutor scheduled = new ScheduledThreadPoolExecutor(2);
        scheduled.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("time:");
            }
        }, 1000, 2000, TimeUnit.MILLISECONDS); //1000表示首次执行任务的延迟时间，2000表示每次执行任务的间隔时间，TimeUnit.MILLISECONDS执行的时间间隔数值单位
    }
}
