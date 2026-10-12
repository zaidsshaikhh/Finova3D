package com.finova3d.dto.goal;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CreateGoalRequest {
    private String name;
    private BigDecimal targetAmount;
    private LocalDateTime targetDate;
    private String description;
}

@Data
public class UpdateGoalRequest {
    private String name;
    private BigDecimal targetAmount;
    private LocalDateTime targetDate;
    private String description;
    private String status;
}

@Data
public class GoalResponse {
    private UUID id;
    private String name;
    private BigDecimal targetAmount;
    private BigDecimal currentSavedAmount;
    private LocalDateTime targetDate;
    private String description;
    private String status;
    private BigDecimal progressPercentage;
    private BigDecimal remainingAmount;
    private BigDecimal requiredMonthlySavings;
}

@Data
public class GoalContributionResponse {
    private UUID id;
    private UUID goalId;
    private BigDecimal amount;
    private LocalDateTime contributionDate;
    private String notes;
}
