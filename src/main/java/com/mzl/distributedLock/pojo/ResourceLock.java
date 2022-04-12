package com.mzl.distributedLock.pojo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 分布式锁表
 */
@Data
public class ResourceLock {
    /**
     * 方法名称
     */
    private String methodName;
    /**
     * 状态：S:表示成功，F:表示失败,P:
     */
    private String status;
    /**
     * 锁定标识：1是已经锁 0是未锁
     */
    private Integer lockFlag;
    /**
     * 开始时间
     */
    private Date beginTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 抢到锁的ip
     */
    private String clientIp;
    /**
     * 生命周期：方法生命周期内只允许一个结点获取一次锁，单位：分钟
     */
    private Integer methodTime = 60;
}
