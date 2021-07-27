package com.mzl.jvm.chap3;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ReferenceCountingGC
 * @description: 引用计数算法的缺陷
 * @date 2021/7/27 14:30
 * jvm：-XX:+PrintGCDetails
 * 对象objA和objB都有字段instance，赋值令 objA.instance=objB及objB.instance=objA，除此之外，这两个对象再无任何引用，
 * 实际上这两个对象已经不可能再被访问，但是它们因为互相引用着对方，导致它们的引用计数都不为零，引用计数算法也就无法回收它们。
 *
 * Java虚拟机并不是通过引用计数算法来判断对象是否存活的
 */
public class ReferenceCountingGC {
    public Object instance = null;
    private static final int _1MB = 1024 * 1024;
    /**
     * 这个成员属性的唯一意义就是占点内存，以便能在GC日志中看清楚是否有回收过
     */
    private byte[] bigSize = new byte[2 * _1MB];

    public static void testGC() {
        ReferenceCountingGC objA = new ReferenceCountingGC();
        ReferenceCountingGC objB = new ReferenceCountingGC();
        objA.instance = objB;
        objB.instance = objA;
        objA = null;
        objB = null;
        // 假设在这行发生GC，objA和objB是否能被回收？
        System.gc();
    }

    public static void main(String[] args) {
        testGC();
    }
}
