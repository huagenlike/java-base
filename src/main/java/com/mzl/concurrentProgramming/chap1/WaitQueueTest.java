package com.mzl.concurrentProgramming.chap1;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author lihuagen
 * @version 1.0
 * @className: WaitQueueTest
 * @description: TODO
 * @date 2021/7/1 15:17
 * 防止虚假唤醒，这个例子不够形象，后期理解多线程后重新举例
 */
public class WaitQueueTest {

    volatile static Queue<String> queue = new LinkedList<>();

    public static void main(String[] args) {

        // 生产线程
        Thread threadA = new Thread(() -> {
            synchronized (queue) {
                // 消费队列满，则等待队列空闲
                System.out.println("threadA：" + queue.size());
                while (queue.size() == 10) {
                    try {
                        // 挂起当前线程，并释放通过同步获取的queue上的锁，让消费者线程可以获取该锁，然后获取队列里面的元素
                        System.out.println("threadA：添加等待");
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // 空闲则生成元素，并通知消费者线程
                queue.add(Thread.currentThread().getName());
                queue.notifyAll();
            }
        });

        // 消费者线程
        Thread threadB = new Thread(() -> {
            synchronized (queue) {
                // 消费者队列为空
                System.err.println("threadB：" + queue.size());
                while (queue.size() == 0) {
                    try {
                        // 挂起当前线程，并释放通过同步块获取的queue上的锁，让生产者线程可以获取该锁，并生产元素放入队列
                        System.err.println("threadB：删除等待");
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    Thread.sleep(500L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 消费元素，并通知唤醒生产线程
                queue.remove();
                queue.notifyAll();
            }
        });

        for (int i = 0; i < 20; i++) {
            new Thread(threadA, "threadA" + i).start();
            new Thread(threadB,"threadB" + i).start();
        }

    }
}
