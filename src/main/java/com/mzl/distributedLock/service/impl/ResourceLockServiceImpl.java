package com.mzl.distributedLock.service.impl;

import com.mzl.distributedLock.pojo.ResourceLock;
import com.mzl.distributedLock.service.ResourceLockService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ResourceLockServiceImpl implements ResourceLockService {

    /**
     * 保存
     * @param resourceLock
     * @return
     */
    @Override
    public int saveResourceLock(ResourceLock resourceLock) {
        return 0;
    }

    /**
     * 根据方法名查询数据
     * @param methodName
     * @return
     */
    @Override
    public ResourceLock getByMethodName(String methodName) {
        return null;
    }

    @Override
    public boolean lock(String methodName, int time) {
        ResourceLock resourceLock = this.getByMethodName(methodName);
        try {
            if (resourceLock == null) {
                //插入锁的数据
                resourceLock = new ResourceLock();
                resourceLock.setMethodTime(time);
                //上锁
                resourceLock.setLockFlag(1);
                //处理中
                resourceLock.setStatus("P");
                resourceLock.setBeginTime(new Date());

                int count = this.saveResourceLock(resourceLock);
                if (count == 1) {
                    //获取锁成功
                    return true;
                }
                return false;
            }
        } catch (Exception x) {
            return false;
        }

        //没上锁并且锁已经超时，即可以获取锁成功
        if (resourceLock.getLockFlag() == 0 && "S".equals(resourceLock.getStatus())
                && new Date().compareTo(resourceLock.getBeginTime()) > 1) {
            //上锁
            resourceLock.setLockFlag(1);
            //处理中
            resourceLock.setStatus("P");
            resourceLock.setBeginTime(new Date());
            //update resourceLock;
            return true;
        } else if (new Date().compareTo(resourceLock.getBeginTime()) >= 1) {
            //超时未正常执行结束,获取锁失败
            return false;
        } else {
            return false;
        }
    }

    /**
     * 解锁
     * @param methodName
     * @param status
     */
    @Override
    public void unlock(String methodName, String status) {
        ResourceLock resourceLock = new ResourceLock();
        //解锁
        resourceLock.setLockFlag(0);
        // S:表示成功，F表示失败
        resourceLock.setStatus(status);
        //update resourceLock;
        return ;
    }
}
