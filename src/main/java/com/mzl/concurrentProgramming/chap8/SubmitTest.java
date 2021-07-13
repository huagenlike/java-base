package com.mzl.concurrentProgramming.chap8;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author lihuagen
 * @version 1.0
 * @className: SubmitTest
 * @description: ThreadPoolExecutor，使用submit()方法
 * @date 2021/7/13 15:26
 * 使用submit()的话，可以在submit()中执行一个Callable接口的实现类，submit()方法可以返回一个Future<> future，然后使用future.get()方法获得返回结果，由于future.get()会使调用线程池的线程阻塞，等待返回结果，所以可以先将Future<> future放入一个集合，等多线程执行完毕后再遍历集合获得运算结果。
 */
public class SubmitTest {
    public static void main(String[] args) {
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(6, 10, 10L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(20),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy());

        // 存放Future<>的集合
        List<Future<RunResult>> list = new ArrayList<Future<RunResult>>(12);

        // 线程同步器
        CountDownLatch main = new CountDownLatch(12);

        for (int i = 1; i <= 12; i++) {
            RunResult result = new RunResult();
            result.setParam(i);
            MyCallable callable = new MyCallable(result, main);
            Future<RunResult> runResultFuture = threadPool.submit(callable);
            list.add(runResultFuture);
        }

        try {
            main.await();
            for (int i = 0; i < list.size(); i++) {
                RunResult runResult = list.get(i).get();
                System.out.println(JSON.toJSON(runResult));
            }
            // 线程池不用了，关闭线程池
            threadPool.shutdown();
            //threadPool.shutdownNow();
        } catch (InterruptedException e) {

        } catch (ExecutionException e) {

        }
    }
}

class MyCallable implements Callable<RunResult> {
    // 运算结果类
    private RunResult result;
    // 线程同步器
    private CountDownLatch main;

    @Override
    public RunResult call() throws Exception {
        String name = Thread.currentThread().getName();
        System.out.println(name + " 线程开始执行" + "-" + result.getParam());
        for (int i = 0; i < 1; i++) {
            for (int j = 1; j <= 200000000; j++) {
                if (j == 200000000 && null != result.getParam()) {
                    result.setResult(result.getParam() * 10);
                    result.setSuccess(true);
                    System.out.println(name + " 线程正在进行计算" + "-" + result.getParam());
                } else {
                    result.setSuccess(false);
                }
            }
        }
        System.out.println(name + " 线程执行完毕" + "-" + result.getParam());
        main.countDown();
        return result;
    }

    public MyCallable(RunResult result, CountDownLatch main) {
        super();
        this.result = result;
        this.main = main;
    }

    public MyCallable() {
        super();
    }
}