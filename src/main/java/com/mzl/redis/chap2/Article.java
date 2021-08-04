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

}
