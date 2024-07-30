package com.ksyun.exam.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;

import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching(proxyTargetClass = true)
public class CacheConfig {

    public static final String TEN_MINUTES_CACHE = "tenMinutesCache";
    public static final String META_DATA_CACHE = "metaDataCache";
    public static final String EAC_META_DATA_CACHE = "eacMetaDataCache";

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        CaffeineCache metaDataCache = buildBaseDataCache(META_DATA_CACHE);
        CaffeineCache tenMinutesCache = buildTenMinutesCache(TEN_MINUTES_CACHE);

        List<CaffeineCache> dataCacheList = new ArrayList<>();
        dataCacheList.add(metaDataCache);
        dataCacheList.add(tenMinutesCache);
        simpleCacheManager.setCaches(dataCacheList);

        simpleCacheManager.afterPropertiesSet();

        CompositeCacheManager compositeCacheManager = new CompositeCacheManager(simpleCacheManager);
        //都找不到相应的cache时，不返回null，而是返回Spring内置的NOPCache
        compositeCacheManager.setFallbackToNoOpCache(true);
        return compositeCacheManager;
    }

    private CaffeineCache buildBaseDataCache(String name) {
        return new CaffeineCache(name, Caffeine.newBuilder()
                //写入2小时后过期
                .expireAfterWrite(2L, TimeUnit.HOURS)
                .initialCapacity(200)
                .maximumSize(1500L)
                .build());
    }

    private CaffeineCache buildTenMinutesCache(String name) {
        return new CaffeineCache(name, Caffeine.newBuilder()
                //写入10分钟后过期
                .expireAfterWrite(10L, TimeUnit.MINUTES)
                .initialCapacity(100)
                .maximumSize(1000L)
                .build());
    }

}