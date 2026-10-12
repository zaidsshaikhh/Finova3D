package com.finova3d.service;

import com.finova3d.dto.goal.CreateGoalRequest;
import com.finova3d.dto.goal.GoalContributionResponse;
import com.finova3d.dto.goal.GoalResponse;
import com.finova3d.dto.goal.UpdateGoalRequest;
import com.finova3d.entity.ApplicationUser;
import com.finova3d.entity.FinancialGoal;
import com.finova3d.entity.GoalContribution;
import com.finova3d.repository.FinancialGoalRepository;
import com.finova3d.repository.GoalContributionRepository;
import com.finova3d.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GoalService {

    private final FinancialGoalRepository financialGoalRepository;
    private final GoalContributionRepository goalContributionRepository;
    private final UserRepository userRepository;

    public GoalResponse createGoal(String email, CreateGoalRequest request) {
        ApplicationUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getTargetAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Target amount must be positive");
        }

        FinancialGoal goal = new FinancialGoal();
        goal.setUser(user);
        goal.setName(request.getName());
        goal.setTargetAmount(request.getTargetAmount());
        goal.setCurrentSavedAmount(BigDecimal.ZERO);
        goal.setTargetDate(request.getTargetDate());
        goal.setDescription(request.getDescription());
        goal.setStatus("Active");
        goal.setCreatedAt(LocalDateTime.now());
        goal.setUpdatedAt(LocalDateTime.now());

        return toResponse(financialGoalRepository.save(goal));
    }

    public List<GoalResponse> getAll(String email) {
        ApplicationUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<GoalResponse> list = new ArrayList<>();
        for (FinancialGoal goal : financialGoalRepository.findByUserId(user.getId())) {
            list.add(toResponse(goal));
        }
        return list;
    }

    public GoalResponse getById(String email, UUID id) {
        ApplicationUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FinancialGoal goal = financialGoalRepository.findByIdAndUserId(id, user.getId());
        if (goal == null) throw new RuntimeException("Goal not found");
        return toResponse(goal);
    }

    public GoalResponse updateGoal(String email, UUID id, UpdateGoalRequest request) {
        ApplicationUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FinancialGoal goal = financialGoalRepository.findByIdAndUserId(id, user.getId());
        if (goal == null) throw new RuntimeException("Goal not found");

        if (request.getName() != null) goal.setName(request.getName());
        if (request.getTargetAmount() != null) goal.setTargetAmount(request.getTargetAmount());
        if (request.getTargetDate() != null) goal.setTargetDate(request.getTargetDate());
        if (request.getDescription() != null) goal.setDescription(request.getDescription());
        if (request.getStatus() != null) goal.setStatus(request.getStatus());
        goal.setUpdatedAt(LocalDateTime.now());

        return toResponse(financialGoalRepository.save(goal));
    }

    public GoalContributionResponse addContribution(String email, UUID goalId, BigDecimal amount, String notes) {
        ApplicationUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FinancialGoal goal = financialGoalRepository.findByIdAndUserId(goalId, user.getId());
        if (goal == null) throw new RuntimeException("Goal not found");
        if (amount.compareTo(BigDecimal.ZERO) <= 0) throw new RuntimeException("Contribution must be positive");

        GoalContribution contribution = new GoalContribution();
        contribution.setGoal(goal);
        contribution.setAmount(amount);
        contribution.setContributionDate(LocalDateTime.now());
        contribution.setNotes(notes);
        contribution.setCreatedAt(LocalDateTime.now());
        goalContributionRepository.save(contribution);

        goal.setCurrentSavedAmount(goal.getCurrentSavedAmount().add(amount));
        goal.setUpdatedAt(LocalDateTime.now());
        financialGoalRepository.save(goal);

        GoalContributionResponse response = new GoalContributionResponse();
        response.setId(contribution.getId());
        response.setGoalId(goal.getId());
        response.setAmount(contribution.getAmount());
        response.setContributionDate(contribution.getContributionDate());
        response.setNotes(contribution.getNotes());
        return response;
    }

    public List<GoalContributionResponse> getContributions(String email, UUID goalId) {
        ApplicationUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FinancialGoal goal = financialGoalRepository.findByIdAndUserId(goalId, user.getId());
        if (goal == null) throw new RuntimeException("Goal not found");

        List<GoalContributionResponse> responses = new ArrayList<>();
        for (GoalContribution contribution : goalContributionRepository.findByGoalId(goalId)) {
            GoalContributionResponse r = new GoalContributionResponse();
            r.setId(contribution.getId());
            r.setGoalId(goalId);
            r.setAmount(contribution.getAmount());
            r.setContributionDate(contribution.getContributionDate());
            r.setNotes(contribution.getNotes());
            responses.add(r);
        }
        return responses;
    }

    private GoalResponse toResponse(FinancialGoal goal) {
        BigDecimal progress = BigDecimal.ZERO;
        if (goal.getTargetAmount().compareTo(BigDecimal.ZERO) > 0) {
            progress = goal.getCurrentSavedAmount().divide(goal.getTargetAmount(), 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
        }

        GoalResponse response = new GoalResponse();
        response.setId(goal.getId());
        response.setName(goal.getName());
        response.setTargetAmount(goal.getTargetAmount());
        response.setCurrentSavedAmount(goal.getCurrentSavedAmount());
        response.setTargetDate(goal.getTargetDate());
        response.setDescription(goal.getDescription());
        response.setStatus(goal.getStatus());
        response.setProgressPercentage(progress);
        response.setRemainingAmount(goal.getTargetAmount().subtract(goal.getCurrentSavedAmount()));

        if (goal.getTargetDate() != null && goal.getCurrentSavedAmount().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal remaining = goal.getTargetAmount().subtract(goal.getCurrentSavedAmount());
            response.setRequiredMonthlySavings(remaining.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP));
        } else {
            response.setRequiredMonthlySavings(BigDecimal.ZERO);
        }

        return response;
    }
}
