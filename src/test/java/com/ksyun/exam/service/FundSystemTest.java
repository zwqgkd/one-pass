package com.ksyun.exam.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
public class FundSystemTest {
    @Autowired
    private FundSystemService fundSystemService;

    @Test
    public void payTest(){
        fundSystemService.pay(600001L,new BigDecimal("10000"));
        fundSystemService.batchPayFinish("100");
    }

}
