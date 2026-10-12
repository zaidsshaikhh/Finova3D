package com.finova3d.repository;

import com.finova3d.entity.ProcessedRecurringOccurrence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProcessedRecurringOccurrenceRepository extends JpaRepository<ProcessedRecurringOccurrence, UUID> {
    List<ProcessedRecurringOccurrence> findByRecurringTransactionId(UUID recurringTransactionId);
}
