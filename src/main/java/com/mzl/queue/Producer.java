package com.mzl.queue;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author may
 * @version 1.0
 * @className: Producer
 * @description: 生产者线程
 * @date 2021/4/27 15:55
 */
public class Producer implements Runnable {
    /**
     * 是否在运行标志
     **/
    private volatile boolean isRunning = true;
    /**
     * 阻塞队列
     **/
    private BlockingQueue queue;
    /**
     * 自动更新的值
     **/
    private static AtomicInteger count = new AtomicInteger();
    private static final int DEFAULT_RANGE_FOR_SLEEP = 1000;
    /**
     * 构造函数
     **/
    public Producer(BlockingQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        String data = null;
        Random r = new Random();

        System.out.println("启动生产者线程！");
        try {
            while (isRunning) {
                System.out.println("正在生产数据...");
                //取0~DEFAULT_RANGE_FOR_SLEEP值的一个随机数
                Thread.sleep(r.nextInt(DEFAULT_RANGE_FOR_SLEEP));
                //以原子方式将count当前值加1
                data = "data:" + count.incrementAndGet();
                System.out.println("将数据：" + data + "放入队列...");
                //设定的等待时间为2s，如果超过2s还没加进去返回true
                if (!queue.offer(data, 2, TimeUnit.SECONDS)) {
                    System.out.println("放入数据失败：" + data);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            System.out.println("退出生产者线程！");
        }
    }

    public void stop() {
        isRunning = false;
    }
}
