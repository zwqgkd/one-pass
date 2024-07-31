package com.ksyun.exam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;


@Service
public class RedisService {

    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public RedisService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void setValue(Long key, BigDecimal value) {
        stringRedisTemplate.opsForValue().set(key.toString(), value.toString());
    }

    public BigDecimal getValue(Long key) {
        return new BigDecimal(Objects.requireNonNull(stringRedisTemplate.opsForValue().get(key.toString())));
    }

    public boolean isExist(Long key) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key.toString()));
    }
}