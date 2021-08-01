package com.mzl.concurrentProgramming.chap11;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ConcurrentHashMapTest1
 * @description: ConcurrentHashMap 模拟多用户同时进入直播间 map 信息的维护。
 * @date 2021/7/15 9:14
 * 在如上代码(2.1)中,使用 map. putIfAbsent方法添加新设备列表,如果 topic在map中不存在,则将 topic和对应设备列表放入map。
 * 要注意的是,这个判断和放入是原子性操作,放入后会返回mll如果 topic已经在map里面存在,则调用 putIfAbsent会返回 topic对应的设备列表,若发现返回的设备列表不为null则把新的设备列表添加到返回的设备列表里面,从而问题得到解决。
 */
public class ConcurrentHashMapTest1 {
    // 创建map，key为topic，value为设备列表
    static ConcurrentHashMap<String, List<String>> map = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        // 进入直播间topic1，线程one
        Thread threadOne = new Thread(() -> {
            List<String> list1 = new ArrayList<>(2);
            list1.add("device1");
            list1.add("device2");
            List<String> oldList = map.putIfAbsent("topic1", list1);
            if (oldList != null) {
                oldList.addAll(list1);
            }
            System.out.println(JSON.toJSONString(map));
        });

        // 进入直播间topic1，线程two
        Thread threadTwo = new Thread(() -> {
            List<String> list1 = new ArrayList<>(2);
            list1.add("device11");
            list1.add("device22");
            List<String> oldList = map.putIfAbsent("topic1", list1);
            if (oldList != null) {
                oldList.addAll(list1);
            }
            System.out.println(JSON.toJSONString(map));
        });

        // 进入直播间topic1，线程three
        Thread threadThree = new Thread(() -> {
            List<String> list1 = new ArrayList<>(2);
            list1.add("device111");
            list1.add("device222");
            List<String> oldList = map.putIfAbsent("topic2", list1);
            if (oldList != null) {
                oldList.addAll(list1);
            }
            System.out.println(JSON.toJSONString(map));
        });

        threadOne.start();
        threadTwo.start();
        threadThree.start();
    }
}
