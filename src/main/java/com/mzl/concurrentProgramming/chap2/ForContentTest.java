package com.mzl.concurrentProgramming.chap2;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ForContentTest
 * @description: 单线程下多个变量被放入同一个缓存对性能有影响吗
 * @date 2021/7/2 16:07
 */
public class ForContentTest {

    // 通过字节填充的方式来避免该问题，也就是创建一个变量时使用填充字段填充该变量所在行的缓存行，
    public final static class FilledLong {
        public volatile long value = 0L;
        public long p1, p2, p3, p4, p5, p6;
    }

    // java8提供的注解，用来解决伪共享问题
    @sun.misc.Contended
    public final static class FilledLong1 {
        public volatile long value = 0L;
    }

    static final int LINE_NUM = 1024;
    static final int COLUM_NUM = 1024;

    public static void main(String[] args) {
        demo1();
        demo2();
    }

    // 4毫秒左右
    // demo1 比 demo2 快，这是因为数组内数组元素的内存地址是连续的，当访问第一个元素时，会把第一个元素后的若干那个元素一块放入缓存行，这样顺序访问数组元素时会在缓存中直接命中
    // 因而就不会去主内存读取了，后续访问也是这样。
    public static void demo1() {
        long [][] array = new long[LINE_NUM][COLUM_NUM];

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < LINE_NUM; ++i) {
            for (int j = 0; j < COLUM_NUM; ++j) {
                array[i][j] = i * 2 + j;
            }
        }
        long endTime = System.currentTimeMillis();
        long cacheTime = endTime - startTime;
        System.out.println("cache time：" + cacheTime);
    }

    // 6毫秒左右
    // 跳跃式访问数组元素，不是顺序的，这破坏了程序访问的局部性原则，并且缓存是有容量控制的，当缓存满了时会根据一定淘汰算法替换缓存行，
    // 这会导致从内存置换过来的缓存行的元素还没等到被读取就被替换掉了
    public static void demo2() {
        long [][] array = new long[LINE_NUM][COLUM_NUM];

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < COLUM_NUM; ++i) {
            for (int j = 0; j < LINE_NUM; ++j) {
                array[j][i] = i * 2 + j;
            }
        }
        long endTime = System.currentTimeMillis();
        long cacheTime = endTime - startTime;
        System.err.println("cache time：" + cacheTime);
    }
}
