package com.ksyun.exam.service;

import com.ksyun.exam.model.FundSystemResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class FundSystemService {

    private final RestTemplate restTemplate;

    private final RetryTemplate retryTemplate;

    private static final String SYSTEM_URL= "http://120.92.116.60/thirdpart/onePass";

    private static final String STUDENT_ID="50022";

    @Autowired
    public FundSystemService(RestTemplate restTemplate, RetryTemplate retryTemplate) {
        this.restTemplate = restTemplate;
        this.retryTemplate = retryTemplate;
    }

    //充值
    public FundSystemResponse pay(Long uid, BigDecimal amount) {
        //generate request id
        String requestId = UUID.randomUUID().toString();
        //generate transaction id
        String transactionId = UUID.randomUUID().toString();

        //set header
        HttpHeaders requestHeader=new HttpHeaders();
        requestHeader.add("X-KSY-KINGSTAR-ID",STUDENT_ID);
        requestHeader.add("X-KSY-REQUEST-ID",requestId);

        //set body
        Map<String, Object> requestBody=new HashMap<>();
        requestBody.put("transactionId",transactionId);
        requestBody.put("uid",uid);
        requestBody.put("amount",amount);

        //create request
        HttpEntity<Map<String,Object>> requestEntity=new HttpEntity<>(requestBody,requestHeader);
        log.debug("pay request:{}",requestEntity);

        //send request
        FundSystemResponse response=retryTemplate.execute(new RetryCallback<FundSystemResponse, RuntimeException>() {
            @Override
            public FundSystemResponse doWithRetry(RetryContext retryContext) throws RuntimeException {
                try {
                    ResponseEntity<FundSystemResponse> responseEntity = restTemplate.exchange(SYSTEM_URL+"/pay", HttpMethod.POST,requestEntity,FundSystemResponse.class);

                    return responseEntity.getBody();
                }catch(RestClientException e){
                    log.error("pay request failed",e);
                    throw new RuntimeException("pay request failed");
                }
            }
        });
        log.debug("pay response:{}",response.toString());

        return response;
    }

    public FundSystemResponse batchPayFinish(String batchPayId){
        //generate request id
        String requestId = UUID.randomUUID().toString();

        //set header
        HttpHeaders requestHeader=new HttpHeaders();
        requestHeader.add("X-KSY-KINGSTAR-ID",STUDENT_ID);
        requestHeader.add("X-KSY-REQUEST-ID",requestId);

        //set body
        Map<String, Object> requestBody=new HashMap<>();
        requestBody.put("batchPayId",batchPayId);

        //create request
        HttpEntity<Map<String,Object>> requestEntity=new HttpEntity<>(requestBody,requestHeader);
        log.debug("batchPayFinish request:{}",requestEntity);

        //send request
        ResponseEntity<FundSystemResponse> response=restTemplate.exchange(SYSTEM_URL+"/batchPayFinish", HttpMethod.POST,requestEntity,FundSystemResponse.class);
        log.debug("batchPayFinish response:{}",response.getBody().toString());

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            // 处理非2xx响应代码
            throw new RuntimeException("Request failed with status: " + response.getStatusCode());
        }
    }
}
