package com.mzl.concurrentProgramming;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.*;

/**
 * @author lihuagen
 * @version 1.0
 * @className: Test02
 * @description: TODO
 * @date 2021/6/24 10:49
 *  CopyOnWriteArrayList和Collections.synchronizedList是实现线程安全的列表的两种方式。
 *  两种实现方式分别针对不同情况有不同的性能表现，其中CopyOnWriteArrayList的写操作性能较差，而多线程的读操作性能较好。
 *  而Collections.synchronizedList的写操作性能比CopyOnWriteArrayList在多线程操作的情况下要好很多，而读操作因为是采用了synchronized关键字的方式，其读操作性能并不如CopyOnWriteArrayList。
 */
public class Test02 {
    private int NUM = 1000000;
    private int THREAD_COUNT = 50;

    @Test
    public void testAdd() throws Exception {
        // CopyOnWriteArrayList写性能差，差别极大
        // Collections.synchronizedList 写性能好，差别极大
        List<Integer> list1 = new CopyOnWriteArrayList<Integer>();
        List<Integer> list2 = Collections.synchronizedList(new ArrayList<Integer>());
        Vector<Integer> v  = new Vector<Integer>();

        CountDownLatch add_countDownLatch = new CountDownLatch(THREAD_COUNT);
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        int add_copyCostTime = 0;
        int add_synchCostTime = 0;
        for (int i = 0; i < THREAD_COUNT; i++) {
            add_copyCostTime += executor.submit(new AddTestTask(list1, add_countDownLatch)).get();
        }
        System.out.println("CopyOnWriteArrayList add method cost time is " + add_copyCostTime);

        for (int i = 0; i < THREAD_COUNT; i++) {
            add_synchCostTime += executor.submit(new AddTestTask(list2, add_countDownLatch)).get();
        }
        System.out.println("Collections.synchronizedList add method cost time is " + add_synchCostTime);


    }

    @Test
    public void testGet() throws Exception {
        List<Integer> list = initList();

        // CopyOnWriteArrayList 读性能差，差别不大
        // Collections.synchronizedList 读性能好，差别不大
        List<Integer> list1 = new CopyOnWriteArrayList<Integer>(list);
        List<Integer> list2 = Collections.synchronizedList(list);

        int get_copyCostTime = 0;
        int get_synchCostTime = 0;
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch get_countDownLatch = new CountDownLatch(THREAD_COUNT);
        for (int i = 0; i < THREAD_COUNT; i++) {
            get_copyCostTime += executor.submit(new GetTestTask(list1, get_countDownLatch)).get();
        }
        System.out.println("CopyOnWriteArrayList add method cost time is " + get_copyCostTime);

        for (int i = 0; i < THREAD_COUNT; i++) {
            get_synchCostTime += executor.submit(new GetTestTask(list2, get_countDownLatch)).get();
        }
        System.out.println("Collections.synchronizedList add method cost time is " + get_synchCostTime);

    }


    private List<Integer> initList() {
        List<Integer> list = new ArrayList<Integer>();
        int num = new Random().nextInt(1000);
        for (int i = 0; i < NUM; i++) {
            list.add(num);
        }
        return list;
    }

    class AddTestTask implements Callable<Integer> {
        List<Integer> list;
        CountDownLatch countDownLatch;

        AddTestTask(List<Integer> list, CountDownLatch countDownLatch) {
            this.list = list;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public Integer call() throws Exception {
            int num = new Random().nextInt(1000);
            long start = System.currentTimeMillis();
            for (int i = 0; i < NUM; i++) {
                list.add(num);
            }
            long end = System.currentTimeMillis();
            countDownLatch.countDown();
            return (int) (end - start);
        }
    }

    class GetTestTask implements Callable<Integer> {
        List<Integer> list;
        CountDownLatch countDownLatch;

        GetTestTask(List<Integer> list, CountDownLatch countDownLatch) {
            this.list = list;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public Integer call() throws Exception {
            int pos = new Random().nextInt(NUM);
            long start = System.currentTimeMillis();
            for (int i = 0; i < NUM; i++) {
                list.get(pos);
            }
            long end = System.currentTimeMillis();
            countDownLatch.countDown();
            return (int) (end - start);
        }
    }
}
