package com.mzl.concurrentProgramming.chap8;

import java.util.concurrent.*;

/**
 * @author lihuagen
 * @version 1.0
 * @className: CustomThreadFactory
 * @description: 自定义线程创建
 * @date 2021/7/13 10:22
 */
public class CustomThreadFactory {
    private static ExecutorService pool;

    public static void main(String[] args) {
        //自定义线程工厂
        pool = new ThreadPoolExecutor(2, 4, 1000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(5),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        System.out.println("线程" + r.hashCode() + "创建");
                        //线程命名
                        Thread th = new Thread(r, "threadPool" + r.hashCode());
                        return th;
                    }
                }, new ThreadPoolExecutor.CallerRunsPolicy());

        for (int i = 0; i < 10; i++) {
            pool.execute(new ThreadTask());
        }
    }
}
