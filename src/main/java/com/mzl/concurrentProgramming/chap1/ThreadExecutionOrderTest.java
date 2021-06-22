package com.mzl.concurrentProgramming.chap1;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ThreadOhterTest
 * @description: start() 和 run() 的区别
 * @date 2021/6/17 17:26
 * 1. start（）方法来启动线程，真正实现了多线程运行。这时无需等待run方法体代码执行完毕，可以直接继续执行下面的代码；通过调用Thread类的start()方法来启动一个线程， 这时此线程是处于就绪状态， 并没有运行。 然后通过此Thread类调用方法run()来完成其运行操作的， 这里方法run()称为线程体，它包含了要执行的这个线程的内容， Run方法运行结束， 此线程终止。然后CPU再调度其它线程。
 * 2. run（）方法当作普通方法的方式调用。程序还是要顺序执行，要等待run方法体执行完毕后，才可继续执行下面的代码； 程序中只有主线程——这一个线程， 其程序执行路径还是只有一条， 这样就没有达到写线程的目的。
 * 记住：多线程就是分时利用CPU，宏观上让所有线程一起执行 ，也叫并发
 */
public class ThreadExecutionOrderTest {

    public static void main(String[] args) {
        Thread threadA = new Thread(() -> {
            System.out.println("threadA线程内打印");
        });

        Thread threadB = new Thread(() -> {
            System.out.println("threadB线程内打印");
        });

        // 会先打印[threadA线程内打印]，再打印[threadB线程内打印]，最后打印[主线程打印]。变成顺序执行
//        threadA.run();
//        threadB.run();
        // 会先打印[主线程打印]，threadA 和 threadB 那个先抢到就先执行
        threadA.start();
        threadB.start();
        System.out.println("主线程打印");


    }
}
