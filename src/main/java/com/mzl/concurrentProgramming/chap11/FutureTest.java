package com.mzl.concurrentProgramming.chap11;

import java.util.concurrent.*;

/**
 * @author lihuagen
 * @version 1.0
 * @className: FutureTest
 * @description: TODO
 * @date 2021/7/16 13:44
 * 代码(1)创建了一个单线程和一个队列元素个数为1的线程池,并且把拒绝策略设置为 DiscardPolicy
 * 代码(2)向线程池提交了一个任务one,并且这个任务会由唯一的线程来执行,任务在打印 start runnable one后会阻塞该线程5s。
 * 代码(3)向线程池提交了一个任务two,这时候会把任务two放入阻塞队列。
 * 代码(4)向线程池提交任务three,由于队列已满所以触发拒绝策略丢弃任务three。
 * 从执行结果看,在任务one阻塞的5s内,主线程执行到了代码(5)并等待任务one执行完毕,当任务one执行完毕后代码(5)返回,主线程打印出 task one null。
 * 任务one执行完成后线程池的唯一线程会去队列里面取出任务two并执行,所以输出 start runnable two然后代码(6)返回,这时候主线程输出 task two null。
 * 然后执行代码(7)等待任务 three执行完毕。从执行结果看,代码(7)会一直阻塞而不会返回,至此问题产生。 *
 * 如果把拒绝策略修改为 DiscardOldestPolicy,也会存在有一个任务的get方法一直阻塞,只是现在是任务two被阻塞。
 * 但是如果把拒绝策略设置为默认的 AbortPolicy则会正常返回,并且会输出如下结果。
 */
public class FutureTest {
    // 1 线程池单个线程，线程池队列元素个数为1
    private final static ThreadPoolExecutor executorService = new ThreadPoolExecutor(1, 1, 1L, TimeUnit.MINUTES,
            new ArrayBlockingQueue<Runnable>(1),
            new ThreadPoolExecutor.AbortPolicy());

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 2 添加任务one
        Future futureOne = executorService.submit(() -> {
            System.out.println("start runnable one");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // 3 添加任务two
        Future futureTwo = executorService.submit(() -> {
            System.out.println("start runnable two");
        });

        // 4 添加任务three
        Future futureThree = null;
        try {
            futureThree = executorService.submit(() -> {
                System.out.println("start runnable three");
            });
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        // 5 等待任务one执行完毕
        System.out.println("task one " + futureOne.get());
        // 6 等待任务two执行完毕
        System.out.println("task two " + futureTwo.get());
        // 7 等待任务three执行完毕
        System.out.println("task three " + (futureThree == null ? null : futureThree.get()));

    }
}
