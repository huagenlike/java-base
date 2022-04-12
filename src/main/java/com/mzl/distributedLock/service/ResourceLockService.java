package com.mzl.distributedLock.service;

import com.mzl.distributedLock.pojo.ResourceLock;

public interface ResourceLockService {

    /**
     * 保存
     * @param resourceLock
     * @return
     */
    int saveResourceLock(ResourceLock resourceLock);

    /**
     * 根据方法名查询数据
     * @param methodName
     * @return
     */
    ResourceLock getByMethodName(String methodName);

    /**
     * 加锁
     * @param methodName
     * @param time
     * @return
     */
    boolean lock(String methodName, int time);

    /**
     * 解锁
     * @param methodName
     * @param status
     */
    void unlock(String methodName, String status);
}
