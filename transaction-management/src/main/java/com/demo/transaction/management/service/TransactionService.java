package com.demo.transaction.management.service;

import com.demo.transaction.management.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {

    Page<Transaction> getAllTransactions(Pageable pageable);

    Transaction createTransaction(Transaction transaction);

    Transaction getTransactionById(Long id);

    Transaction updateTransaction(Transaction transaction);

    void deleteTransaction(Long transactionId);
}
