package com.mzl.concurrentProgramming.mashibing;

import org.openjdk.jol.info.ClassLayout;

/**
 * @program: java-base
 * @description: 看到java对象内存布局
 * @author: may
 * @create: 2021-06-15 21:59
 **/
public class JustTest {

    public static void main(String[] args) {
        Object o = new Object();
        // 看下最简单的Object对象是怎么样的
        System.out.println(ClassLayout.parseInstance(o).toPrintable());
        synchronized (o) {
            // 所谓锁定o，指的是修改o的头，记录锁的信息
            System.out.println(ClassLayout.parseInstance(o).toPrintable());
        }
    }
}
