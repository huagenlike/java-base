package com.mzl.mysql.distributedLock.model.entity;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ResourceLock
 * @description: TODO
 * @date 2021/7/21 17:05
 */
@Data
public class ResourceLock {
    private Long id;
    private String resourceName;
    private String nodeInfo;
    private Integer count;
    private String desc;
    private Timestamp updateTime;
    private Timestamp createTime;
}
