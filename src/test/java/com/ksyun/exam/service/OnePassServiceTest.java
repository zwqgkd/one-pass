package com.ksyun.exam.service;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;

@SpringBootTest
public class OnePassServiceTest {

    @Autowired
    private OnePassService onePassService;

    @Test
    public void testOnePass(){
        List<Long> uids = new ArrayList<>();
        uids.add(600001L);
        uids.add(600002L);
        //充值
        onePassService.batchPay("98312888830",uids);
        //查询
        onePassService.queryUserAmount(uids);
        //转账
        onePassService.userTrade(600001L,600002L,new BigDecimal("10"));
        //查询
        onePassService.queryUserAmount(uids);
    }
}
