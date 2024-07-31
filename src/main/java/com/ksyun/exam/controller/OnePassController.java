package com.ksyun.exam.controller;


import com.ksyun.exam.config.CacheConfig;
import com.ksyun.exam.config.LogAnnotation;
import com.ksyun.exam.model.BatchPayRequest;
import com.ksyun.exam.model.FundSystemResponse;
import com.ksyun.exam.model.QueryUserAmountResponse;
import com.ksyun.exam.model.TradeRequest;
import com.ksyun.exam.service.OnePassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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

    @Cacheable(value= CacheConfig.TEN_MINUTES_CACHE,key="#requestId")
    @LogAnnotation
    @PostMapping("/batchPay")
    public FundSystemResponse batchPay(@RequestHeader("X-KSY-REQUEST-ID") String requestId, @RequestBody BatchPayRequest request){
        String batchPayId = request.getBatchPayId();
        List<Long> uids = request.getUids();

        onePassService.batchPay(batchPayId, uids);
        return new FundSystemResponse(
                200,
                "ok",
                requestId,
                "null"
        );
    }

    @Cacheable(value= CacheConfig.TEN_MINUTES_CACHE,key="#requestId")
    @LogAnnotation
    @PostMapping("/userTrade")
    public FundSystemResponse userTrade(@RequestHeader("X-KSY-REQUEST-ID") String requestId, @RequestBody TradeRequest request) {
        Long sourceUid = request.getSourceUid();
        Long targetUid = request.getTargetUid();
        BigDecimal amount = request.getAmount();

        onePassService.userTrade(sourceUid, targetUid, amount);
        return new FundSystemResponse(
                200,
                "ok",
                requestId,
                "null"
        );
    }

    @Cacheable(value= CacheConfig.TEN_MINUTES_CACHE,key="#requestId")
    @LogAnnotation
    @PostMapping("/queryUserAmount")
    public QueryUserAmountResponse queryUserAmount(@RequestHeader("X-KSY-REQUEST-ID") String requestId, @RequestBody List<Long> uids) {
        return new QueryUserAmountResponse(
                200,
                "ok",
                requestId,
                onePassService.queryUserAmount(uids)
        );
    }
}
