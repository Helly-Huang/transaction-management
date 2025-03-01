package com.demo.transaction.management.controller;


import com.demo.transaction.management.common.TransactionStatus;
import com.demo.transaction.management.common.TransactionType;
import com.demo.transaction.management.dao.TransactionRepository;
import com.demo.transaction.management.entity.Transaction;
import com.demo.transaction.management.response.ResponseFactory;
import com.demo.transaction.management.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private TransactionRepository transactionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Transaction transaction;

    @BeforeEach
    public void setUp() {
        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(new BigDecimal(10));
        transaction.setCurrency("RMB");
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setCategory("出行");
        transaction.setDescription("地铁");
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setCreateTime(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
    }

    private Page<Transaction> createPageWithTransaction() {
        Page<Transaction> page = new PageImpl<>(List.of(transaction), PageRequest.of(0, 1), 1);
        return page;
    }


    @Test
    public void testGetAllTransactions() throws Exception {
        // Setup
        when(transactionService.getAllTransactions(any(PageRequest.class)))
                .thenReturn(createPageWithTransaction());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/transaction")
                        .param("pageNo", "0")
                        .param("pageSize", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String response = objectMapper.writeValueAsString(ResponseFactory.success(createPageWithTransaction()).getBody());
        assertThat(result.getResponse().getContentAsString(StandardCharsets.UTF_8)).isEqualTo(response);
    }

    @Test
    public void testGetTransaction() throws Exception {
        // Setup
        when(transactionService.getTransactionById(1L)).thenReturn(transaction);

        // Run the test
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/transaction/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Verify the results
        String response = objectMapper.writeValueAsString(ResponseFactory.success(transaction).getBody());
        assertThat(result.getResponse().getContentAsString(StandardCharsets.UTF_8)).isEqualTo(response);
    }

    @Test
    public void testCreateTransaction() throws Exception {
        // Setup
        when(transactionService.createTransaction(any(Transaction.class))).thenReturn(transaction);

        // Run the test
        String jsonContent = objectMapper.writeValueAsString(transaction);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/transaction")
                        .content(jsonContent)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Verify the results
        String response = objectMapper.writeValueAsString(ResponseFactory.success(transaction).getBody());
        assertThat(result.getResponse().getContentAsString(StandardCharsets.UTF_8)).isEqualTo(response);
    }

    @Test
    public void testUpdateTransaction() throws Exception {
        // Setup
        when(transactionService.updateTransaction(any(Transaction.class))).thenReturn(transaction);

        // Run the test
        String jsonContent = objectMapper.writeValueAsString(transaction);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/transaction/1")
                        .content(jsonContent)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Verify the results
        String response = objectMapper.writeValueAsString(ResponseFactory.success(transaction).getBody());
        assertThat(result.getResponse().getContentAsString(StandardCharsets.UTF_8)).isEqualTo(response);
    }


    @Test
    public void testDeleteTransaction() throws Exception {
        // Setup
        when(transactionRepository.existsById(1L)).thenReturn(true);

        // Run the test
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/transaction/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Verify the results
        String response = objectMapper.writeValueAsString(ResponseFactory.success().getBody());
        assertThat(result.getResponse().getContentAsString(StandardCharsets.UTF_8)).isEqualTo(response);
    }

}
