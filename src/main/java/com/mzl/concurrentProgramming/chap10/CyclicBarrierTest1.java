package com.mzl.concurrentProgramming.chap10;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lihuagen
 * @version 1.0
 * @className: CyclicBarrierTest1
 * @description: TODO
 * @date 2021/7/14 16:09
 * 在如上代码中,每个子线程在执行完阶段1后都调用了 await方法,等到所有线程都到达屏障点后才会一块往下执行,这就保证了所有线程都完成了阶段1后才会开始执行阶段2。
 * 然后在阶段2后面调用了 await方法,这保证了所有线程都完成了阶段2后,才能开始阶段3的执行。
 * 这个功能使用单个 CountdownLatch 是无法完成的
 */
public class CyclicBarrierTest1 {
    private static CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.submit(() ->{
            try {
                System.out.println(Thread.currentThread() + " step1");
                cyclicBarrier.await();
                System.out.println(Thread.currentThread() + " step2");
                cyclicBarrier.await();
                System.out.println(Thread.currentThread() + " step3");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        });

        executorService.submit(() ->{
            try {
                System.out.println(Thread.currentThread() + " step1");
                cyclicBarrier.await();
                System.out.println(Thread.currentThread() + " step2");
                cyclicBarrier.await();
                System.out.println(Thread.currentThread() + " step3");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        });

        executorService.shutdown();
    }
}
