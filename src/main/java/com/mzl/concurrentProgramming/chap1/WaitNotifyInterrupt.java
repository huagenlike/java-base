package com.mzl.concurrentProgramming.chap1;

/**
 * @program: java-base
 * @description:
 * @author: may
 * @create: 2021-06-05 21:15
 * 当一个线程调用共享对象的wait（）方法被阻塞挂起后，如果其他线程中断了该线程，则该线程会抛出InterruptedException异常并返回。
 **/
public class WaitNotifyInterrupt {
    static Object obj = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread threadA = new Thread(() -> {
            try {
                System.out.println("---begin---");
                // 阻塞当前线程
                synchronized (obj) {
                    obj.wait();
                }
                System.out.println("---end---");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        threadA.start();

        // 后主线程在休眠1s后中断了threadA线程，中断后threadA在obj.wait（）处抛出java.lang.InterruptedException异常而返回并终止。
        Thread.sleep(1000L);

        System.out.println("begin interrupt threadA");
        threadA.interrupt();
        System.out.println("end interrupt threadA");
    }
}
