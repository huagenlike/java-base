package com.mzl.concurrentProgramming.chap11;

import java.util.*;

/**
 * @author lihuagen
 * @version 1.0
 * @className: AppTest
 * @description: 手淘app示例 （对需要复用但是会被下游修改的参数要进行深复制）
 * @date 2021/7/15 10:11
 * 每个消息对应一个 DataId ，其用来唯一标识一个消息。在每个发送消息的实现里面都会添加一个前缀以用于分类统计。
 * 代码（2）和代码（3）则是给对应的 appKey 新增设备列表。
 * 代码（4）创建消息体。
 * 代码（5）实现根据不appKey 使用不同的发送策略进行消息发送。
 * 运行上面代码，我们期望的输出结果为
 *  TwoService abc device d2
 *  eServ ce abc [ ” device idl ” ]
 * 但是实际结果却是
 *  TwoService abc ["device id2 ” ]
 *  oneService TwoService abc [”device idl ” ]
 * 问题产生了。这个例子运行的结果是固定的，但是如果在每个发送消息的 sendMsg方法里面异步修改消息的 DataId，那么运行的结果就不是固定的了。
 */
public class AppTest {
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
        while (appKeyItr.hasNext()) {
            Integer appKey = appKeyItr.next();
            // 这里根据appKey获取自己的消息列表
            StrategyService strategyService = serviceMap.get(appKey);
            if (null != strategyService) {
                strategyService.sendMsg(msgList, appKeyMap.get(appKey));
            } else {
                System.out.println(String.format("appKey:%s, is not registered service", appKey));
            }
        }
    }

}
