package com.ksyun.exam.controller;

import com.ksyun.exam.model.BatchPayRequest;
import com.ksyun.exam.model.TradeRequest;
import com.ksyun.exam.service.FundSystemService;
import com.ksyun.exam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RestController
@RequestMapping("/onePass")
public class OnePassController {

    @Autowired
    private UserService userService;

    @Autowired
    private FundSystemService fundSystemService;

    private static final String MIN_AMOUNT = "0.01";

    private static final String MAX_AMOUNT = "10000";

    @PostMapping("/batchPay")
    public String batchPay(@RequestBody BatchPayRequest request) {
        String batchPayId = request.getBatchPayId();
        List<Long> uids = request.getUids();

        // todo: 实现业务逻辑
        BigDecimal low=new BigDecimal(MIN_AMOUNT);
        BigDecimal high=new BigDecimal(MAX_AMOUNT);
        BigDecimal precision = new BigDecimal("0.01"); // 两位小数
        for(Long uid : uids) {
            while(high.subtract(low).compareTo(precision) > 0) {
                BigDecimal mid = low.add(high).divide(new BigDecimal(2), RoundingMode.HALF_UP);

                int code = fundSystemService.pay(uid, mid).getCode();
                if(code==200){
                    low=mid;
                }
            }
        }

        return "batchPay";
    }

    @PostMapping("/userTrade")
    public String userTrade(@RequestBody TradeRequest request) {
        Long sourceUid = request.getSourceUid();
        Long targetUid = request.getTargetUid();
        BigDecimal amount = request.getAmount();
        return "userTrade";
    }

    @PostMapping("/queryUserAmount")
    public String queryUserAmount(@RequestBody List<Long> uids) {

        return "queryUserAmount";
    }
}
