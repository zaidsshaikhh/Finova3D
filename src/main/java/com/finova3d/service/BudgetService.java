package com.finova3d.service;

import com.finova3d.dto.budget.BudgetResponse;
import com.finova3d.dto.budget.CreateBudgetRequest;
import com.finova3d.dto.budget.UpdateBudgetRequest;
import com.finova3d.entity.ApplicationUser;
import com.finova3d.entity.Budget;
import com.finova3d.entity.Category;
import com.finova3d.entity.Expense;
import com.finova3d.repository.BudgetRepository;
import com.finova3d.repository.CategoryRepository;
import com.finova3d.repository.ExpenseRepository;
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
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ExpenseRepository expenseRepository;

    public BudgetResponse createBudget(String email, CreateBudgetRequest request) {
        ApplicationUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (!"Expense".equalsIgnoreCase(category.getType())) {
            throw new RuntimeException("Budget category must be expense type");
        }

        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Budget amount must be positive");
        }

        Budget budget = new Budget();
        budget.setUser(user);
        budget.setName(request.getName());
        budget.setCategory(category);
        budget.setAmount(request.getAmount());
        budget.setStartDate(request.getStartDate());
        budget.setEndDate(request.getEndDate());
        budget.setAlertThresholdPercentage(request.getAlertThresholdPercentage());
        budget.setCreatedAt(LocalDateTime.now());
        budget.setUpdatedAt(LocalDateTime.now());

        Budget saved = budgetRepository.save(budget);
        return toResponse(saved);
    }

    public List<BudgetResponse> getAll(String email) {
        ApplicationUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Budget> budgets = budgetRepository.findByUserId(user.getId());
        List<BudgetResponse> responses = new ArrayList<>();
        for (Budget budget : budgets) {
            responses.add(toResponse(budget));
        }
        return responses;
    }

    public BudgetResponse getById(String email, UUID id) {
        ApplicationUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Budget budget = budgetRepository.findByIdAndUserId(id, user.getId());
        if (budget == null) throw new RuntimeException("Budget not found");
        return toResponse(budget);
    }

    public BudgetResponse updateBudget(String email, UUID id, UpdateBudgetRequest request) {
        ApplicationUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Budget budget = budgetRepository.findByIdAndUserId(id, user.getId());
        if (budget == null) throw new RuntimeException("Budget not found");

        if (request.getName() != null) budget.setName(request.getName());
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            if (!"Expense".equalsIgnoreCase(category.getType())) {
                throw new RuntimeException("Budget category must be expense type");
            }
            budget.setCategory(category);
        }
        if (request.getAmount() != null) budget.setAmount(request.getAmount());
        if (request.getStartDate() != null) budget.setStartDate(request.getStartDate());
        if (request.getEndDate() != null) budget.setEndDate(request.getEndDate());
        if (request.getAlertThresholdPercentage() != null) budget.setAlertThresholdPercentage(request.getAlertThresholdPercentage());
        budget.setUpdatedAt(LocalDateTime.now());

        return toResponse(budgetRepository.save(budget));
    }

    public void deleteBudget(String email, UUID id) {
        ApplicationUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Budget budget = budgetRepository.findByIdAndUserId(id, user.getId());
        if (budget == null) throw new RuntimeException("Budget not found");
        budgetRepository.delete(budget);
    }

    private BudgetResponse toResponse(Budget budget) {
        BigDecimal spent = BigDecimal.ZERO;
        List<Expense> expenses = expenseRepository.findAll();
        for (Expense expense : expenses) {
            if (expense.getUser().getId().equals(budget.getUser().getId())
                    && expense.getCategory().getId().equals(budget.getCategory().getId())
                    && !expense.getTransactionDate().isBefore(budget.getStartDate())
                    && !expense.getTransactionDate().isAfter(budget.getEndDate())) {
                spent = spent.add(expense.getAmount());
            }
        }

        BigDecimal remaining = budget.getAmount().subtract(spent);
        BigDecimal utilization = BigDecimal.ZERO;
        if (budget.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            utilization = spent.divide(budget.getAmount(), 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
        }

        BudgetResponse response = new BudgetResponse();
        response.setId(budget.getId());
        response.setName(budget.getName());
        response.setCategoryId(budget.getCategory().getId());
        response.setCategoryName(budget.getCategory().getName());
        response.setAmount(budget.getAmount());
        response.setStartDate(budget.getStartDate());
        response.setEndDate(budget.getEndDate());
        response.setAlertThresholdPercentage(budget.getAlertThresholdPercentage());
        response.setSpentAmount(spent);
        response.setRemainingAmount(remaining);
        response.setUtilizationPercentage(utilization);
        response.setStatus(utilization.compareTo(budget.getAlertThresholdPercentage()) >= 0 ? "Warning" : "Healthy");
        return response;
    }
}
