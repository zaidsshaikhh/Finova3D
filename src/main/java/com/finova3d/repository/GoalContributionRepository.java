package com.finova3d.repository;

import com.finova3d.entity.GoalContribution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GoalContributionRepository extends JpaRepository<GoalContribution, UUID> {
    List<GoalContribution> findByGoalId(UUID goalId);
}
