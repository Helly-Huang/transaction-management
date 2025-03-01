package com.demo.transaction.management.dao;

import com.demo.transaction.management.entity.Transaction;
import com.demo.transaction.management.common.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    boolean existsByTypeAndAmountAndCurrencyAndCategory(TransactionType type, BigDecimal amount, String currency, String category);

}
