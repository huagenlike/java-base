package com.mzl.concurrentProgramming.chap1;

/**
 * @program: java-base
 * @description:
 * @author: may
 * @create: 2021-06-06 12:19
 * 主线程里面启动了两个子线程，然后分别调用了它们的join（）方法，那么主线程首先会在调用threadOne.join（）方法后被阻塞，等待threadOne执行完毕后返回。
 * threadOne执行完毕后threadOne.join（）就会返回，然后主线程调用threadTwo.join（）方法后再次被阻塞，等待threadTwo执行完毕后返回。
 * 这里只是为了演示join方法的作用，在这种情况下使用后面会讲到的CountDownLatch是个不错的选择。
 **/
public class JoinTest {
    public static void main(String[] args) throws InterruptedException {
        Thread threadOne = new Thread(() -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("child threadOne over");
        });

        Thread threadTwo = new Thread(() -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("child threadTwo over");
        });

        // 启动子线程
        threadOne.start();
        threadTwo.start();

        System.out.println("wait all child thread over!");

        // 等待子线程执行完毕，返回
        threadOne.join();
        threadTwo.join();

        System.out.println("all child thread over");

    }
}
