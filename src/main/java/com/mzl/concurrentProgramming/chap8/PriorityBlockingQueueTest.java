package com.mzl.concurrentProgramming.chap8;

import java.util.concurrent.*;

/**
 * @author lihuagen
 * @version 1.0
 * @className: PriorityBlockingQueueTest
 * @description: 优先任务队列：优先任务队列通过PriorityBlockingQueue实现，下面我们通过一个例子演示下
 * @date 2021/7/13 9:50
 * 大家可以看到除了第一个任务直接创建线程执行外，其他的任务都被放入了优先任务队列，按优先级进行了重新排列执行，且线程池的线程数一直为corePoolSize，也就是只有一个。
 * 通过运行的代码我们可以看出PriorityBlockingQueue它其实是一个特殊的无界队列，它其中无论添加了多少个任务，线程池创建的线程数也不会超过corePoolSize的数量，只不过其他队列一般是按照先进先出的规则处理任务，而PriorityBlockingQueue队列可以自定义规则根据任务的优先级顺序先后执行。
 */
public class PriorityBlockingQueueTest {
    private static ExecutorService pool;

    public static void main(String[] args) {
        //优先任务队列
        pool = new ThreadPoolExecutor(1, 2, 1000, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<Runnable>(), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

        for (int i = 0; i < 20; i++) {
            pool.execute(new ThreadTask(i));
        }
    }
}

class ThreadTask implements Runnable, Comparable<ThreadTask> {

    private int priority;

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public ThreadTask() {

    }

    public ThreadTask(int priority) {
        this.priority = priority;
    }

    //当前对象和其他对象做比较，当前优先级大就返回-1，优先级小就返回1,值越小优先级越高
    @Override
    public int compareTo(ThreadTask o) {
        return this.priority > o.priority ? -1 : 1;
    }

    @Override
    public void run() {
        try {
            //让线程阻塞，使后续任务进入缓存队列
            Thread.sleep(1000);
            System.out.println("priority:" + this.priority + ",ThreadName:" + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}