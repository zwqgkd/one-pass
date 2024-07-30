package com.ksyun.exam.controller;

import com.ksyun.exam.mapper.module.UserRecord;
import com.ksyun.exam.model.BatchPayRequest;
import com.ksyun.exam.model.QueryUserAmountResponse;
import com.ksyun.exam.model.TradeRequest;
import com.ksyun.exam.service.FundSystemService;
import com.ksyun.exam.service.OnePassService;
import com.ksyun.exam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/onePass")
public class OnePassController {

    private final OnePassService onePassService;

    @Autowired
    public OnePassController(OnePassService onePassService) {
        this.onePassService = onePassService;
    }

    @PostMapping("/batchPay")
    public String batchPay(@RequestBody BatchPayRequest request) {
        String batchPayId = request.getBatchPayId();
        List<Long> uids = request.getUids();

        onePassService.batchPay(batchPayId, uids);

        return "batchPay";
    }

    @PostMapping("/userTrade")
    public String userTrade(@RequestBody TradeRequest request) {
        Long sourceUid = request.getSourceUid();
        Long targetUid = request.getTargetUid();
        BigDecimal amount = request.getAmount();

        onePassService.userTrade(sourceUid, targetUid, amount);

        return "userTrade";
    }

    @PostMapping("/queryUserAmount")
    public QueryUserAmountResponse queryUserAmount(@RequestBody List<Long> uids) {
        return new QueryUserAmountResponse(
                200,
                "ok",
                UUID.randomUUID().toString(),
                onePassService.queryUserAmount(uids)
        );
    }
}
