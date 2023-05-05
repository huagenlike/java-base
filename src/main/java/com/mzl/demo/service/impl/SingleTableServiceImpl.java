package com.mzl.demo.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzl.demo.mapper.SingleTableMapper;
import com.mzl.demo.pojo.SingleTable;
import com.mzl.demo.service.SingleTableService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class SingleTableServiceImpl extends ServiceImpl<SingleTableMapper, SingleTable> implements SingleTableService {

    @Override
    @Transactional
    public void saveSingleTable() {
        Random random = new Random();
        List<SingleTable> list = new ArrayList<>();
        for (int i = 15000; i < 50000; i++) {
            if (i != 0 && i % 500 == 0) {
                this.saveBatch(list);
                list.clear();
            }
            SingleTable singleTable = new SingleTable();
            singleTable.setKey1(random.nextInt(200000) + "");
            singleTable.setKey2(i);
            singleTable.setKey3(random.nextInt(50000) + "");
            singleTable.setKeyPart1(random.nextInt(100000) + "");
            singleTable.setKeyPart2(random.nextInt(40000) + "");
            singleTable.setKeyPart3(random.nextInt(500000) + "");
            singleTable.setCommonField("测试数据" + i);
            list.add(singleTable);
        }
        if (CollectionUtils.isNotEmpty(list)) {
            this.saveBatch(list);
        }
    }
}
