package com.mzl.concurrentProgramming.chap1;

/**
 * @program: java-base
 * @description:
 * @author: may
 * @create: 2021-06-05 20:49
 * 当一个线程调用一个共享变量的wait（）方法时，该调用线程会被阻塞挂起，直到发生下面几件事情之一才返回：
 * （1）其他线程调用了该共享对象的notify（）或者notifyAll（）方法；
 * （2）其他线程调用了该线程的interrupt（）方法，该线程抛出InterruptedException异常返回。
 * 那么一个线程如何才能获取一个共享变量的监视器锁呢？
 * （1）执行synchronized同步代码块时，使用该共享变量作为参数。
 * （2）调用该共享变量的方法，并且该方法使用了synchronized修饰。
 *
 * 当线程调用共享对象的wait（）方法时，当前线程只会释放当前共享对象的锁，当前线程持有的其他共享对象的监视器锁并不会被释放。
 **/
public class WaitTest {

    private static volatile Object resourceA = new Object();
    private static volatile Object resourceB = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread threadA = new Thread(() -> {
            // wait()方法没有获取到监视器锁，则会抛出 IllegalMonitorStateException 异常
            // resourceA.wait();
            try {
                // 获取resourceA共享资源的监视器锁
                synchronized (resourceA) {
                    System.out.println("threadA get resourceA lock");
                    // 获取resourceB共享资源的监视器锁
                    synchronized (resourceB) {
                        System.out.println("threadA get resourceB lock");
                        // 线程A阻塞，并释放获取到resourceA的锁
                        System.out.println("threadA release resourceA lock");
                        resourceA.wait();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread threadB = new Thread(() -> {
            try {
                Thread.sleep(1000L);
                // 获取resourceA共享资源的监视器锁
                synchronized (resourceA) {
                    System.out.println("threadB get resourceA lock");
                    System.out.println("threadB try get resourceB lock...");
                    // 获取resourceB共享资源的监视器锁
                    synchronized (resourceB) {
                        System.out.println("threadB get resourceB lock");
                        // 线程B阻塞，并释放获取到resourceA的锁
                        System.out.println("ThreadB release resourceA lock");
                        resourceA.wait();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        threadA.start();
        threadB.start();

        // join 就是主线程执行到这，需要等threadA执行完毕后才会继续往下执行
        threadA.join();
        threadB.join();

        System.out.println("main over");

    }
}
