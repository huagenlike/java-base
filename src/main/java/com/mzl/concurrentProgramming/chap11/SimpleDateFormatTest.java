package com.mzl.concurrentProgramming.chap11;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author lihuagen
 * @version 1.0
 * @className: SimpleDateFormatTest
 * @description: SimpleDateFormat 是线程不安全的
 * @date 2021/7/15 9:21
 * 代码(1)创建了 SimpleDateFormat 的一个实例,代码(2)创建10个线程,每个线程都共用同一个sdf对象对文本日期进行解析。
 * 多运行几次代码就会抛出 java.lang.NumberFormatException 异常,增加线程的个数有利于复现该问题
 * SimpleDateFormat类的 parse 方法内部主流程
 * 1 解析日期字符串，并将解析好的数据放入CalendarBuilder的实例calb中
 * 2 使用calb中解析好的日期数据设置calendar，这里调用establish(Calendar cal)方法，是CalendarBuilder类中的方法
 * 3 establish方法中，重置日期对象cal的属性值
 * 4 使用calb中的属性设置cal
 * 5 返回设置好的cal对象
 * 由于3、4、5不是原子性操作，所哟多个线程时会出现问题
 */
public class SimpleDateFormatTest {
    // 创建单例实例
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        // 创建多个线程，并启动
        for (int i = 0; i < 10; i++){
            Thread thread = new Thread(() -> {
                try {
                    // 使用单例日期实例解析文本
                    System.out.println(sdf.parse("2021-07-15 09:23:25"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });
            // 启动线程
            thread.start();
        }
    }
}
