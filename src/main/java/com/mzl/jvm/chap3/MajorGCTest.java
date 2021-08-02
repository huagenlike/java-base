package com.mzl.jvm.chap3;

/**
 * @author lihuagen
 * @version 1.0
 * @className: MajorGCTest
 * @description: 大对象直接进入老年代，老年代GC
 * @date 2021/7/30 11:07
 * Parallel Scavenge并不支持这个参数。如果必须使用此参数进行调优，可考虑 ParNew加CMS的收集器组合。
 */
public class MajorGCTest {

    private static final int _1MB = 1024 * 1024;

    public static void testPretenureSizeThreshold() {
        //直接分配在老年代中
        byte[] allocation;
        allocation = new byte[4 * _1MB];
    }

    public static void main(String[] args) {
        testPretenureSizeThreshold();
    }
}
