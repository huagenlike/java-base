package com.mzl.concurrentProgramming.chap1;

/**
 * @author lihuagen
 * @version 1.0
 * @className: TestWaitLongTime
 * @description: wait() 、 wait(long timeout) 和 wait(long timeoutMillis, int nanos) 的区别
 * @date 2021/7/1 16:17
 */
public class TestWaitLongTime {
    static Object obj = new Object();

    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();

        Thread threadA = new Thread(() -> {
            synchronized (obj) {
                try {
                    obj.wait(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("threadA：被唤醒了");
            System.out.println(System.currentTimeMillis() - startTime);
        });

        Thread threadB = new Thread(() -> {
            // wait 在对象没有获取到监控器锁的时候，会抛出异常 IllegalMonitorStateException
            synchronized (obj) {
                try {
                    // 默认值0L，会一直等，直到notify（）或者notifyAll（）方法，或者其他线程调用了该线程的interrupt（）方法，该线程抛出InterruptedException异常返回
                    obj.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("threadB：被唤醒了");
            System.out.println(System.currentTimeMillis() - startTime);
        });

        Thread threadC = new Thread(() -> {
            synchronized (obj) {
                try {
                    // 在指定timeout的时间过去之后被唤醒
                    obj.wait(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("threadC：被唤醒了");
            System.out.println(System.currentTimeMillis() - startTime);
        });

        Thread threadD = new Thread(() -> {
            synchronized (obj) {
                try {
                    // wait(long timeoutMillis, int nanos)
                    // timeoutMillis：毫秒
                    // nanos：纳秒，最大值999999
                    obj.wait(2000, 999999);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("threadD：被唤醒了");
            System.out.println(System.currentTimeMillis() - startTime);
        });


        threadA.start();
        Thread.sleep(1000L);

        threadB.start();
        threadC.start();
        threadD.start();
    }
}
