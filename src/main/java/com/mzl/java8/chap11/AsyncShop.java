package com.mzl.java8.chap11;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static com.mzl.java8.chap11.Util.delay;
import static com.mzl.java8.chap11.Util.format;

/**
 * 异步方法（最佳价格查询器）
 */
public class AsyncShop {

    private final String name;
    private final Random random;

    public AsyncShop(String name) {
        this.name = name;
        random = new Random(name.charAt(0) * name.charAt(1) * name.charAt(2));
    }

    public Future<Double> getPrice(String product) {
        /*CompletableFuture<Double> futurePrice = new CompletableFuture<>();
        // 创建CompletableFuture对象，它会包含计算的结果
        new Thread( () -> {
                    try {
                        // 在另一个线程中以异步方式执行计算
                        double price = calculatePrice(product);
                        // 需长时间计算的任务结束并得出结果时，设置Future的返回值
                        futurePrice.complete(price);
                    } catch (Exception ex) {
                        // 使用CompletableFuture的completeExceptionally方法将导致CompletableFuture内发生问题的异常抛出。
                        // 否则就抛出导致失败的异常，完成这次Future操作
                        futurePrice.completeExceptionally(ex);
                    }
        }).start();
        // 无需等待还没结束的计算，直接返回Future对象
        return futurePrice;*/
        // 使用工厂方法supplyAsync创建CompletableFuture
        return CompletableFuture.supplyAsync(() -> calculatePrice(product));
    }

    private double calculatePrice(String product) {
        delay();
        // 模拟价格计算过程中产生了错误
        if (true) throw new RuntimeException("product not available");
        return format(random.nextDouble() * product.charAt(0) + product.charAt(1));
    }

}