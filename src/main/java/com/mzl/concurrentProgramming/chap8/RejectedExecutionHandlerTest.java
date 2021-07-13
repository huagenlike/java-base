package com.mzl.concurrentProgramming.chap8;

import java.util.concurrent.*;

/**
 * @author lihuagen
 * @version 1.0
 * @className: RejectedExecutionHandlerTest
 * @description: 自定义拒绝策略
 * @date 2021/7/13 10:10
 * 1.创建核心线程
 * 2.线程池创建线时，发现核心线程满了，往阻塞队列里存
 * 3.阻塞队列存放了，当前线程数<最大线程数，创建线程
 * 4.当前线程已是最大线程数，且阻塞队列已满，执行拒绝策略
 */
public class RejectedExecutionHandlerTest {
    private static ExecutorService pool;

    public static void main(String[] args) {
        //自定义拒绝策略
        pool = new ThreadPoolExecutor(1, 2, 1000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(5), Executors.defaultThreadFactory(), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                System.out.println(r.toString() + "执行了拒绝策略");
            }
        });
        for (int i = 0; i < 10; i++) {
            pool.execute(new ThreadTask1());
        }
    }
}

class ThreadTask1 implements Runnable {
    @Override
    public void run() {
        try {
            //让线程阻塞，使后续任务进入缓存队列
            Thread.sleep(1000);
            System.out.println("ThreadName:" + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
