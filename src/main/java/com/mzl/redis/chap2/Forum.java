package com.mzl.redis.chap2;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
//        accessFrequencyRestriction(conn);
//        accessFrequencyRestriction1(conn);
//        accessFrequencyRestrictionTransaction(conn);
//        testTx(conn);
//        redisAchieveTaskQueue(conn);
        redisAchieveTaskQueueOptimization(conn);
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

    /**
     * 访问频率限制，10秒内只能访问10次，超过不允许（使用事务）
     */
    public static void accessFrequencyRestrictionTransaction(Jedis conn) throws InterruptedException {
        conn.del("user:lisi");
        for (int i = 0; i < 110; i++) {
            Boolean isTrue = conn.exists("user:lisi");
            if (!isTrue) {
                // 使用事务，确保set和设置过期时间的原子性
                Transaction transaction = conn.multi();
                transaction.set("user:lisi", "1");
                transaction.expire("user:lisi", 10);
                transaction.exec();
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

    /**
     * 访问频率限制，10秒内只能访问10次，超过不允许
     * 精确地保证每分钟最多访问10次，需要记录下用户每次访问的时间
     * 杜绝在同一分钟的最后一秒访问了9次，又在下一分钟的第一秒访问了10次，这样的访问是可以通过现在的访问频率限制的，但实际上该用户在2秒内访问了19次博客，这与每个用户每分钟只能访问10次的限制差距较大。
     */
    public static void accessFrequencyRestriction1(Jedis conn) throws InterruptedException {
        conn.del("user:lisi");
        for (int i = 0; i < 110; i++) {
            long startTime = System.currentTimeMillis() / 1000;
            Long llen = conn.llen("user:lisi");
            System.out.println(llen);
            if (llen < 10) {
                conn.lpush("user:lisi", startTime + "");
                System.out.println("存入redis");
            } else {
                String time = conn.lindex("user:lisi", -1);
                if (startTime - Long.valueOf(time).longValue() < 20) {
                    System.out.println("访问频率超过了限制，请稍后再试");
                } else {
                    conn.lpush("user:lisi", startTime + "");
                    // 只保留指定片段
                    conn.ltrim("user:lisi", 0 , 9);
                }
            }
            Thread.sleep(1000);
        }
    }

    /**
     * 测试事务
     */
    public void testTx(Jedis conn) throws InterruptedException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("hello","world");
        jsonObject.put("my","redis");
        conn.watch("age");

        Transaction multi = conn.multi();
        //multi.watch("age");
        multi.set("age","18");
        multi.set("phone","15010898888");
        multi.get("phone");
        multi.set("sm",jsonObject.toJSONString());
        multi.get("sm");
        Thread.sleep(5000);
        List exec = multi.exec();
        System.out.println(exec);
        if(null!=exec){
            exec.stream().forEach(System.out::println);
        }
    }
    
    /**
     * 使用Redis实现任务队列
     */
    public void redisAchieveTaskQueue(Jedis conn) {
        // 消费者
        // 存在问题：如果key没值，也会执行循环，浪费性能
        Thread threadA = new Thread(() -> {
            while (true) {
                String quque = conn.rpop("queue");
                if (StringUtils.isNotEmpty(quque)) {
                    System.out.println("value is ：" + quque);
                } else {
                    try {
                        System.out.println("休眠1秒钟");
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // 生产者
        Thread threadB = new Thread(() -> {
            List<String> strings = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k");

            for (int i = 0; i < strings.size(); i++) {
                conn.lpush("queue", strings.get(i));
                System.out.println("将值" + strings.get(i) + "存入key为 queue");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        threadA.start();
        threadB.start();
    }

    /**
     * 使用Redis实现任务队列（优化）
     */
    public void redisAchieveTaskQueueOptimization(Jedis conn) throws InterruptedException {
        // 消费者
        Thread threadA = new Thread(() -> {
            while (true) {
                // 如果任务队列中没有新任务，BRPOP 命令会一直阻塞，不会执行 execute()
                try {
                    List<String> strings = conn.brpop("queue","30");
                    for (String string : strings) {
                        System.out.println("移除值" + string);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // 生产者
        Thread threadB = new Thread(() -> {
            List<String> strings = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k");

            for (int i = 0; i < strings.size(); i++) {
                conn.lpush("queue", strings.get(i));
                System.out.println("将值" + strings.get(i) + "存入key为 queue");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        threadB.start();
        Thread.sleep(2000);
        threadA.start();
    }

}
