package com.mzl.concurrentProgramming.chap8;

import java.time.LocalDateTime;
import java.util.concurrent.*;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ThreadPoolExecutorTest
 * @description: Executors静态工厂创建几种常用线程池
 * @date 2021/7/12 17:28
 * 四种线程池
 * newCachedThreadPool：可缓存的线程池
 * newFixedThreadPool：定长线程池
 * newSingleThreadExecutor：单线程线程池
 * newScheduledThreadPool：支持定时的定长线程池
 * 线程池不允许使用Executors去创建，而是通过ThreadPoolExecutor的方式，这样的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险。 说明：Executors返回的线程池对象的弊端如下：
 * 1）FixedThreadPool和SingleThreadPool:
 * 允许的请求队列长度为Integer.MAX_VALUE，可能会堆积大量的请求，从而导致OOM。
 * 2）CachedThreadPool:
 * 允许的创建线程数量为Integer.MAX_VALUE，可能会创建大量的线程，从而导致OOM。
 */
public class ThreadPoolExecutorTest {

    public static void main(String[] args) {
//        newCachedThreadPoolTest();
//        newFixedThreadPoolTest();
//        newSingleThreadExecutorTest();
        newScheduledThreadPoolTest();
//        customThreadFactoryTest();
    }

    /**
     * newCachedThreadPool的方法中是返回一个ThreadPoolExecutor实例，从源码中可以看出该线程池的特点：
     * 1、该线程池的核心线程数量是0，线程的数量最高可以达到Integer 类型最大值；
     * 2、创建ThreadPoolExecutor实例时传过去的参数是一个SynchronousQueue实例，说明在创建任务时，若存在空闲线程就复用它，没有的话再新建线程。
     * 3、线程处于闲置状态超过60s的话，就会被销毁。
     * 上面的代码因为每次循环都是隔一秒执行，这个时间足够之前的线程工作完毕，并在新循环中复用这个线程，程序的运行结果如下：
     **/
    public static void newCachedThreadPoolTest() {
        //定义ExecutorService实例
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        // 自定义工厂
        /*cachedThreadPool = Executors.newCachedThreadPool(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                System.out.println("我是线程" + r);
                return t;
            }
        });*/
        for (int i = 0; i < 10; i++) {
            final int index = i;
            try {
                Thread.sleep(index * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //调用execute方法
            cachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread() + ":" + index);
                }
            });
        }
    }

    /**
     * 线程池特点：
     * 1、线程池的最大线程数等于核心线程数，并且线程池的线程不会因为闲置超时被销毁。
     * 2、使用的列队是LinkedBlockingQueue，表示如果当前线程数小于核心线程数，那么即使有空闲线程也不会复用线程去执行任务，而是创建新的线程去执行任务。如果当前执行任务数量大于核心线程数，此时再提交任务就在队列中等待，直到有可用线程。
     * 定义一个线程数为3的线程池，循环10次执行，可以发现运行的线程永远只有三个，结果如下：
     **/
    public static void newFixedThreadPoolTest() {
        ExecutorService cachedThreadPool = Executors.newFixedThreadPool(3);
        // 自定义工厂
        /*cachedThreadPool = Executors.newFixedThreadPool(3, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                System.out.println("我是线程" + r);
                return t;
            }
        });*/
        for (int i = 0; i < 10; i++) {
            final int index = i;
            try {
                Thread.sleep(index * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread() + ":" + index);
                }
            });
        }
    }

    /**
     * 和newFixedThreadPool类似，只是一直只有一个线程在工作
     **/
    public static void newSingleThreadExecutorTest() {
        ExecutorService cachedThreadPool = Executors.newSingleThreadExecutor();
        // 自定义ThreadFactory
        /*cachedThreadPool = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                System.out.println("我是线程" + r);
                return t;
            }
        });*/
        for (int i = 0; i < 10; i++) {
            final int index = i;
            try {
                Thread.sleep(index * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread() + ":" + index);
                }
            });
        }
    }

    /**
     * newScheduledThreadPool的方法不是直接返回一个ThreadPoolExecutor实例，而是通过有定时功能的ThreadPoolExecutor，也就是ScheduledThreadPoolExecutor 来返回ThreadPoolExecutor实例，从源码中可以看出：
     * 1、该线程池可以设置核心线程数量，最大线程数与newCachedThreadPool一样，都是Integer.MAX_VALUE。
     * 2、该线程池采用的队列是DelayedWorkQueue，具有延迟和定时的作用。
     **/
    public static void newScheduledThreadPoolTest() {
        ExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(3);
        // 自定义工厂
        /*scheduledThreadPool = Executors.newScheduledThreadPool(3, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                System.out.println("我是线程" + r);
                return t;
            }
        });*/
        //延迟3秒执行，只执行一次
        ((ScheduledExecutorService) scheduledThreadPool).schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("延迟========");
            }
        }, 3, TimeUnit.SECONDS);

        // 延迟1秒后每隔两秒执行一次
        // ScheduledExecutorService#scheduleAtFixedRate() 指的是“以固定的频率”执行，period（周期）指的是两次成功执行之间的时间
        // 比如，scheduleAtFixedRate(command, 1, 2, second)，第一次开始执行是1s后，假如执行耗时1s，那么下次开始执行是3s后，再下次开始执行是5s后
        ((ScheduledExecutorService) scheduledThreadPool).scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("以固定的频率执行============" + LocalDateTime.now().getSecond());
            }
        }, 1, 2, TimeUnit.SECONDS); //单位是秒

        // 而ScheduledExecutorService#scheduleWithFixedDelay()  指的是“以固定的延时”执行，delay（延时）指的是一次执行终止和下一次执行开始之间的延迟
        // 还是上例，scheduleWithFixedDelay(command, 1, 2, second)，第一次开始执行是1s后，假如执行耗时1s，执行完成时间是2s后，那么下次开始执行是4s后，再下次开始执行是11s后
        ((ScheduledExecutorService) scheduledThreadPool).scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                System.out.println("以固定的延时========" + LocalDateTime.now().getSecond());
            }
        }, 1, 2, TimeUnit.SECONDS);
    }

    /**
     * 自定义线程工厂
     * 默认情况下，ThreadPoolExecutor构造器传入的ThreadFactory 参数是Executors类中的defaultThreadFactory()，相当于一个线程工厂，帮我们创建了线程池中所需的线程。
     * 除此之外，我们也可以自定义ThreadFactory，并根据自己的需要来操作线程，下面是实例代码：
     **/
    public static void customThreadFactoryTest() {
        ExecutorService service = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>(),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread t = new Thread(r);
                        System.out.println("我是线程" + r);
                        return t;
                    }
                });
        //用lambda表达式编写方法体中的逻辑
        Runnable run = () -> {
            try {
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + "正在执行");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        for (int i = 0; i < 5; i++) {
            service.submit(run);
        }
        //这里一定要做关闭
        service.shutdown();
    }
}




