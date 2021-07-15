package com.mzl.concurrentProgramming.chap11;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author lihuagen
 * @version 1.0
 * @className: TimerTest
 * @description: Timer行多个TimerTask时，只要其中一个TimerTask在执行中向run方法外抛出了异常，则其他任务也会自动终止。
 * @date 2021/7/15 9:56
 * 如上代码首先添加了第一个任务,让其在500ms后执行。然后添加了第二个任务在ls后执行,我们期望当第一个任务输出- -one Task-后,等待1s,第二个任务输出--twoTask-,但是执行代码后,输出结果为
 */
public class TimerTest {
    // 创建定时器对象
    static Timer timer = new Timer();

    public static void main(String[] args) {
        // 添加任务1，延迟500ms执行
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("--- one Task ---");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                throw new RuntimeException("error");
            }
        }, 500);

        // 添加任务2，延迟500ms执行
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (;;) {
                    System.out.println("--- two Task ---");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, 1000);
    }
}
