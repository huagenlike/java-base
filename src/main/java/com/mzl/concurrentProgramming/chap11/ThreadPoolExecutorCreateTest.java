package com.mzl.concurrentProgramming.chap11;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ThreadPoolExecutorCreateTest
 * @description: 线程池创建错误示范
 * @date 2021/7/16 11:48
 * 线程报错的话，不能直观体现具体哪个模块的线程池抛出的异常
 */
public class ThreadPoolExecutorCreateTest {
    static ThreadPoolExecutor executorOne = new ThreadPoolExecutor(5, 5, 1, TimeUnit.MINUTES, new LinkedBlockingDeque<>());
    static ThreadPoolExecutor executorTwo = new ThreadPoolExecutor(5, 5, 1, TimeUnit.MINUTES, new LinkedBlockingDeque<>());

    public static void main(String[] args) {
        // 接受用户链接模块
        executorOne.execute(() -> {
            System.out.println("接受用户链接线程");
            throw new NullPointerException();
        });

        // 具体处理用户请求模块
        executorTwo.execute(() -> {
            System.out.println("具体处理用户请求模块");
        });

        executorOne.shutdown();
        executorTwo.shutdown();
    }
}
