package com.mzl.concurrentProgramming.chap11;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * @author lihuagen
 * @version 1.0
 * @className: StrategyOneServiceImpl
 * @description: TODO
 * @date 2021/7/15 10:15
 */
public class StrategyOneServiceImpl implements StrategyService{

    @Override
    public void sendMsg(List<Msg> msgList, List<String> deviceIdList) {
        for (Msg msg : msgList) {
            msg.setDataId("oneService_" + msg.getDataId());
            System.out.println(msg.getDataId() + " " + JSON.toJSONString(deviceIdList));
        }
    }

}
