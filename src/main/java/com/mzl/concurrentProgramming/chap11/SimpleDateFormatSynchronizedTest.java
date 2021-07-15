package com.mzl.concurrentProgramming.chap11;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author lihuagen
 * @version 1.0
 * @className: SimpleDateFormatTest1
 * @description: 针对 SimpleDateFormatTest 的解决方案（第二种方案）
 * @date 2021/7/15 9:43
 */
public class SimpleDateFormatSynchronizedTest {
    // 1 创建threadLocal实例
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        // 2 创建多个线程，并启动
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                try {
                    synchronized (sdf) {
                        System.out.println(sdf.parse("2021-07-15 09:48:56"));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                } finally {
                }
            });

            // 5 启动线程
            thread.start();
        }
    }
}
