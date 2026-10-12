package com.finova3d.dto.income;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CreateIncomeRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotNull(message = "Category id is required")
    private UUID categoryId;

    @NotNull(message = "Income date is required")
    private LocalDateTime incomeDate;

    @NotBlank(message = "Source is required")
    private String source;

    private String paymentMethod = "Bank Transfer";
    private String notes;
}

@Data
class IncomeResponse {
    private UUID id;
    private String title;
    private BigDecimal amount;
    private UUID categoryId;
    private String categoryName;
    private LocalDateTime incomeDate;
    private String source;
    private String paymentMethod;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
