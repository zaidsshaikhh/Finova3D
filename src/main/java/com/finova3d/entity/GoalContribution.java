package com.finova3d.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "goal_contributions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalContribution {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "financial_goal_id", nullable = false)
    private FinancialGoal goal;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime contributionDate;

    @Column(length = 500)
    private String notes;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
