package com.mzl.concurrentProgramming.chap6;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ReentrantLockTryLock
 * @description: TODO
 * @date 2021/7/8 11:57
 */
public class ReentrantLockTryLock {
    Lock lock = new ReentrantLock();

    void m1() {
        lock.lock();
        try {
            for (int i = 0; i < 10; i++) {
                TimeUnit.SECONDS.sleep(1);
                System.out.println(i);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /*
	 使用tryLock进行尝试锁定，不管锁定与否，方法都将继续执行
	 可以根据tryLock的返回值来判定是否锁定
	 也可以指定tryLock的时间，由于tryLock(time)抛出异常，所以要注意unclock的处理，必须放到finally中
	*/

    void m2() {
		/*
		//第一种
		boolean locked = lock.tryLock();//尝试一下 拿到，返回值为true,否则为false
		System.out.println("m2 ..." + locked);
		if(locked) lock.unlock();
		*/
        //第二种
        boolean locked = false;
        try {
            locked = lock.tryLock(5, TimeUnit.SECONDS);
            System.out.println("m2 ..." + locked);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if(locked) {
                lock.unlock();
            }
        }

    }

    public static void main(String[] args) {
        ReentrantLockTryLock rl = new ReentrantLockTryLock();
        new Thread(rl::m1).start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(rl::m2).start();
    }
}
