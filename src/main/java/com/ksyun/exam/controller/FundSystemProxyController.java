package com.ksyun.exam.controller;

import com.ksyun.exam.service.FundSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/fundSystem")
public class FundSystemProxyController {

    @Autowired
    private FundSystemService fundSystemService;

    @PostMapping("/pay")
    public String pay() {
        return "";
    }
}
