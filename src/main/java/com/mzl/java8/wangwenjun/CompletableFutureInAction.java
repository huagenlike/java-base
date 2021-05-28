package com.mzl.java8.wangwenjun;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

import static java.util.stream.Collectors.toList;

/***************************************
 * @author:Alex Wang
 * @Date:2016/11/8 QQ:532500648
 * QQ交流群:286081824
 * // CompletableFuture ：实现异步编程
 *         // supplyAsync：有返回值
 *         // whenComplete：是执行当前任务的线程执行继续执行 whenComplete 的任务。
 *         // whenCompleteAsync：是执行把 whenCompleteAsync 这个任务继续提交给线程池来进行执行。
 *         // thenCompose：将在与上游任务相同的线程上调用generateRequest()(如果上游任务已经完成，则调用该线程).
 *         // thenComposeAsync：将在提供的执行程序(如果提供)上调用generateRequest()，否则将在默认的ForkJoinPool上调用.
 ***************************************/
public class CompletableFutureInAction {

    private static final Random random = new Random(System.currentTimeMillis());

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        /*CompletableFuture<Double> future = new CompletableFuture<>();
        new Thread(() -> {
            try {
                Thread.sleep(10000L);
                future.complete(1000d);
            } catch (InterruptedException e) {
                future.completeExceptionally(e);
            }

        }).start();

        System.out.println("..............");
        future.whenComplete((v, t) -> {
            System.out.println(v);
            t.printStackTrace();
        });*/

        /*CompletableFuture<Double> future = CompletableFuture.supplyAsync(CompletableFutureInAction::get);

        future.whenComplete((v, t) -> {
            System.out.println(v);
            t.printStackTrace();
        });*/

        long start = System.currentTimeMillis();
        List<Double> doubles = Arrays.asList(random.nextDouble(), random.nextDouble(), random.nextDouble(), random.nextDouble(), random.nextDouble());
        System.out.println(doubles);
        List<CompletableFuture<Double>> futures = doubles
                .stream()
                .map(d -> CompletableFuture.supplyAsync(() -> {
                    try {
                        Thread.sleep(10000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return d * 10d;
                }))
                .collect(toList());

        List<Double> collect = futures.stream().parallel().map(f -> f.join()).collect(toList());
        System.out.println(collect);
        System.out.println(start-System.currentTimeMillis());

        /*Executor executor = Executors.newFixedThreadPool(2, r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
        executor.execute(() -> System.out.println("sfsdfsfs"));*/

        /*Double value = CompletableFuture.supplyAsync(CompletableFutureInAction::get)
                .whenComplete((v, t) -> System.out.println(">>>>" + v))
                .thenCompose(i -> CompletableFuture.supplyAsync(() -> i + 10))
                .get();
        System.out.println(value);*/
    }

    private static double get() {
        try {
            Thread.sleep(10000L);
            double v = random.nextDouble();
            System.out.println(v);
            return v;
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
    }
}
