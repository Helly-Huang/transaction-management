package com.demo.transaction.management.config;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

public class TransactionFilter {

    // 预期插入100万条数据，误判率0.1%
    private static final BloomFilter<Long> bloomFilter = BloomFilter.create(
            Funnels.longFunnel(),
            1_000_000,
            0.001
    );

    public static boolean mightContain(Long transactionId) {
        return bloomFilter.mightContain(transactionId);
    }

    public static void put(Long transactionId) {
        bloomFilter.put(transactionId);
    }
}
