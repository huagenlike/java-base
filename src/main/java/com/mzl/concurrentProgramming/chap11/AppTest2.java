package com.mzl.concurrentProgramming.chap11;

import java.util.*;

/**
 * @author lihuagen
 * @version 1.0
 * @className: AppTest
 * @description: AppTest 的正确解决方案
 * @date 2021/7/15 10:11
 */
public class AppTest2 {
    // 1 不同appkey注册不同的服务
    static Map<Integer, StrategyService> serviceMap = new HashMap<>();
    static {
        serviceMap.put(111, new StrategyOneServiceImpl());
        serviceMap.put(222, new StrategyTwoServiceImpl());
    }

    public static void main(String[] args) {
        // 2 key为appkey，value为设备ID列表
        Map<Integer, List<String>> appKeyMap = new HashMap<>();

        // 3 创建appkey=111的设备列表
        List<String> oneList = new ArrayList<>();
        oneList.add("device_id1");
        appKeyMap.put(111, oneList);

        // 3 创建appkey=222的设备列表
        List<String> twoList = new ArrayList<>();
        twoList.add("device_id2");
        appKeyMap.put(222, twoList);

        List<String> threeList = new ArrayList<>();
        threeList.add("device_id3");
        appKeyMap.put(333, threeList);

        // 4 创建消息
        List<Msg> msgList = new ArrayList<>();
        Msg msg = new Msg();
        msg.setDataId("abc");
        msg.setBody("hello");
        msgList.add(msg);

        // 5 根据不同的appKey使用不同的策略进行处理
        Iterator<Integer> appKeyItr = appKeyMap.keySet().iterator();
        Map<Integer, List<Msg>> appKeyMsgMap = new HashMap<>();
        while (appKeyItr.hasNext()) {
            // 复制每个消息到临时消息列表
            List<Msg> tempList = new ArrayList<>();
            Iterator<Msg> itrMsg = msgList.iterator();
            while (itrMsg.hasNext()) {
                Msg temMsg = null;
                try {
                    // 使用BeanUtils.cloneBean对Msg对象进行属性复制
                    //temMsg = BeanUtils.cloneBean(itrMsg.next()); // 没有工具类，注释
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (null != temMsg) {
                    tempList.add(temMsg);
                }
            }
            // 存放当前appKey对应的经过深复制的消息列表
            appKeyMsgMap.put(appKeyItr.next(), tempList);
        }

        appKeyItr = appKeyMap.keySet().iterator();
        while (appKeyItr.hasNext()) {
            Integer appKey = appKeyItr.next();
            // 这里根据appKey获取自己的消息列表
            StrategyService strategyService = serviceMap.get(appKey);
            if (null != strategyService) {
                strategyService.sendMsg(appKeyMsgMap.get(appKey), appKeyMap.get(appKey));
            } else {
                System.out.println(String.format("appKey:%s, is not registered service", appKey));
            }

        }
    }

}
