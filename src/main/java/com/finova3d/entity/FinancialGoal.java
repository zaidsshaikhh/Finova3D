package com.finova3d.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "financial_goals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinancialGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private ApplicationUser user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal targetAmount;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal currentSavedAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    private LocalDateTime targetDate;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String status = "Active"; // Active, Completed, Abandoned

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
}
