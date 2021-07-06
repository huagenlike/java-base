package com.mzl.concurrentProgramming.chap6;

import java.util.concurrent.locks.LockSupport;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ParkTest
 * @description: TODO
 * @date 2021/7/6 17:33
 */
public class ParkTest {

    public void parkTest() {
        LockSupport.park();
        // 使用带 blocker 参数的park方法,线程堆栈可u一提供更多有关阻塞对象的信息
        // - parking to wait for  <0x00000006c205a1b0> (a com.mzl.concurrentProgramming.chap6.ParkTest)
//        LockSupport.park(this);
    }

    public static void main(String[] args) {
        ParkTest parkTest = new ParkTest();
        parkTest.parkTest();
    }
}
