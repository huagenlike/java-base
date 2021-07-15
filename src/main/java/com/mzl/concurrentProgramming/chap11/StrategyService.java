package com.mzl.concurrentProgramming.chap11;

import java.util.List;

public interface StrategyService {

    void sendMsg(List<Msg> msgList, List<String> deviceIdList);

}
