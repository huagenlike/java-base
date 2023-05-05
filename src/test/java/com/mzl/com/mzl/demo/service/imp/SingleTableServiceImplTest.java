package com.mzl.com.mzl.demo.service.imp;

import com.mzl.demo.service.SingleTableService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SingleTableServiceImplTest {

    @Autowired
    private SingleTableService singleTableService;

    @Test
    void saveSingleTable() {
        singleTableService.saveSingleTable();
    }


}
