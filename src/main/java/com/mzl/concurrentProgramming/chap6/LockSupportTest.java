package com.mzl.concurrentProgramming.chap6;

import java.util.concurrent.locks.LockSupport;

/**
 * @author lihuagen
 * @version 1.0
 * @className: LockSupportTest
 * @description: LockSupport是一个线程工具类
 * @date 2021/7/6 16:09
 * LockSupport是一个线程工具类，所有的方法都是静态方法，可以让线程在任意位置阻塞，也可以在任意位置唤醒
 * 两类主要的方法：park（停车阻塞线程）和unpark（启动唤醒线程）。
 * unpark其实就相当于一个许可，告诉特定线程你可以停车，特定线程想要park停车的时候一看到有许可，就可以立马停车继续运行了。因此其执行顺序可以颠倒。
 */
public class LockSupportTest {

    public static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println("进入线程");
            LockSupport.park();
            System.out.println("t1线程运行结束");
        }
    }

    public static void main(String[] args) throws InterruptedException {
//        parkTest();
//        unparkTest();
//        lockSupportTest();
//        lockSupportTest1();
        parkNanosTest();
    }

    // 如果调用park()方法的线程已经拿到了与 LockSupport 关联的许可证，则 调用LockSupport.park() 时会马上返回，否则调用线程会被禁止参与线程的调度，也就是会被阻塞挂起。
    private static void parkTest() {
        System.out.println("begin park!");
        LockSupport.park();
        System.out.println("end park!");
    }

    /**
     * @Author lhg
     * @Description TODO
     * @Date 16:14 2021/7/6
     * @Param []
     * @return void
     **/
    private static void unparkTest() {
        System.out.println("begin park!");
        // 使用当前线程获取到许可证
        LockSupport.unpark(Thread.currentThread());
        // 再次调用park方法
        LockSupport.park();
        System.out.println("end park!");
    }

    // park/unpark 与 wait/notify的区别：
    // （1）wait和notify都是Object中的方法,在调用这两个方法前必须先获得锁对象，但是park不需要获取某个对象的锁就可以锁住线程。
    // （2）notify只能随机选择一个线程唤醒，无法唤醒指定的线程，unpark却可以唤醒一个指定的线程。
    private static void lockSupportTest() throws InterruptedException {
        // 1.创建子线程 t1，子线程启动后调用park方法，由于在默认情况下子线程没有持有许可证，因而他会把自己挂起
        MyThread t1 = new MyThread();
        t1.start();

        // 主线程休眠2s
        Thread.sleep(2000L);

        System.out.println("t1已经启动，但是内部进行了park");
        // 2.主线程执行unpark方法，参数为子线程，这样做的目的是让子线程持有许可证，然后子线程调用的park方法就会返回了。
        LockSupport.unpark(t1);
        System.out.println("LockSupport进行了unpark");
    }

    // park方法返回时不会告诉你因何原因返回，所以调用者需要根据之前调用park方法的原因，再次检查条件是否满足，如果不满足还需要再次调用park方法。
    private static void lockSupportTest1 () throws InterruptedException {
        Thread thread = new Thread(() -> {
            System.out.println("child thread begin park!");
            // 调用park方法，挂起自己，只有被中断才会退出循环
            while (!Thread.currentThread().isInterrupted()) {
                LockSupport.park();
            }
            System.out.println("child thread unpark!");
        });

        // 启动子线程
        thread.start();

        // 子线程不中断，即使调用unpark(thread)方法，子线程也不会结束
        LockSupport.unpark(thread);

        // 主线程休眠2s
        Thread.sleep(2000L);

        System.out.println("main thread begin unpark!");

        // 中断子线程
        thread.interrupt();
    }

    // 如果调用 park 方法的线程已经拿到了与 LockSupport 关联的许可证，则调用 LockSupport.parkNanos(long nanos）方法后会马上返回。该方法的不同在于 ，如果没有拿到许可证，则调用线程会被挂起 nanos 时间后修改为自动返回。
    private static void parkNanosTest() {
        System.out.println("begin park");

        // 线程挂起10秒,参数单位是纳秒
        LockSupport.parkNanos(1000 * 1000 * 1000 * 10L);

        System.out.println("end park!");
    }

}
