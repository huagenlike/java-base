package com.mzl.distributedLock.service.impl;

import com.mzl.distributedLock.service.OrderService;
import com.mzl.distributedLock.service.ResourceLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ResourceLockService resourceLockService;

    @Override
    public void addOrder() {
        String methodName = "";
        String status = "";
        Integer time = 60;
        try{
            //加锁
            if(resourceLockService.lock(methodName,time)){
                // status = process();//你的业务逻辑处理。
            }
        } finally{
            //释放锁
            resourceLockService.unlock(methodName, status);
        }
    }

}
