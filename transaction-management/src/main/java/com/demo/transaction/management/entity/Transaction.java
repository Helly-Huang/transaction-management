package com.demo.transaction.management.entity;

import com.demo.transaction.management.common.TransactionStatus;
import com.demo.transaction.management.common.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "金额不能为空")
    @Positive(message = "金额不能为负数")
    private BigDecimal amount;

    @NotNull(message = "货币类型不能为空")
    private String currency;

    @NotNull(message = "交易类型不能为空")
    private TransactionType type;

    @NotBlank(message = "交易种类不能为空")
    @Size(max = 50, message = "交易种类长度不能超过50")
    private String category;

    @Size(max = 255, message = "交易描述长度不能超过255")
    private String description;

    @NotNull(message = "交易状态不能为空")
    private TransactionStatus status;

    @PastOrPresent(message = "创建时间不能为将来的时间")
    private LocalDateTime createTime;

    @PastOrPresent(message = "修改时间不能为将来的时间")
    private LocalDateTime updateTime;

}


