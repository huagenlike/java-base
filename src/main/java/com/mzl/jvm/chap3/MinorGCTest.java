package com.mzl.jvm.chap3;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author lihuagen
 * @version 1.0
 * @className: MinorGCTest
 * @description: 对象优先在Eden分配，年轻代 GC
 * @date 2021/7/30 10:30
 * VM参数：-verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8
 * PSYoungGen：GC日志中得PSYoungGen（PS是指Parallel Scavenge）为Eden+FromSpace，而整个YoungGeneration为Eden+FromSpace+ToSpace
 * PSYoungGen 其中 PS => Parallel Scavenge：并发、青年代、吞吐量优先，标记复制算法
 * ParOldGen => Parallel Old：并发、老年代、标记整理
 *
 * PSYoungGen：其中PS是Parallel Scavenge的简写，整个就表示新生代采用了Parallel Scavenge收集器。
 *  后面紧跟total参数：表示新生代使用内存9216k，只有9M是因为只计算了eden和from survivor，我们知道to survivor在jvm运行时是预留的，只有在回收的时候才会使用。刚刚设置新生代内存是10M、eden/survivor=8，刚刚验证了配置参数。
 * eden space 8192K, 33% used：eden区域总共8192k,使用了33%。2731/8192约等于0.33。
 *  from space 1024K, 0% used；
 *  to space 1024K, 0%used：因为还没有进行过回收所以两个survivor区域都是空的；
 * ParOldGen total 10240K, used 0K：Par是Parallel Old的简写，所以老年代采用的是Parallel Old收集器进行垃圾回收。
 * Metaspace used 3312K：元空间，因为用的是本地内存，所以没有total只有used。
 */
public class MinorGCTest {
    private static final int _1MB = 1024 * 1024;

    public static void testAllocation() {
        byte[] allocation1, allocation2, allocation3, allocation4;
        allocation1 = new byte[2 * _1MB];
        System.out.println("allocation1 " + getAddresses(allocation1));
        allocation2 = new byte[2 * _1MB];
        allocation3 = new byte[2 * _1MB];
        allocation4 = new byte[4 * _1MB]; // 出现一次Minor GC
    }

    // 执行testAllocation()中分配allocation4对象的语句时会发生一次Minor GC，这次回收的结果是新生代6753KB变为1000KB，而总内存占用量则几乎没有减少（因为allocation1、2、3三个对象都是存活的，虚拟机几乎没有找到可回收的对象）。
    // 产生这次垃圾收集的原因是为allocation4分配内存时，发现Eden已经被占用了6MB，剩余空间已不足以分配allocation4所需的4MB内存，因此发生Minor GC。垃圾收集期间虚拟机又发现已有的三个2MB大小的对象全部无法放入Survivor空间（Survivor空间只有 1MB大小），所以只好通过分配担保机制提前转移到老年代去。
    // 这次收集结束后，4MB的allocation4对象顺利分配在Eden中。因此程序执行完的结果是Eden占用 4MB（被allocation4占用），Survivor空闲，老年代被占用6MB（被allocation1、2、3占用）。通过GC日志可以证实这一点。
    public static void main(String[] args) {
        testAllocation();
    }

    public static void printAddressOf(Object... objects) {
        String addresses = getAddresses(objects);
        System.out.println(addresses);
    }

    public static String getAddresses(Object... objects) {
        StringBuffer sb = new StringBuffer();
        sb.append("0x");
        // sun.arch.data.model=32 // 32 bit JVM
        // sun.arch.data.model=64 // 64 bit JVM
        boolean is64bit = Integer.parseInt(System.getProperty("sun.arch.data.model")) == 32 ? false : true;
        Unsafe unsafe = getUnsafe();
        long last = 0;
        int offset = unsafe.arrayBaseOffset(objects.getClass());
        int scale = unsafe.arrayIndexScale(objects.getClass());
        switch (scale) {
            case 4:
                long factor = is64bit ? 8 : 1;
                final long i1 = (unsafe.getInt(objects, offset) & 0xFFFFFFFFL) * factor;
                // 输出指针地址
                sb.append(Long.toHexString(i1));
                last = i1;
                for (int i = 1; i < objects.length; i++) {
                    final long i2 = (unsafe.getInt(objects, offset + i * 4) & 0xFFFFFFFFL) * factor;
                    if (i2 > last) {
                        sb.append(", +" + Long.toHexString(i2 - last));
                    } else {
                        sb.append(", -" + Long.toHexString(last - i2));
                    }
                    last = i2;
                }
                break;
            case 8:
                throw new AssertionError("Not supported");
        }
        return sb.toString();
    }

    // 反射获取到Unsafe，不允许直接调用
    private static Unsafe getUnsafe() {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            return (Unsafe) theUnsafe.get(null);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }
}