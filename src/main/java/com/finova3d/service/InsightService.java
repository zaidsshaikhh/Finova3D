package com.finova3d.service;

import com.finova3d.dto.insight.InsightResponse;
import com.finova3d.entity.ApplicationUser;
import com.finova3d.entity.Expense;
import com.finova3d.entity.Income;
import com.finova3d.repository.ExpenseRepository;
import com.finova3d.repository.IncomeRepository;
import com.finova3d.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InsightService {

    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;

    public List<InsightResponse> getInsights(String email) {
        ApplicationUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<InsightResponse> insights = new ArrayList<>();

        BigDecimal totalExpense = expenseRepository.findAll().stream()
                .filter(e -> e.getUser().getId().equals(user.getId()))
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalExpense.compareTo(BigDecimal.ZERO) > 0) {
            for (Expense expense : expenseRepository.findAll()) {
                if (expense.getUser().getId().equals(user.getId())) {
                    if (expense.getAmount().compareTo(new BigDecimal("100")) > 0) {
                        InsightResponse insight = new InsightResponse();
                        insight.setType("HighSpend");
                        insight.setTitle("Large expense detected");
                        insight.setDescription("A purchase exceeds the usual spending pattern for this account.");
                        insight.setSeverity("Medium");
                        insight.setRelatedCategoryId(expense.getCategory().getId());
                        insight.setCreatedAt(LocalDateTime.now());
                        insights.add(insight);
                    }
                }
            }
        }

        BigDecimal totalIncome = incomeRepository.findAll().stream()
                .filter(i -> i.getUser().getId().equals(user.getId()))
                .map(Income::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalIncome.compareTo(BigDecimal.ZERO) > 0 && totalExpense.compareTo(BigDecimal.ZERO) > 0) {
            InsightResponse insight = new InsightResponse();
            insight.setType("SavingsRate");
            insight.setTitle("Cash flow balance");
            insight.setDescription("Your net cash flow is being tracked from actual income and expense records.");
            insight.setSeverity("Low");
            insight.setCreatedAt(LocalDateTime.now());
            insights.add(insight);
        }

        return insights;
    }
}
