package com.finova3d.repository;

import com.finova3d.entity.RecurringTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RecurringTransactionRepository extends JpaRepository<RecurringTransaction, UUID> {
    List<RecurringTransaction> findByUserId(UUID userId);
    RecurringTransaction findByIdAndUserId(UUID id, UUID userId);
}
