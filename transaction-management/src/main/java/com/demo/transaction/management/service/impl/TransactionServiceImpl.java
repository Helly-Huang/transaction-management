package com.demo.transaction.management.service.impl;

import com.demo.transaction.management.common.Constants;
import com.demo.transaction.management.config.TransactionFilter;
import com.demo.transaction.management.dao.TransactionRepository;
import com.demo.transaction.management.entity.Transaction;
import com.demo.transaction.management.exception.BusinessException;
import com.demo.transaction.management.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CacheManager cacheManager;

    @Override
    @Cacheable(value = Constants.CACHE_NAME_TRANSACTIONS, key = "#p0.pageNumber + '_' + #p0.pageSize", unless = "#result == null")
    public Page<Transaction> getAllTransactions(Pageable pageable) {
        return transactionRepository.findAll(pageable);
    }

    @Override
    @CacheEvict(value = Constants.CACHE_NAME_TRANSACTIONS, allEntries = true)
    public Transaction createTransaction(Transaction transaction) {

        if (transactionRepository.existsByTypeAndAmountAndCurrencyAndCategory(transaction.getType(),
                transaction.getAmount(), transaction.getCurrency(), transaction.getCategory())) {
            throw new BusinessException("交易已存在，请勿重复添加");
        }

        transaction.setCreateTime(LocalDateTime.now());
        Transaction transactionSaved = transactionRepository.save(transaction);
        // 更新BloomFilter
        TransactionFilter.put(transactionSaved.getId());
        return transactionSaved;
    }

    @Override
    public Transaction getTransactionById(Long id) {
        // BloomFilter检查
        if (!TransactionFilter.mightContain(id)) {
            return null; // 确定不存在时直接返回
        }
        //尝试从缓存获取数据
        if (cacheContains(id)) {
            return getFromCache(id);
        }
        //缓存不存在，从数据库查询
        return transactionRepository.findById(id).orElse(null);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = Constants.CACHE_NAME_TRANSACTION, key = "#p0.id"),
            @CacheEvict(value = Constants.CACHE_NAME_TRANSACTIONS, allEntries = true)
    })
    public Transaction updateTransaction(Transaction transaction) {
        Long id = transaction.getId();
        Transaction existing = transactionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("交易不存在"));
        existing.setAmount(transaction.getAmount());
        existing.setCurrency(transaction.getCurrency());
        existing.setType(transaction.getType());
        existing.setCategory(transaction.getCategory());
        existing.setDescription(transaction.getDescription());
        existing.setStatus(transaction.getStatus());
        existing.setUpdateTime(LocalDateTime.now());
        return transactionRepository.save(existing);
    }


    /**
     * 删除交易，删除相关缓存；如果交易不存在返回提示
     *
     * @param id
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = Constants.CACHE_NAME_TRANSACTION, key = "#id"),
            @CacheEvict(value = Constants.CACHE_NAME_TRANSACTIONS, allEntries = true)
    })
    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new BusinessException("交易不存在");
        }
        transactionRepository.deleteById(id);
    }

    /**
     * 检查缓存是否包含指定 ID 的数据
     *
     * @param id
     * @return
     */
    private boolean cacheContains(Long id) {
        Cache cache = cacheManager.getCache(Constants.CACHE_NAME_TRANSACTION);
        if (cache == null) {
            // 记录日志，通知开发者缓存不存在
            log.warn("Cache '{}' is not available in the cache manager.", Constants.CACHE_NAME_TRANSACTION);
            return false;
        }
        // 使用 Optional 来处理可能的空值
        Optional<Transaction> optionalValue = Optional.ofNullable(cache.get(id, Transaction.class));
        return optionalValue.isPresent();
    }


    /**
     * 从缓存中获取数据
     *
     * @param id
     * @return
     */
    private Transaction getFromCache(Long id) {
        Cache cache = cacheManager.getCache(Constants.CACHE_NAME_TRANSACTION);
        return cache.get(id, Transaction.class);
    }
}
