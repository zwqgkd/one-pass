package com.ksyun.exam.controller;


import com.ksyun.exam.model.BatchPayRequest;
import com.ksyun.exam.model.QueryUserAmountResponse;
import com.ksyun.exam.model.TradeRequest;
import com.ksyun.exam.service.OnePassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
    public boolean batchPay(@RequestBody BatchPayRequest request) {
        String batchPayId = request.getBatchPayId();
        List<Long> uids = request.getUids();

        return onePassService.batchPay(batchPayId, uids);
    }

    @PostMapping("/userTrade")
    public boolean userTrade(@RequestBody TradeRequest request) {
        Long sourceUid = request.getSourceUid();
        Long targetUid = request.getTargetUid();
        BigDecimal amount = request.getAmount();

        return onePassService.userTrade(sourceUid, targetUid, amount);
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
