package com.mzl.concurrentProgramming.chap3;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author lihuagen
 * @version 1.0
 * @className: RandomTest
 * @description: TODO
 * @date 2021/7/5 9:11
 */
public class RandomTest {

    public static void main(String[] args) {
        demo1();
        System.out.println("-------------------");
//        demo2();
    }

    // Random的缺点是多个线程会使用同一个原子性变量，从而导致对原子变量更新的竞争
    public static void demo1() {
        long startTime = System.currentTimeMillis();
        // 创建一个默认种子的随机数生成器
        Random random = new Random();
        for (long i = 0; i < 100000000; i++) {
            // 输出10个在0-5（包含0，不包含5）之间的随机数
            random.nextInt(5);
//            System.out.println(random.nextInt(5));
        }
        System.out.println(System.currentTimeMillis() - startTime);
    }

    // ThreadLocalRandom通过让每一个线程复制一份变量，使得在每个线程对变量进行操作时实际时操作自己本地内存里的副本，从而避免了对共享变量进行同步。
    // ThreadLocalRandom调用nextInt方法时，实际上是获取当前线程的threadLocalRandomSeed变量作为当前种子来计算新的种子，然后更新新的种子到当前线程的threadLocalRandomSeed变量，然后再根据心中自并使用具体算法计算随机数。
    public static void demo2() {
        long startTime = System.currentTimeMillis();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (long i =0; i < 100000000; i++) {
            random.nextInt(5);
//            System.out.println(random.nextInt(5));
        }
        System.err.println(System.currentTimeMillis() - startTime);
    }
}
