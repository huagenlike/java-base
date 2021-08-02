package com.mzl.jvm.chap3;

/**
 * @author lihuagen
 * @version 1.0
 * @className: MajorGCTest1
 * @description: TODO
 * @date 2021/7/30 14:28
 */
public class MajorGCTest1 {
    private static final int _1MB = 1024 * 1024;

    /*** VM参数：-verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:Survivor-Ratio=8 -XX:MaxTenuringThreshold=1 -XX:+PrintTenuringDistribution */
    @SuppressWarnings("unused")
    public static void testTenuringThreshold() {
        byte[] allocation1, allocation2, allocation3;
        allocation1 = new byte[_1MB / 4];
        // 什么时候进入老年代决定于XX:MaxTenuring- Threshold设置
        allocation2 = new byte[4 * _1MB];
        allocation3 = new byte[4 * _1MB];
        allocation3 = null;
        allocation3 = new byte[4 * _1MB];
    }

    public static void main(String[] args) {
        testTenuringThreshold();
    }
}
