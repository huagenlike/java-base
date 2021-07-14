package com.mzl.concurrentProgramming.chap10;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * @author lihuagen
 * @version 1.0
 * @className: CountDownLatchTest1
 * @description: 集齐七龙珠，召唤神龙
 * @date 2021/7/14 9:56
 */
public class CountDownLatchTest1 {

    private static final Integer THREAD_COUNT_NUM = 7;
    private static CountDownLatch countDownLatch = new CountDownLatch(THREAD_COUNT_NUM);

    /**
     * implement
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = initExecutor();

        List<String> list = Collections.synchronizedList(Lists.newArrayList());
        for (int i = 0; i < THREAD_COUNT_NUM; i++) {
            int index = i + 1;
            executor.execute(() -> findDragonBall(index, list));
        }
        countDownLatch.await();
        System.out.println("集齐七龙珠，召唤神龙！");
        System.out.println(list.toString());
        executor.shutdown();
    }

    /**
     * 初始化线程池
     */
    private static ExecutorService initExecutor() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("my-ThreadPool-%d").build();
        return new ThreadPoolExecutor(7, 10, 5, TimeUnit.SECONDS, new SynchronousQueue<>(),
                namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * 找第index颗星的龙珠
     *
     * @param index 第几颗星第龙珠
     * @param list  存所有龙珠的list
     */
    private static void findDragonBall(int index, List<String> list) {
        try {
            int sleepInt = new Random().nextInt(3000);
            Thread.sleep(sleepInt);
            System.out.println("第" + index + "颗龙珠已经收集到！    sleep " + sleepInt);
            list.add(index + "");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            countDownLatch.countDown();
        }
    }
}
