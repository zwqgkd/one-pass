package com.ksyun.exam.service;

import com.ksyun.exam.mapper.module.UserRecord;
import com.ksyun.exam.model.BatchPayRequest;
import com.ksyun.exam.model.FundSystemResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class OnePassService {

    private final UserService userService;

    private final FundSystemService fundSystemService;

    private static final String MIN_PAY_AMOUNT = "0.01";

    private static final String MAX_PAY_AMOUNT = "10000";

    @Autowired
    public OnePassService(UserService userService, FundSystemService fundSystemService) {
        this.userService = userService;
        this.fundSystemService = fundSystemService;
    }

    public boolean batchPay(String batchPayId, List<Long> uids) {

        BigDecimal precision = new BigDecimal("0.01"); // 两位小数
        for(Long uid : uids) {
            BigDecimal balanceAmount = new BigDecimal(0);
            BigDecimal curPayAmount = new BigDecimal(MAX_PAY_AMOUNT);
            boolean isInsert = true;

            while(curPayAmount.compareTo(precision) >= 0) {
                FundSystemResponse response =fundSystemService.pay(uid,curPayAmount);
                int code=response.getCode();
                if(code==200){ //充值成功
                    balanceAmount=balanceAmount.add(curPayAmount);
                }else if(code==501){    //余额不足
                    if(curPayAmount.equals(new BigDecimal(MIN_PAY_AMOUNT)))
                        break;
                    curPayAmount=curPayAmount.divide(new BigDecimal(2),2, RoundingMode.HALF_UP);
                } else if(code==404){   //用户不存在
                    log.error("pay error: user not exist");
                    isInsert=false;
                    break;
                } else{
                    log.error("pay error: unknown code");
                }
            }

            //insert into db
            if(isInsert)
                userService.insertOne(uid,balanceAmount);
        }

        int finishCode= fundSystemService.batchPayFinish(batchPayId).getCode();
        if(finishCode!=200){
            log.error("batchPayFinish error");
            return false;
        }else{
            log.info("batchPayFinish success");
            return true;
        }

    }

//    @Async
    @Transactional
    public boolean userTrade(Long sourceUid, Long targetUid, BigDecimal amount) {
        // 从sourceUid账户扣款
        Optional<UserRecord> sourceUser=userService.selectOneById(sourceUid);
        Optional<UserRecord> targetUser=userService.selectOneById(targetUid);

        if(!sourceUser.isPresent()||!targetUser.isPresent()) {
            log.error("user not exist");
            return false;
        }

        BigDecimal sourcePayedAmount = sourceUser.get().getBalanceAmount().subtract(amount);
        if(sourcePayedAmount.compareTo(BigDecimal.ZERO)<0){
            log.error("balance not enough");
            return false;
        }
        userService.updateBalanceAmountById(sourceUid, sourcePayedAmount);
        userService.updateBalanceAmountById(targetUid, targetUser.get().getBalanceAmount().add(amount));

        return true;
    }

//    @Async
    public List<UserRecord> queryUserAmount(List<Long> uids){
        List<UserRecord> users = new ArrayList<>();

        for(Long uid:uids){
            Optional<UserRecord> user=userService.selectOneById(uid);
            user.ifPresent(users::add);
        }
        users.forEach(user->log.debug("queryUserAmount:{}",user.toString()));
        return users;
    }
}
