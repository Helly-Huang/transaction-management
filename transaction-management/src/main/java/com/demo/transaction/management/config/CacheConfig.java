package com.demo.transaction.management.config;

import com.demo.transaction.management.common.Constants;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        // 配置 transactions 缓存
        Caffeine<Object, Object> transactionsCacheBuilder = Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(10, TimeUnit.MINUTES);

        // 配置 transaction 缓存
        Caffeine<Object, Object> transactionCacheBuilder = Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(10, TimeUnit.MINUTES);

        // 创建缓存实例
        CaffeineCache transactionsCache = new CaffeineCache(Constants.CACHE_NAME_TRANSACTIONS, transactionsCacheBuilder.build());
        CaffeineCache transactionCache = new CaffeineCache(Constants.CACHE_NAME_TRANSACTION, transactionCacheBuilder.build());

        // 添加到缓存管理器
        cacheManager.setCaches(Arrays.asList(transactionsCache, transactionCache));

        return cacheManager;
    }
}