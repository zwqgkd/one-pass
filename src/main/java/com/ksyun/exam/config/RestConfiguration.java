package com.ksyun.exam.config;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestConfiguration {

    @Bean
    public RestTemplate restTemplate(OkHttpClient okHttpClient) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new OkHttp3ClientHttpRequestFactory(okHttpClient));
        return restTemplate;
    }

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        // 配置重试策略
        RetryPolicy retryPolicy = new SimpleRetryPolicy(3); // 最大重试次数为3
        retryTemplate.setRetryPolicy(retryPolicy);

        // 配置退避策略
        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(1000); // 每次重试的间隔时间为1000毫秒
        retryTemplate.setBackOffPolicy(backOffPolicy);

        return retryTemplate;
    }

    // 注入一个单例的OkHttpClient，可以设置大量的属性
    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool())
                .connectTimeout(Duration.ofSeconds(3))
                .readTimeout(Duration.ofSeconds(25))
                .writeTimeout(Duration.ofSeconds(3))
                .retryOnConnectionFailure(true) // 连接超时重试一次
                .build();
    }

}