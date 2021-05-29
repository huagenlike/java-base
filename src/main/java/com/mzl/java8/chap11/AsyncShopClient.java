package com.mzl.java8.chap11;

import java.util.concurrent.Future;

import static com.mzl.java8.chap11.ExecutorServiceTest.doSomethingElse;


public class AsyncShopClient {

    public static void main(String[] args) {
        AsyncShop shop = new AsyncShop("BestShop");
        long start = System.nanoTime();
        // 查询商店，试图取得商品的价格
        Future<Double> futurePrice = shop.getPrice("myPhone");
        long incocationTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("Invocation returned after " + incocationTime + " msecs");
        // 执行更多任务，比如查询其他商店
        doSomethingElse();
        // 在计算商品价格的同时
        try {
            // 从Future对象中读取价格，如果价格未知，会发生阻塞
            Double price = futurePrice.get();
            System.out.println("Price is %.2f%n" + price);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        long retrivalTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("Price returned after " + retrivalTime + " msecs");
    }
}