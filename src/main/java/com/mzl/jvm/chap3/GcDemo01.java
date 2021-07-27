package com.mzl.jvm.chap3;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lihuagen
 * @version 1.0
 * @className: GcDemo01
 * @description: 演示软引用
 * @date 2021/7/27 16:45
 * jvm：-Xms20m -Xmx20m -XX:+PrintGCDetails
 * 如下代码，堆内存只有20M ，需要读取5张 4M 的图片存入一个 list 中，分别演示强引用和软引用：
 */
public class GcDemo01 {
    private static final int _4MB = 4 * 1024 * 1024;

    public static void main(String[] args) throws IOException {
//        strong();
        soft();
    }

    /**
     * 使用强引用导致堆内存溢出 java.lang.OutOfMemoryError: Java heap space
     */
    public static void strong() {
        List<byte[]> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(new byte[_4MB]);
        }
    }

    /**
     * 使用软引用，当内存不足垃圾回收时list中个别软引用对象所引用的byte[]对象会被回收，所以ref.get()可能返回null
     */
    public static void soft() {
        // list --> SoftReference --> byte[]
        List<SoftReference<byte[]>> list = new ArrayList<>();
        // 在创建第5个byte[]的时候，内存不足，进行GC
        for (int i = 0; i < 5; i++) {
            SoftReference<byte[]> softRef = new SoftReference<>(new byte[_4MB]);
            // 读取对应的byte[]对象为null，表示对应的内容已经被GC了
            System.out.println(softRef.get());
            list.add(softRef);
            System.out.println(list.size());
        }
        System.out.println("循环结束：" + list.size());
        for (SoftReference<byte[]> ref : list) {
            System.out.println(ref.get());
        }
    }
}
