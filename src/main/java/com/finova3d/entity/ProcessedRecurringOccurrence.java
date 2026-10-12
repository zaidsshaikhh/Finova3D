package com.finova3d.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "processed_recurring_occurrences")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessedRecurringOccurrence {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recurring_transaction_id", nullable = false)
    private RecurringTransaction recurringTransaction;

    @Column(nullable = false)
    private LocalDateTime scheduledDate;

    @Column
    private UUID generatedExpenseId;

    @Column
    private UUID generatedIncomeId;

    @Column(nullable = false)
    private LocalDateTime processedAt = LocalDateTime.now();
}
