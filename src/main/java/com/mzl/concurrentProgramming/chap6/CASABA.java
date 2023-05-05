package com.mzl.concurrentProgramming.chap6;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author lihuagen
 * @version 1.0
 * @className: CASABA
 * @description: CAS ABA问题
 * @date 2021/6/23 10:13
 */
public class CASABA {
    // 普通的原子类，存在ABA问题
    AtomicInteger a1 = new AtomicInteger(10);
    // 带有时间戳的原子类，不存在ABA问题，第二个参数就是默认时间戳，这里指定为0
    AtomicStampedReference<Integer> a2 = new AtomicStampedReference<Integer>(10, 0);

    public static void main(String[] args) {
        CASABA a = new CASABA();
//        a.test();
        a.test11();
    }
    public void test10() {
        Double num = 69.26;
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(4);
        nf.setRoundingMode(RoundingMode.UP);
        String str = nf.format(num);
        System.out.println(str);
    }
    public void test11() {
        double d = 1;
        // #.00 表示两位小数
        DecimalFormat df = new DecimalFormat("#0.000"); // 保留两位小数，四舍五入
        System.out.println("方法二：" + df.format(d)); //0.35
    }

    public void test() {
        new Thread1().start();
        new Thread2().start();
        new Thread3().start();
        new Thread4().start();
    }

    // 模拟ABA操作
    class Thread1 extends Thread {
        @Override
        public void run() {
            // 会比较预期值是否当前值，如果不是，不执行操作
            a1.compareAndSet(11, 10);
            a1.compareAndSet(10, 11);
            a1.compareAndSet(11, 10);
        }
    }

    class Thread2 extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(200);  // 睡0.2秒，给线程1时间做ABA操作
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("AtomicInteger原子操作：" + a1.compareAndSet(10, 11));
        }
    }

    class Thread3 extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(500);  // 睡0.5秒，保证线程4先执行
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int stamp = a2.getStamp();
            a2.compareAndSet(10, 11, stamp, stamp + 1);
            stamp = a2.getStamp();
            a2.compareAndSet(11, 10, stamp, stamp + 1);
        }
    }

    // 可以看到使用AtomicStampedReference进行compareAndSet的时候，除了要验证数据，还要验证时间戳。
    // 如果数据一样，但是时间戳不一样，那么这个数据其实也被修改过了。
    class Thread4 extends Thread {
        @Override
        public void run() {
            int stamp = a2.getStamp();
            try {
                Thread.sleep(1000);  // 睡一秒，给线程3时间做ABA操作
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("AtomicStampedReference原子操作:" + a2.compareAndSet(10, 11, stamp, stamp + 1));
        }
    }
}
