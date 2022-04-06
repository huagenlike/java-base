package com.mzl.concurrentProgramming.chap11;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: java-base
 * @description:
 * @author: may
 * @create: 2021-07-14 20:57
 **/
public class TestMap {
    static ConcurrentHashMap<String, List<String>> map = new ConcurrentHashMap<>();
    public static void main(String[] args) {
        // 进入直播间topic1，线程one
        Thread threadOne = new Thread(() -> {
            List<String> list1 = new ArrayList<String>();
            list1.add("device1");
            list1.add("device2");
            map.put("topic1", list1);
            System.out.println(JSON.toJSONString(map));
        });

        // 进入直播间topic1，线程two
        Thread threadTwo = new Thread(() -> {
            List<String> list1 = new ArrayList<String>();
            list1.add("device1");
            list1.add("device2");
            map.put("topic1", list1);
            System.out.println(JSON.toJSONString(map));
        });

        // 进入直播间topic2，线程Three
        Thread threadThree = new Thread(() -> {
            List<String> list1 = new ArrayList<String>();
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
