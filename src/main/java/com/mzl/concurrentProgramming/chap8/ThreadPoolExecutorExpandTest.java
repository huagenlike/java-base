package com.mzl.concurrentProgramming.chap8;

import java.util.concurrent.*;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ThreadPoolExecutorExpandTest
 * @description: ThreadPoolExecutor扩展
 * @date 2021/7/13 10:37
 * 1、beforeExecute：线程池中任务运行前执行
 * 2、afterExecute：线程池中任务运行完毕后执行
 * 3、terminated：线程池退出后执行
 */
public class ThreadPoolExecutorExpandTest {
    private static ExecutorService pool;

    public static void main(String[] args) throws InterruptedException {
        //实现自定义接口
        pool = new ThreadPoolExecutor(2, 4, 1000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(5),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        System.out.println("线程" + r.hashCode() + "创建");
                        //线程命名
                        Thread th = new Thread(r, "threadPool" + r.hashCode());
                        return th;
                    }
                }, new ThreadPoolExecutor.CallerRunsPolicy()) {

            @Override
            protected void beforeExecute(Thread t, Runnable r) {
                System.out.println("准备执行：" + ((ThreadTask2) r).getTaskName());
            }

            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                System.out.println("执行完毕：" + ((ThreadTask2) r).getTaskName());
            }

            @Override
            protected void terminated() {
                System.out.println("线程池退出");
            }
        };

        for (int i = 0; i < 10; i++) {
            pool.execute(new ThreadTask2("Task" + i));
        }
        // 使用shutdown方法可以比较安全的关闭线程池，当线程池调用该方法后，线程池中不再接受后续添加的任务。但是，此时线程池不会立刻退出，直到添加到线程池中的任务都已经处理完成，才会退出。
        pool.shutdown();
    }
}

class ThreadTask2 implements Runnable {
    private String taskName;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public ThreadTask2(String name) {
        this.setTaskName(name);
    }

    @Override
    public void run() {
        //输出执行线程的名称
        System.out.println("TaskName" + this.getTaskName() + "---ThreadName:" + Thread.currentThread().getName());
    }
}
