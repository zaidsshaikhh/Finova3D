package com.finova3d.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "recurring_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecurringTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private ApplicationUser user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String transactionType; // Expense or Income

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private String frequency; // Daily, Weekly, Monthly, Yearly

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column
    private LocalDateTime nextDueDate;

    @Column
    private LocalDateTime endDate;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
}
