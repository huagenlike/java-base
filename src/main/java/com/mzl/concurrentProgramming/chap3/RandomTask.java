package com.mzl.concurrentProgramming.chap3;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author lihuagen
 * @version 1.0
 * @className: RandomTask
 * @description: TODO
 * @date 2021/7/5 11:14
 */
public class RandomTask implements Runnable {
    private final Random rnd;
    protected final int id;
    private final long count;
    private final CountDownLatch latch;

    private RandomTask(Random rnd, int id, long count, CountDownLatch latch) {
        this.rnd = rnd;
        this.id = id;
        this.count = count;
        this.latch = latch;
    }

    protected Random getRandom() {
        return rnd;
    }

    @Override
    public void run() {
        try {
            final Random r = getRandom();
            System.err.println(r.hashCode());
            latch.countDown();
            latch.await();
            final long start = System.currentTimeMillis();
            int sum = 0;
            for (long j = 0; j < count; ++j) {
                sum += r.nextInt();
            }
            final long time = System.currentTimeMillis() - start;
            System.out.println("Thread #" + id + " Time = " + time / 1000.0 + " sec, sum = " + sum);
        } catch (InterruptedException e) {
        }
    }

    private static final long COUNT = 10000000;
    // 通过调整线程数，体现效率差异
    private static final int THREADS = 2;

    public static void main(String[] args) {
//        System.out.println("Random start");
//        testRandom(THREADS, COUNT);
//        System.out.println("ThreadLocal<Random>");
//        testRandomArray(THREADS, COUNT, 1);
//        System.out.println(" Random[] with padding");
//        testRandomArray(THREADS, COUNT, 2);
        System.out.println("ThreadLocalRandom");
        testThreadLocalRandom(THREADS, COUNT);
//        testTL_Random(THREADS, COUNT);
//        System.out.println(" Random[] with no padding");
    }

    /**
     * @param threads
     * @param cnt
     * @description: 单实例下的Random，多个线程同时共享同一个实例。
     * @return: void
     * @author: lihuagen
     * @time: 2021/7/5 11:39
     * 简单的测试下，竞争越激烈，运行时间越长（线程数越多）。
     * 多线程中用的都是同一个对象 r， 所以涉及到 CAS，锁的竞争
     */
    private static void testRandom(final int threads, final long cnt) {
        final CountDownLatch latch = new CountDownLatch(threads);
        final Random r = new Random(100);
        for (int i = 0; i < threads; ++i) {
            final Thread thread = new Thread(new RandomTask(r, i, cnt, latch));
            thread.start();
        }
    }

    /**
     * @param threads
     * @param cnt
     * @param padding 来控制数组缓存的大小。
     * @description: 运用数组 缓存多个Random实例，减少多线程下的竞争资源。
     * @return: void
     * @author: lihuagen
     * @time: 2021/7/5 11:26
     * 通过 padding 来控制数组缓存的大小，每个线程都用对应的对象 rnd[i * padding]，避免多个线程共用同一个对象的情况，减少多线程下的锁竞争资源
     */
    private static void testRandomArray(final int threads, final long cnt, final int padding) {
        final CountDownLatch latch = new CountDownLatch(threads);
        final Random[] rnd = new Random[threads * padding];
        for (int i = 0; i < threads * padding; ++i) //allocate together
        {
            rnd[i] = new Random(100);
        }
        for (int i = 0; i < threads; ++i) {
            final Thread thread = new Thread(new RandomTask(rnd[i * padding], i, cnt, latch));
            thread.start();
        }
    }

    /**
     * @description: ThreadLocalRandom
     * @param threads
     * @param cnt
     * @return: void
     * @author: lihuagen
     * @time: 2021/7/5 11:56
     */
    private static void testThreadLocalRandom(final int threads, final long cnt) {
        final CountDownLatch latch = new CountDownLatch(threads);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < threads; ++i) {
            final Thread thread = new Thread(new RandomTask(random, i, cnt, latch));
            thread.start();
        }
    }

    /**
     * @param threads
     * @param cnt
     * @description: ThreadLocal<Random> 实现当前线程共享一个实例。
     * @return: void
     * @author: lihuagen
     * @time: 2021/7/5 11:48
     * 把java.util.Random实例装入ThreadLocal后执行的效率有些不一样，当线程数超过CPU核心数时性能会有个下降。
     */
    private static void testTL_Random(final int threads, final long cnt) {
        final CountDownLatch latch = new CountDownLatch(threads);
        final ThreadLocal<Random> rnd = new ThreadLocal<Random>() {
            @Override
            protected Random initialValue() {
                return new Random(100);
            }
        };
        for (int i = 0; i < threads; ++i) {
            final Thread thread = new Thread(new RandomTask(null, i, cnt, latch) {
                @Override
                protected Random getRandom() {
                    return rnd.get();
                }
            });
            thread.start();
        }
    }
}
