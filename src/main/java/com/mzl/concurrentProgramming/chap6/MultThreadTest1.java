package com.mzl.concurrentProgramming.chap6;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lihuagen
 * @version 1.0
 * @className: MultThreadTest1
 * @description: TODO
 * @date 2021/7/8 11:39
 */
public class MultThreadTest1 extends Thread {
    private static final Logger log = LoggerFactory.getLogger(MultThreadTest1.class);
    private static ReentrantLock reentrantLock = new ReentrantLock();

    // 必须加上static 否则j只会改变一次
    private static int j = 1001;

    private Object object = new Object();

    @Override
    public void run() {
        reentrantLock.lock();
        try {
            if (j == 0) {
                log.info("线程已死：" + Thread.currentThread().getName() + "此时j=" + j);
            } else {
                j--;
                log.info("当前线程" + Thread.currentThread().getName() + "此时j=" + j);
            }
        } finally {
            reentrantLock.unlock();
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 1110; i++) {
            new MultThreadTest1().start();
        }
    }
}
