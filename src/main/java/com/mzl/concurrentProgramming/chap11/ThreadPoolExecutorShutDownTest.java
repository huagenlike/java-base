package com.mzl.concurrentProgramming.chap11;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ThreadPoolExecutorCreateTest
 * @description: 线程池使用错误示范
 * @date 2021/7/16 11:48
 * 线程池使用完如果不调用shutdown关闭线程池，则会导致线程池资源一直不被释放。
 */
public class ThreadPoolExecutorShutDownTest {
    static ThreadPoolExecutor executorOne = new ThreadPoolExecutor(5, 5, 1, TimeUnit.MINUTES, new LinkedBlockingDeque<>());
    static ThreadPoolExecutor executorTwo = new ThreadPoolExecutor(5, 5, 1, TimeUnit.MINUTES, new LinkedBlockingDeque<>());

    public static void main(String[] args) {
        // 接受用户链接模块
        executorOne.execute(() -> {
            System.out.println("接受用户链接线程");
        });

        // 具体处理用户请求模块
        executorTwo.execute(() -> {
            System.out.println("具体处理用户请求模块");
        });

        // shutdown 程序会退出，否则代码执行完后，jvm还在运行
        /*executorOne.shutdown();
        executorTwo.shutdown();*/
    }
}
