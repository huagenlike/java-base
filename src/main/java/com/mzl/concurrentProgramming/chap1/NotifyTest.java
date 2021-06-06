package com.mzl.concurrentProgramming.chap1;

/**
 * @program: java-base
 * @description:
 * @author: may
 * @create: 2021-06-06 11:55
 * 一个线程调用共享对象的notify（）方法后，会唤醒一个在该共享变量上调用wait系列方法后被挂起的线程。一个共享变量上可能会有多个线程在等待，具体唤醒哪个等待的线程是随机的。
 * 此外，被唤醒的线程不能马上从wait方法返回并继续执行，它必须在获取了共享对象的监视器锁后才可以返回，也就是唤醒它的线程释放了共享变量上的监视器锁后，被唤醒的线程也不一定会获取到共享对象的监视器锁，这是因为该线程还需要和其他线程一起竞争该锁，只有该线程竞争到了共享变量的监视器锁后才可以继续执行。
 * 类似wait系列方法，只有当前线程获取到了共享变量的监视器锁后，才可以调用共享变量的notify（）方法，否则会抛出IllegalMonitorStateException异常。
 *
 * notifyAll（）方法则会唤醒所有在该共享变量上由于调用wait系列方法而被挂起的线程。
 *
 * 从输出结果可知线程调度器这次先调度了线程A占用CPU来运行，线程A首先获取resourceA上面的锁，然后调用resourceA的wait（）方法挂起当前线程并释放获取到的锁，
 * 然后线程B获取到resourceA上的锁并调用resourceA的wait（）方法，此时线程B也被阻塞挂起并释放了resourceA上的锁，到这里线程A和线程B都被放到了resourceA的阻塞集合里面。
 * 线程C休眠结束后在共享资源resourceA上调用了notify（）方法，这会激活resourceA的阻塞集合里面的一个线程，这里激活了线程A，所以线程A调用的wait（）方法返回了，线程A执行完毕。而线程B还处于阻塞状态。
 **/
public class NotifyTest {
    private static volatile Object resourceA = new Object();

    public static void main(String[] args) throws InterruptedException {
        // 创建线程
        Thread threadA = new Thread(() -> {
            // 获取resourceA共享资源的监视器锁
            synchronized (resourceA) {
                System.out.println("threadA get resourceA lock");
                try {
                    System.out.println("threadA begin wait");
                    resourceA.wait();
                    System.out.println("threadA end wait");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // 创建线程
        Thread threadB = new Thread(() -> {
            synchronized (resourceA) {
                System.out.println("threadB get resourceA lock");
                try {
                    System.out.println("threadB begin wait");
                    resourceA.wait();
                    System.out.println("threadB end wait");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // 创建线程
        Thread threadC = new Thread(() -> {
            synchronized (resourceA) {
                System.out.println("threadC begin notify");
                // resourceA.notify();
                // 线程C调用notifyAll（）方法会唤醒resourceA的等待集合里面的所有线程，这里线程A和线程B都会被唤醒，只是线程B先获取到resourceA上的锁，然后从wait（）方法返回。
                // 线程B执行完毕后，线程A又获取了resourceA上的锁，然后从wait（）方法返回。线程A执行完毕后，主线程返回，然后打印输出。
                // TODO 在共享变量上调用notifyAll（）方法只会唤醒调用这个方法前调用了wait系列函数而被放入共享变量等待集合里面的线程。如果调用notifyAll（）方法后一个线程调用了该共享变量的wait（）方法而被放入阻塞集合，则该线程是不会被唤醒的。尝试把主线程里面休眠1s的代码注释掉，再运行程序会有一定概率输出下面的结果。
                resourceA.notifyAll();
            }
        });

        // 启动线程
        threadA.start();
        threadB.start();
        // 这里启动线程C前首先调用sleep方法让主线程休眠1s，这样做的目的是让线程A和线程B全部执行到调用wait方法后再调用线程C的notify方法。
        Thread.sleep(1000L);
        threadC.start();

        // 等待线程结束
        threadA.join();
        threadB.join();
        threadC.join();

        System.out.println("main over");
    }
}
