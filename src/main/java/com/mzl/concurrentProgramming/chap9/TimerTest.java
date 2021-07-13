package com.mzl.concurrentProgramming.chap9;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author lihuagen
 * @version 1.0
 * @className: TimerTest
 * @description: TODO
 * @date 2021/7/13 16:34
 */
public class TimerTest {
    public static void main(String[] args) {
        // 多线程并行处理定时任务时，Timer运行多个TimeTask时，只要其中之一没有捕获抛出的异常，其它任务便会自动终止运行，使用ScheduledExecutorService则没有这个问题。
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("123");
            }
        }, 2000, 1000); //2000表示第一次执行任务延迟时间，1000表示以后每隔多长时间执行一次run里面的任务
    }
}
