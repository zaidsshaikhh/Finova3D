package com.finova3d.repository;

import com.finova3d.entity.FinancialGoal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FinancialGoalRepository extends JpaRepository<FinancialGoal, UUID> {
    List<FinancialGoal> findByUserId(UUID userId);
    FinancialGoal findByIdAndUserId(UUID id, UUID userId);
}
