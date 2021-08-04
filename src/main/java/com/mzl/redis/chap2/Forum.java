package com.mzl.redis.chap2;

import redis.clients.jedis.Jedis;

import java.util.HashMap;

/**
 * @program: java-base
 * @description: 论坛
 * @author: may
 * @create: 2021-08-04 21:37
 **/
public class Forum {

    public static void main(String[] args) throws InterruptedException {
        new Forum().run();
    }

    public void run() throws InterruptedException {
        Jedis conn = new Jedis("localhost");
        conn.select(0);
        articlePublishing(conn);
    }
    /**
     * 文章发表
     */
    public static void articlePublishing(Jedis conn) {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", "1");
        map.put("author", "小白");
        map.put("author", "第一篇日志");
        map.put("author", "2021-08-04");
        map.put("author", "今天是星期三......");
        Boolean isExists = conn.hexists("article." + map.get("id"), "id");
        if (!isExists) {
            conn.hmset("article." + map.get("id"), map);
        }
        System.out.println(isExists);
        isExists = conn.hexists("article." + map.get("id"), "id");
        System.out.println(isExists);
    }
}
