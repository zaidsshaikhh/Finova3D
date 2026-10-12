package com.finova3d.dto.budget;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CreateBudgetRequest {
    private String name;
    private UUID categoryId;
    private BigDecimal amount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal alertThresholdPercentage = new BigDecimal("80.00");
}

@Data
public class UpdateBudgetRequest {
    private String name;
    private UUID categoryId;
    private BigDecimal amount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal alertThresholdPercentage;
}

@Data
public class BudgetResponse {
    private UUID id;
    private String name;
    private UUID categoryId;
    private String categoryName;
    private BigDecimal amount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal alertThresholdPercentage;
    private BigDecimal spentAmount;
    private BigDecimal remainingAmount;
    private BigDecimal utilizationPercentage;
    private String status;
}
