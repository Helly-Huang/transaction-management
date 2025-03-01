package com.demo.transaction.management.service;

import com.demo.transaction.management.common.Constants;
import com.demo.transaction.management.common.TransactionStatus;
import com.demo.transaction.management.common.TransactionType;
import com.demo.transaction.management.config.TransactionFilter;
import com.demo.transaction.management.dao.TransactionRepository;
import com.demo.transaction.management.entity.Transaction;
import com.demo.transaction.management.exception.BusinessException;
import com.demo.transaction.management.service.impl.TransactionServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static com.demo.transaction.management.config.TransactionFilter.mightContain;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest(useMainMethod = SpringBootTest.UseMainMethod.ALWAYS)
public class TransactionServiceImplTest {
    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CacheManager cacheManager;


    @Mock
    private Cache cache;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(cacheManager.getCache(Constants.CACHE_NAME_TRANSACTIONS)).thenReturn(cache);
        when(cacheManager.getCache(Constants.CACHE_NAME_TRANSACTION)).thenReturn(cache);
    }

    @Test
    public void testFindAll() {
        Pageable pageable = PageRequest.of(0, 1);
        Transaction transaction = new Transaction(1L, new BigDecimal(10),
                "USD",
                TransactionType.DEPOSIT,
                "消费",
                "超市购物",
                TransactionStatus.COMPLETED,
                LocalDateTime.now(),
                null);
        Page<Transaction> page = new PageImpl<>(Collections.singletonList(transaction));
        when(transactionRepository.findAll(pageable)).thenReturn(page);

        Page<Transaction> result = transactionService.getAllTransactions(pageable);
        assertEquals(1, result.getTotalElements());
        verify(transactionRepository).findAll(pageable);
    }

    @Test
    public void testCreateTransaction() {
        Transaction transaction = new Transaction(null,
                new BigDecimal(10),
                "RMB",
                TransactionType.DEPOSIT,
                "消费",
                "超市购物",
                TransactionStatus.COMPLETED,
                LocalDateTime.now(),
                null);
        Transaction transactionSave = new Transaction(1L,
                new BigDecimal(10),
                "RMB",
                TransactionType.DEPOSIT,
                "消费",
                "超市购物",
                TransactionStatus.COMPLETED,
                LocalDateTime.now(),
                null);

        when(transactionRepository.existsByTypeAndAmountAndCurrencyAndCategory(transaction.getType(), transaction.getAmount(), transaction.getCurrency(),
                transaction.getCategory())).thenReturn(false);
        when(transactionRepository.save(transaction)).thenReturn(transactionSave);

        Transaction result = transactionService.createTransaction(transaction);
        assertEquals(transactionSave, result);
        verify(transactionRepository).save(transaction);
    }

    @Test(expected = BusinessException.class)
    public void testCreateTransactionExists() {
        Transaction transaction = new Transaction(null,
                new BigDecimal(10),
                "RMB",
                TransactionType.DEPOSIT,
                "消费",
                "超市购物",
                TransactionStatus.COMPLETED,
                LocalDateTime.now(),
                null);

        when(transactionRepository.existsByTypeAndAmountAndCurrencyAndCategory(transaction.getType(), transaction.getAmount(),
                transaction.getCurrency(), transaction.getCategory())).thenReturn(true);
        transactionService.createTransaction(transaction);
    }

    @Test
    public void testFindByIdNotInBloomFilter() {
        // 使用 try-with-resources 控制静态 Mock 范围
        try (MockedStatic<TransactionFilter> mockedFilter = mockStatic(TransactionFilter.class)) {
            Long id = 1L;

            // 配置静态方法行为
            mockedFilter.when(() -> mightContain(id)).thenReturn(false);

            // 执行测试
            Transaction result = transactionService.getTransactionById(id);

            // 验证结果
            mockedFilter.verify(() -> mightContain(id));
            assertEquals(null, result);
        }
    }

    @Test
    public void testFindByIdNotCache() {
        // 使用 try-with-resources 控制静态 Mock 范围
        try (MockedStatic<TransactionFilter> mockedFilter = mockStatic(TransactionFilter.class)) {
            Long id = 1L;

            // 配置静态方法行为
            mockedFilter.when(() -> mightContain(id)).thenReturn(true);

            when(mightContain(id)).thenReturn(true);
            Transaction transaction = new Transaction(1L,
                    new BigDecimal(10),
                    "RMB",
                    TransactionType.DEPOSIT,
                    "消费",
                    "超市购物",
                    TransactionStatus.COMPLETED,
                    LocalDateTime.now(),
                    null);

            when(transactionRepository.findById(id)).thenReturn(Optional.ofNullable(transaction));
            Transaction result = transactionService.getTransactionById(id);
            assertNotNull(result);
            assertEquals(transaction, result);
        }


    }


    @Test
    public void testFindByIdHaveCache() {
        // 使用 try-with-resources 控制静态 Mock 范围
        try (MockedStatic<TransactionFilter> mockedFilter = mockStatic(TransactionFilter.class)) {
            Long id = 1L;

            // 配置静态方法行为
            mockedFilter.when(() -> mightContain(id)).thenReturn(true);
            when(mightContain(id)).thenReturn(true);
            Transaction transaction = new Transaction(1L,
                    new BigDecimal(10),
                    "RMB",
                    TransactionType.DEPOSIT,
                    "消费",
                    "超市购物",
                    TransactionStatus.COMPLETED,
                    LocalDateTime.now(),
                    null);
            when(cache.get(id, Transaction.class)).thenReturn(transaction);
            Transaction result = transactionService.getTransactionById(id);
            assertNotNull(result);
            assertEquals(transaction, result);
        }
    }

    @Test
    public void testUpdateTransaction() {
        Long id = 1L;
        Transaction transaction = new Transaction(1L, new BigDecimal(10),
                "RMB",
                TransactionType.DEPOSIT,
                "消费",
                "超市购物",
                TransactionStatus.COMPLETED,
                LocalDateTime.now(),
                null);
        Transaction transactionExists = new Transaction(1L, new BigDecimal(10),
                "RMB",
                TransactionType.DEPOSIT,
                "出行",
                "火车",
                TransactionStatus.PENDING,
                LocalDateTime.now(),
                null);
        when(transactionRepository.findById(id)).thenReturn(Optional.of(transactionExists));
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        when(transactionRepository.save(any())).thenReturn(transaction);
        Transaction updateTransaction = transactionService.updateTransaction(transaction);

        assertNotNull(updateTransaction);
        assertEquals("消费", updateTransaction.getCategory());
        assertEquals("超市购物", updateTransaction.getDescription());
        assertEquals(TransactionStatus.COMPLETED, updateTransaction.getStatus());
        verify(transactionRepository).save(transactionExists);
    }

    @Test(expected = BusinessException.class)
    public void testUpdateTransactionNotFound() {
        Transaction transaction = new Transaction(1L, new BigDecimal(10),
                "RMB",
                TransactionType.DEPOSIT,
                "消费",
                "超市购物",
                TransactionStatus.COMPLETED,
                LocalDateTime.now(),
                null);
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());
        transactionService.updateTransaction(transaction);
    }

    @Test
    public void testDeleteTransaction() {
        Long id = 1L;
        when(transactionRepository.existsById(id)).thenReturn(true);

        transactionService.deleteTransaction(id);

        verify(transactionRepository).deleteById(id);

    }

    @Test(expected = BusinessException.class)
    public void testDeleteTransactionNotFound() {
        Long id = 1L;
        when(transactionRepository.existsById(id)).thenReturn(false);

        transactionService.deleteTransaction(id);
    }
}
