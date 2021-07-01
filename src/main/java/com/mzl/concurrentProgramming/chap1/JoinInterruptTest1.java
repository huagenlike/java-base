package com.mzl.concurrentProgramming.chap1;

/**
 * @author lihuagen
 * @version 1.0
 * @className: JoinInterruptTest1
 * @description: TODO
 * @date 2021/7/1 17:19
 */
public class JoinInterruptTest1 {
    //定义一个变量
    public static volatile int number = 0;

    public static void main(String[] args) throws InterruptedException {
        //获取主线程
        Thread mainThread = Thread.currentThread();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mainThread.interrupt();
                    Thread.sleep(1000);
                    System.out.println("子线程做加法,number=" + (++number));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "子线程");

        thread.start();
        System.out.println("子线程加入主线程，所以主线程要等它一起结束");
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            //从运行结果可以发现，即使主线程中断了，子线程一样会继续运行自己的代码逻辑【++number】。
            //而不过要杜绝这种情况，那么在主线程收到中断异常时，在catch时也应该让子线程中断，如下：（注释的话，子线程会继续运行）
            //thread.interrupt();
        }
        //假设这是一段要耗时2秒的业务逻辑
        Thread.sleep(2000);
        //在2秒的逻辑后，需要用number的值做一系列计算然后返回结果
        System.out.println("用number的值【" + number + "】做一系列计算" + "然后返回结果");
    }
}
