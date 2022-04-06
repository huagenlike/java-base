package com.mzl.redis.chap2;

import lombok.Data;

/**
 * @program: java-base
 * @description: 文章
 * @author: may
 * @create: 2021-08-04 21:40
 **/
@Data
public class Article {
    private int id;
    private String title;
    private String author;
    private String time;
    private String content;

    public static void main(String[] args) {
        double money = test(2.03d, 20000d);
        System.out.println("2017年按照查到的最高利率：2.03%");
        System.out.println("国家最高利率=" + money);
        System.out.println("国家最高利率的4倍=" + money * 4);

    }

    private static double test(double a, double b) {
        return a / 100 * b;
    }

    private static double test1(double a, double b) {
        return 0d;
    }
}
