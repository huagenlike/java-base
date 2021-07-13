package com.mzl.concurrentProgramming.chap7;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ArrayBlockingQueueTest
 * @description: TODO
 * @date 2021/7/12 17:06
 */
public class ArrayBlockingQueueTest {
    public static void main(String[] args) {
        int capacity = 10;
        ArrayBlockingQueue<Bread> queue = new ArrayBlockingQueue<Bread>(capacity);

        new Thread(new Producer(queue)).start();
        new Thread(new Producer(queue)).start();
        new Thread(new Consumer(queue)).start();
        new Thread(new Consumer(queue)).start();
        new Thread(new Consumer(queue)).start();
    }
}

/**
 * 生产者
 **/
class Producer implements Runnable{

    //容器
    private final ArrayBlockingQueue<Bread> queue;

    public Producer(ArrayBlockingQueue<Bread> queue){
        this.queue = queue;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        while(true){
            produce();
        }
    }

    public void produce(){
        /**
         * put()方法是如果容器满了的话就会把当前线程挂起
         * offer()方法是容器如果满的话就会返回false。
         */
        try {
            Bread bread = new Bread();
            queue.put(bread);
            System.out.println("Producer:"+bread);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

/**
 * 消费者
 **/
class Consumer implements Runnable{

    //容器
    private final ArrayBlockingQueue<Bread> queue;

    public Consumer(ArrayBlockingQueue<Bread> queue){
        this.queue = queue;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        while(true){
            consume();
        }
    }

    public void consume(){
        /**
         * take()方法和put()方法是对应的，从中拿一个数据，如果拿不到线程挂起
         * poll()方法和offer()方法是对应的，从中拿一个数据，如果没有直接返回null
         */
        try {
            Bread bread = queue.take();
            System.out.println("consumer:"+bread);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Bread {
    public Bread() {

    }
}