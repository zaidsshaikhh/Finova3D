package com.finova3d.repository;

import com.finova3d.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BudgetRepository extends JpaRepository<Budget, UUID> {
    List<Budget> findByUserId(UUID userId);
    Budget findByIdAndUserId(UUID id, UUID userId);
}
