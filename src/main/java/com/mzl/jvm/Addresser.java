package com.mzl.jvm;

import java.lang.reflect.Field;
import sun.misc.Unsafe;

/**
 * @author lihuagen
 * @version 1.0
 * @className: Addresser
 * @description: TODO
 * @date 2021/7/30 11:43
 */
public class Addresser {
    private static Unsafe unsafe;

    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long addressOf(Object o) throws Exception {

        Object[] array = new Object[] { o };

        long baseOffset = unsafe.arrayBaseOffset(Object[].class);
        //arrayBaseOffset方法是一个本地方法，可以获取数组第一个元素的偏移地址
        int addressSize = unsafe.addressSize();
        long objectAddress;
        switch (addressSize) {
            case 4:
                objectAddress = unsafe.getInt(array, baseOffset);
                //getInt方法获取对象中offset偏移地址对应的int型field的值
                break;
            case 8:
                objectAddress = unsafe.getLong(array, baseOffset);
                //getLong方法获取对象中offset偏移地址对应的long型field的值
                break;
            default:
                throw new Error("unsupported address size: " + addressSize);
        }
        return (objectAddress);
    }

    public static void main(String... args) throws Exception {
        Object mine = "Hello world".toCharArray(); //先把字符串转化为数组对象
        long address = addressOf(mine);
        System.out.println("Addess: " + address);

        // Verify address works - should see the characters in the array in the output
        printBytes(address, 27);
    }

    public static void printBytes(long objectAddress, int num) {
        for (long i = 0; i < num; i++) {
            int cur = unsafe.getByte(objectAddress + i);
            System.out.print((char) cur);
        }
        System.out.println();
    }
}
