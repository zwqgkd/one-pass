package com.ksyun.exam.config;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
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