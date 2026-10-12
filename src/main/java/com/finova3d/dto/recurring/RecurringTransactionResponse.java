package com.finova3d.dto.recurring;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CreateRecurringTransactionRequest {
    private String title;
    private BigDecimal amount;
    private String transactionType;
    private UUID categoryId;
    private String frequency;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime nextDueDate;
    private boolean isActive = true;
}

@Data
public class UpdateRecurringTransactionRequest {
    private String title;
    private BigDecimal amount;
    private String transactionType;
    private UUID categoryId;
    private String frequency;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime nextDueDate;
    private Boolean isActive;
}

@Data
public class RecurringTransactionResponse {
    private UUID id;
    private String title;
    private BigDecimal amount;
    private String transactionType;
    private UUID categoryId;
    private String categoryName;
    private String frequency;
    private LocalDateTime startDate;
    private LocalDateTime nextDueDate;
    private LocalDateTime endDate;
    private boolean isActive;
}
