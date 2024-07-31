package com.ksyun.exam.service;

import com.ksyun.exam.mapper.module.UserRecord;
import com.ksyun.exam.model.FundSystemResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PreDestroy;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OnePassService {

    private final RedisService redisService;

    private final FundSystemService fundSystemService;

    private static final String MIN_PAY_AMOUNT = "0.01";

    private static final String MAX_PAY_AMOUNT = "10000";

    private final Lock lock = new ReentrantLock();

    ExecutorService executorService = Executors.newFixedThreadPool(10); // 使用固定大小线程池，可以根据实际情况调整线程数

    @Autowired
    public OnePassService(RedisService redisService, FundSystemService fundSystemService) {
        this.redisService = redisService;
        this.fundSystemService = fundSystemService;
    }

    @PreDestroy
    @SneakyThrows
    void shutdownExecutor() {
        executorService.shutdown();
        if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
        }
    }

    //    @Async
    public boolean batchPay(String batchPayId, List<Long> uids) {
        BigDecimal precision = new BigDecimal("0.01"); // 两位小数

        List<Callable<Boolean>> callableTasks = new ArrayList<>();
        for (Long uid : uids) {
            callableTasks.add(() -> {
                BigDecimal balanceAmount = new BigDecimal(0);
                BigDecimal curPayAmount = new BigDecimal(MAX_PAY_AMOUNT);
                boolean isInsert = true;
                while (curPayAmount.compareTo(precision) >= 0) {
                    FundSystemResponse response = fundSystemService.pay(uid, curPayAmount);
                    int code = response.getCode();
                    if (code == 200) { // 充值成功
                        balanceAmount = balanceAmount.add(curPayAmount);
                    } else if (code == 501) { // 余额不足
                        if (curPayAmount.equals(new BigDecimal(MIN_PAY_AMOUNT)))
                            break;
                        curPayAmount = curPayAmount.divide(new BigDecimal(2), 2, RoundingMode.HALF_UP);
                    } else if (code == 404) { // 用户不存在
                        log.error("pay error: user not exist");
                        isInsert = false;
                        break;
                    } else {
                        log.error("pay error: unknown code");
                    }
                }
                if (isInsert)
                    redisService.setValue(uid, balanceAmount);
                return isInsert;
            });
        }

        try {
            List<Future<Boolean>> futures = executorService.invokeAll(callableTasks);
            // 等待所有充值操作完成
            for (Future<Boolean> future : futures) {
                if (!future.get()) { // 如果某个任务返回false，说明有支付失败
                    log.error("Batch pay error for some users");
                    return false;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("Exception while waiting for payment tasks to complete", e);
            return false;
        }

        int finishCode = fundSystemService.batchPayFinish(batchPayId).getCode();
        if (finishCode != 200) {
            log.error("batchPayFinish error");
            return false;
        } else {
            log.info("batchPayFinish success");
            return true;
        }
    }

    @Transactional
    public boolean userTrade(Long sourceUid, Long targetUid, BigDecimal amount) {
        if (!redisService.isExist(sourceUid) || !redisService.isExist(targetUid)) {
            log.error("user not exist");
            return false;
        }
        //加锁
        lock.lock();
        try {
            BigDecimal sourcePayedAmount = redisService.getValue(sourceUid).subtract(amount);
            BigDecimal targetPayedAmount = redisService.getValue(targetUid).add(amount);
            if (sourcePayedAmount.compareTo(BigDecimal.ZERO) < 0) {
                log.error("balance not enough");
                return false;
            }
            redisService.setValue(sourceUid, sourcePayedAmount);
            redisService.setValue(targetUid, targetPayedAmount);
            return true;
        } finally {
            lock.unlock();
        }
    }

    public List<UserRecord> queryUserAmount(List<Long> uids) {
        List<UserRecord> users = new ArrayList<>();

        for (Long uid : uids) {
            if (redisService.isExist(uid)) {
                UserRecord user = new UserRecord(uid, redisService.getValue(uid));
                users.add(user);
            }
        }
        users.forEach(user -> log.debug("queryUserAmount:{}", user.getBalanceAmount()));
        return users;
    }
}
