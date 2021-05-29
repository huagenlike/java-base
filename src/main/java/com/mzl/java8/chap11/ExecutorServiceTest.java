package com.mzl.java8.chap11;

import java.util.concurrent.*;

/**
 * @program: java-base
 * @description: 使用Future以异步的方式执行一个耗时的操作
 * @author: may
 * @create: 2021-05-29 14:52
 **/
public class ExecutorServiceTest {
    public static void main(String[] args) {
        // 创建Executor-Service，通过它你可以向线程池提交任务
        // newCachedThreadPool：创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
        ExecutorService executor = Executors.newCachedThreadPool();

        // ←─向Executor-Service提交一个Callable对象
        Future<Double> future = executor.submit(new Callable<Double>() {

            public Double call() {
                // ←─以异步方式在新的线程中执行耗时的操作
                return doSomeLongComputation();
            }
        });
        // ←─异步操作进行的同时，你可以做其他的事情
        doSomethingElse();

        try {
            // ←─获取异步操作的结果，如果最终被阻塞，无法得到结果，那么在最多等待1秒钟之后退出
            Double result = future.get(1, TimeUnit.SECONDS);
            System.out.println(result);
        } catch (ExecutionException ee) {
            // 计算抛出一个异常
        } catch (InterruptedException ie) {
            // 当前线程在等待过程中被中断
        } catch (TimeoutException te) {
            // 在Future对象完成之前超过已过期
        }
    }

    public static Double doSomeLongComputation() {
        return 0d;
    }

    public static void doSomethingElse() {
        System.out.println("执行了一些其他业务");
    }

}
