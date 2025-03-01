package com.demo.transaction.management.controller;


import com.demo.transaction.management.entity.Transaction;
import com.demo.transaction.management.response.Response;
import com.demo.transaction.management.response.ResponseFactory;
import com.demo.transaction.management.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    /**
     * 交易列表分页查询
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping
    public Response<Page<Transaction>> getAllTransactions(
            @RequestParam(value = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("id").descending());
        return ResponseFactory.success(transactionService.getAllTransactions(pageable));
    }

    /**
     * 新增交易
     * @param transaction
     * @return
     */
    @PostMapping
    public Response<Transaction> createTransaction(@Valid @RequestBody Transaction transaction) {
        return ResponseFactory.success(transactionService.createTransaction(transaction));
    }

    /**
     * 获取交易详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Response<Transaction> getTransaction(@PathVariable(value = "id") Long id) {
        return ResponseFactory.success(transactionService.getTransactionById(id));
    }

    /**
     * 更新交易
     * @param transaction
     * @return
     */
    @PutMapping("/{id}")
    public Response<Transaction> updateTransaction(@Valid @RequestBody Transaction transaction) {
        return ResponseFactory.success(transactionService.updateTransaction(transaction));
    }

    /**
     * 删除交易
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Response deleteTransaction(@PathVariable(value = "id") Long id) {
        transactionService.deleteTransaction(id);
        return ResponseFactory.success();
    }
}
