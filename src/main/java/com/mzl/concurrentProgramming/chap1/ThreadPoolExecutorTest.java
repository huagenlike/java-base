package com.mzl.concurrentProgramming.chap1;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ThreadPoolExecutorTest
 * @description: 线程池
 * @date 2021/6/23 11:10
 * ThreadPoolExecutor 继承了 AbstractExecutorService 实现了 ExecutorService 继承了 Executor。
 * 参数的含义：
 * corePoolSize：核心池的大小，这个参数跟后面讲述的线程池的实现原理有非常大的关系。在创建了线程池后，默认情况下，线程池中并没有任何线程，而是等待有任务到来才创建线程去执行任务，除非调用了prestartAllCoreThreads()或者prestartCoreThread()方法，从这2个方法的名字就可以看出，是预创建线程的意思，即在没有任务到来之前就创建corePoolSize个线程或者一个线程。默认情况下，在创建了线程池后，线程池中的线程数为0，当有任务来之后，就会创建一个线程去执行任务，当线程池中的线程数目达到corePoolSize后，就会把到达的任务放到缓存队列当中；
 * maximumPoolSize：线程池最大线程数，这个参数也是一个非常重要的参数，它表示在线程池中最多能创建多少个线程；
 * keepAliveTime：表示线程没有任务执行时最多保持多久时间会终止。默认情况下，只有当线程池中的线程数大于corePoolSize时，keepAliveTime才会起作用，直到线程池中的线程数不大于corePoolSize，即当线程池中的线程数大于corePoolSize时，如果一个线程空闲的时间达到keepAliveTime，则会终止，直到线程池中的线程数不超过corePoolSize。但是如果调用了allowCoreThreadTimeOut(boolean)方法，在线程池中的线程数不大于corePoolSize时，keepAliveTime参数也会起作用，直到线程池中的线程数为0；
 * unit：参数keepAliveTime的时间单位，有7种取值，在TimeUnit类中有7种静态属性：
 *  TimeUnit.DAYS;               //天
 *  TimeUnit.HOURS;             //小时
 *  TimeUnit.MINUTES;           //分钟
 *  TimeUnit.SECONDS;           //秒
 *  TimeUnit.MILLISECONDS;      //毫秒
 *  TimeUnit.MICROSECONDS;      //微妙
 *  TimeUnit.NANOSECONDS;       //纳秒
 * workQueue：一个阻塞队列，用来存储等待执行的任务，这个参数的选择也很重要，会对线程池的运行过程产生重大影响，一般来说，这里的阻塞队列有以下几种选择：
 *  ArrayBlockingQueue;
 *  LinkedBlockingQueue;
 *  SynchronousQueue;
 *  ArrayBlockingQueue和PriorityBlockingQueue使用较少，一般使用LinkedBlockingQueue和Synchronous。线程池的排队策略与BlockingQueue有关。
 * threadFactory：线程工厂，主要用来创建线程；
 * handler：表示当拒绝处理任务时的策略，有以下四种取值：
 *  ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException异常（默认策略）。
 *  ThreadPoolExecutor.DiscardPolicy：也是丢弃任务，但是不抛出异常。
 *  ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）
 *  ThreadPoolExecutor.CallerRunsPolicy：由调用线程处理该任务
 * 不过在java doc中，并不提倡我们直接使用ThreadPoolExecutor，而是使用Executors类中提供的几个静态方法来创建线程池：
 *  Executors.newCachedThreadPool();        //创建一个缓冲池，缓冲池容量大小为Integer.MAX_VALUE
 *      将corePoolSize设置为0，将maximumPoolSize设置为Integer.MAX_VALUE，使用的SynchronousQueue，也就是说来了任务就创建线程运行，当线程空闲超过60秒，就销毁线程。
 *  Executors.newSingleThreadExecutor();   //创建容量为1的缓冲池
 *      将corePoolSize和maximumPoolSize都设置为1，也使用的LinkedBlockingQueue；
 *  Executors.newFixedThreadPool(int);    //创建固定容量大小的缓冲池
 *      创建的线程池corePoolSize和maximumPoolSize值是相等的，它使用的LinkedBlockingQueue；
 *  另外，如果ThreadPoolExecutor达不到要求，可以自己继承ThreadPoolExecutor类进行重写。
 * 如何合理配置线程池的大小
 *  一般需要根据任务的类型来配置线程池大小：
 *  如果是CPU密集型任务，就需要尽量压榨CPU，参考值可以设为 NCPU+1
 *  如果是IO密集型任务，参考值可以设置为2*NCPU
 *  当然，这只是一个参考值，具体的设置还需要根据实际情况进行调整，比如可以先将线程池大小设置为参考值，再观察任务运行情况和系统负载、资源利用率来进行适当调整。
 */
public class ThreadPoolExecutorTest {
    // 当线程池中线程的数目大于5时，便将任务放入任务缓存队列里面，当任务缓存队列满了之后，便创建新的线程。如果上面程序中，将for循环中改成执行20个任务，就会抛出任务拒绝异常了。
    public static void main(String[] args) {
        // 创建线程池
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 200, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(5));

        for (int i = 0; i < 30; i++) {
            MyTask myTask = new MyTask(i);
            executor.execute(myTask);
            System.out.println("线程池中线程数目：" + executor.getPoolSize() + "，队列中等待执行的任务数目：" + executor.getQueue().size() + "，已执行玩别的任务数目：" + executor.getCompletedTaskCount());
        }
        executor.shutdown();
    }
}

class MyTask implements Runnable {
    private int taskNum;

    public MyTask(int num) {
        this.taskNum = num;
    }

    @Override
    public void run() {
        System.out.println("正在执行task" + taskNum);
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("task" + taskNum + "执行完毕");
    }

}
