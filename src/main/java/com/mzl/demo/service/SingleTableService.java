package com.mzl.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mzl.demo.pojo.SingleTable;

public interface SingleTableService extends IService<SingleTable> {

    void saveSingleTable();

}
