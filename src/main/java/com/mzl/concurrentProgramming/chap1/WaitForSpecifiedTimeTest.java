package com.mzl.concurrentProgramming.chap1;

/**
 * @program: java-base
 * @description:
 * @author: may
 * @create: 2021-06-05 21:54
 **/
public class WaitForSpecifiedTimeTest {
    private Object vitual = new Object();

    public void sleepFor(long mills)
    {
        synchronized(vitual)
        {
            long now = System.currentTimeMillis();
            System.out.println("be going to sleep");
            try {
                vitual.wait(mills);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("I've slept for "+(System.currentTimeMillis()-now)+" mills!");
        }
    }

    public void startAnotherThread()
    {
        Thread t = new Thread(){
            public void run()
            {
                try {
                    sleep(300);//睡一下的作用是确保让main线程先获得vitual的监视器锁.
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                synchronized(vitual)
                {
                    long now = System.currentTimeMillis();
                    System.out.println("I get the monitor");
                    try {
                        //睡一下的目的是推迟5秒释放vitual的锁，5秒的时间已经超过了wait()的等待时间，这样可以看看程序有什么异常行为。
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("I hold the monitor for "+(System.currentTimeMillis()-now)+" mills");
                }
            }
        };
        t.start();

    }

    public static void main(String []args)
    {
        WaitForSpecifiedTimeTest test = new WaitForSpecifiedTimeTest();
        test.startAnotherThread();
        test.sleepFor(3000);
    }
}
