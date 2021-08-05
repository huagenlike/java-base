package com.mzl.redis.chap2;

import org.thymeleaf.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

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
//        articlePublishing(conn);
        accessFrequencyRestriction(conn);
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

    /**
     * 访问频率限制，10秒内只能访问10次，超过不允许
     */
    public static void accessFrequencyRestriction(Jedis conn) throws InterruptedException {
        conn.del("user:lisi");
        for (int i = 0; i < 110; i++) {
            Boolean isTrue = conn.exists("user:lisi");
            if (!isTrue) {
                conn.set("user:lisi", "1");
                conn.expire("user:lisi", 10);
            } else {
                Thread.sleep(500);
                String num = conn.get("user:lisi");
                if (StringUtils.isEmpty(num)) {
                    continue;
                }
                Long ttl = conn.ttl("user:lisi");
                if (Integer.valueOf(num) >= 10) {
                    System.out.println("访问频繁，稍后再试");
                } else {
                    conn.incr("user:lisi");
                }
                System.out.println("ttl:" + ttl);
            }
        }

    }

    public static void accessFrequencyRestrictionTransaction(Jedis conn) throws InterruptedException {
        conn.del("user:lisi");
        for (int i = 0; i < 110; i++) {
            Transaction transaction = conn.multi();
            Boolean isTrue = conn.exists("user:lisi");
            if (!isTrue) {
                conn.set("user:lisi", "1");
                conn.expire("user:lisi", 10);
            } else {
                Thread.sleep(500);
                String num = conn.get("user:lisi");
                if (StringUtils.isEmpty(num)) {
                    continue;
                }
                Long ttl = conn.ttl("user:lisi");
                if (Integer.valueOf(num) >= 10) {
                    System.out.println("访问频繁，稍后再试");
                } else {
                    conn.incr("user:lisi");
                }
                System.out.println("ttl:" + ttl);
            }
        }

    }
}
