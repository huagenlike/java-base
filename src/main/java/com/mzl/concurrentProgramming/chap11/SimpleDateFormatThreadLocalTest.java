package com.mzl.concurrentProgramming.chap11;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author lihuagen
 * @version 1.0
 * @className: SimpleDateFormatTest1
 * @description: 针对 SimpleDateFormatTest 的解决方案（第三种）
 * @date 2021/7/15 9:43
 * 代码(1)创建了一个线程安全的 SimpleDateFormat实例,代码(3)首先使用get()方法获取当前线程下 SimpleDateFormat的实例。
 * 在第一次调用 ThreadLocal的get()方法时会触发其 initialValue方法创建当前线程所需要的 SimpleDateFormat对象。
 * 另外需要注意的是,在代码(4)中,使用完线程变量后,要进行清理,以避免内存泄漏。
 */
public class SimpleDateFormatThreadLocalTest {
    // 1 创建threadLocal实例
    static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    public static void main(String[] args) {
        // 2 创建多个线程，并启动
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                try {
                    // 3 使用单例日期实例解析文本
                    System.out.println(threadLocal.get().parse("2021-07-15 09:48:56"));
                } catch (ParseException e) {
                    e.printStackTrace();
                } finally {
                    // 4 使用完毕记得清楚，避免内存泄漏
                    threadLocal.remove();
                }
            });

            // 5 启动线程
            thread.start();
        }
    }
}
