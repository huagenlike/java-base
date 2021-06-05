package com.mzl.concurrentProgramming.chap1;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @program: java-base
 * @description:
 * @author: may
 * @create: 2021-06-05 09:57
 **/
public class ThreadTest {

    // 继承了Thread类，并重写了run（）方法。
    // 任务和代码没有分离，当多个线程执行同样代码的时候，就需要多份任务代码，没有返回值
    public static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println("I am a child thread");
        }
    }

    // 多个线程可以共用代码逻辑，没有返回值
    public static class RunnableTask implements Runnable {
        @Override
        public void run() {
            System.out.println("I am a child thread");
        }
    }

    public static class CallerTask implements Callable<String> {
        @Override
        public String call() throws Exception {
            Thread.sleep(5000L);
            return "hello";
        }
    }

    public static void main(String[] args) {
        // 方式一，无返回值
        // 创建线程
        MyThread thread = new MyThread();
        // 启动线程
        thread.start();

        System.out.println("------------------");

        // 方式二，无返回值
        RunnableTask task = new RunnableTask();
        // 两个线程可以共用task代码逻辑
        new Thread(task).start();
        new Thread(task).start();

        System.out.println("------------------");

        // 方式三，有返回值
        // 创建异步任务
        FutureTask<String> futureTask = new FutureTask<>(new CallerTask());
        // 启动线程
        new Thread(futureTask).start();
        try {
            // 等待任务执行完毕，并返回结果
            String result = futureTask.get();
            System.out.println(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


}
