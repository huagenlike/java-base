package com.mzl.concurrentProgramming.chap11;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ConcurrentHashMapTest
 * @description: ConcurrentHashMap 模拟多用户同时进入直播间 map 信息的维护。（数据会出现丢失）
 * @date 2021/7/15 9:07
 * 可见,topic1房间中的用户会丢失一部分,这是因为put方法如果发现map里面存在这个key,则使用value覆盖该key对应的老的value值。
 * 而 putIfAbsent方法则是,如果发现已经存在该key则返回该key对应的 value,但并不进行覆盖,如果不存在则新增该key,并且判断和写入是原子性操作。
 * 使用 putIfAbsent替代pu方法后的代码如下。
 */
public class ConcurrentHashMapTest {
    // 创建map，key为topic，value为设备列表
    static ConcurrentHashMap<String, List<String>> map = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        // 进入直播间topic1，线程one
        Thread threadOne = new Thread(() -> {
            List<String> list1 = new ArrayList<>(2);
            list1.add("device1");
            list1.add("device2");
            map.put("topic1", list1);
            System.out.println(JSON.toJSONString(map));
        });

        // 进入直播间topic1，线程two
        Thread threadTwo = new Thread(() -> {
            List<String> list1 = new ArrayList<>(2);
            list1.add("device11");
            list1.add("device22");
            map.put("topic1", list1);
            System.out.println(JSON.toJSONString(map));
        });

        // 进入直播间topic1，线程three
        Thread threadThree = new Thread(() -> {
            List<String> list1 = new ArrayList<>(2);
            list1.add("device111");
            list1.add("device222");
            map.put("topic2", list1);
            System.out.println(JSON.toJSONString(map));
        });

        threadOne.start();
        threadTwo.start();
        threadThree.start();
    }
}
