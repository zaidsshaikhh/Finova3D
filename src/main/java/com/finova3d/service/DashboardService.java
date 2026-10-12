package com.finova3d.service;

import com.finova3d.dto.dashboard.CashFlowPoint;
import com.finova3d.dto.dashboard.CategoryBreakdownResponse;
import com.finova3d.dto.dashboard.DashboardSummaryResponse;
import com.finova3d.dto.dashboard.MonthlyTrendResponse;
import com.finova3d.dto.dashboard.RecentTransactionResponse;
import com.finova3d.entity.ApplicationUser;
import com.finova3d.entity.Category;
import com.finova3d.entity.Expense;
import com.finova3d.entity.Income;
import com.finova3d.repository.ExpenseRepository;
import com.finova3d.repository.IncomeRepository;
import com.finova3d.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;

    public DashboardSummaryResponse getSummary(String email, LocalDateTime from, LocalDateTime to) {
        ApplicationUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Expense> expenses = expenseRepository.findAll().stream()
                .filter(e -> e.getUser().getId().equals(user.getId()))
                .filter(e -> !e.getTransactionDate().isBefore(from))
                .filter(e -> !e.getTransactionDate().isAfter(to))
                .toList();

        List<Income> incomes = incomeRepository.findAll().stream()
                .filter(i -> i.getUser().getId().equals(user.getId()))
                .filter(i -> !i.getIncomeDate().isBefore(from))
                .filter(i -> !i.getIncomeDate().isAfter(to))
                .toList();

        BigDecimal totalIncome = incomes.stream().map(Income::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalExpenses = expenses.stream().map(Expense::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal netCashFlow = totalIncome.subtract(totalExpenses);
        BigDecimal savingsRate = BigDecimal.ZERO;
        if (totalIncome.compareTo(BigDecimal.ZERO) > 0) {
            savingsRate = netCashFlow.divide(totalIncome, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
        }

        DashboardSummaryResponse response = new DashboardSummaryResponse();
        response.setTotalIncome(totalIncome);
        response.setTotalExpenses(totalExpenses);
        response.setNetCashFlow(netCashFlow);
        response.setSavingsRate(savingsRate);
        response.setCurrentMonthBudgetUtilization(BigDecimal.ZERO);
        response.setPreviousPeriodComparison(BigDecimal.ZERO);
        response.setTransactionCount(expenses.size() + incomes.size());
        response.setExpenseCount(expenses.size());
        response.setIncomeCount(incomes.size());
        return response;
    }

    public List<CashFlowPoint> getCashFlow(String email, LocalDateTime from, LocalDateTime to) {
        List<CashFlowPoint> points = new ArrayList<>();
        ApplicationUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        for (LocalDateTime date = from; !date.isAfter(to); date = date.plusDays(1)) {
            BigDecimal income = incomeRepository.findAll().stream()
                    .filter(i -> i.getUser().getId().equals(user.getId()))
                    .filter(i -> i.getIncomeDate().toLocalDate().equals(date.toLocalDate()))
                    .map(Income::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal expenses = expenseRepository.findAll().stream()
                    .filter(e -> e.getUser().getId().equals(user.getId()))
                    .filter(e -> e.getTransactionDate().toLocalDate().equals(date.toLocalDate()))
                    .map(Expense::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            CashFlowPoint point = new CashFlowPoint();
            point.setLabel(date.toLocalDate().toString());
            point.setIncome(income);
            point.setExpenses(expenses);
            points.add(point);
        }

        return points;
    }

    public List<CategoryBreakdownResponse> getCategoryBreakdown(String email, LocalDateTime from, LocalDateTime to) {
        ApplicationUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<CategoryBreakdownResponse> list = new ArrayList<>();
        BigDecimal total = expenseRepository.findAll().stream()
                .filter(e -> e.getUser().getId().equals(user.getId()))
                .filter(e -> !e.getTransactionDate().isBefore(from))
                .filter(e -> !e.getTransactionDate().isAfter(to))
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (total.compareTo(BigDecimal.ZERO) == 0) return list;

        for (Expense expense : expenseRepository.findAll()) {
            if (expense.getUser().getId().equals(user.getId()) && !expense.getTransactionDate().isBefore(from) && !expense.getTransactionDate().isAfter(to)) {
                CategoryBreakdownResponse response = new CategoryBreakdownResponse();
                response.setCategoryName(expense.getCategory().getName());
                response.setTotalAmount(expense.getAmount());
                response.setPercentage(expense.getAmount().divide(total, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).doubleValue());
                list.add(response);
            }
        }

        return list;
    }

    public List<MonthlyTrendResponse> getMonthlyTrends(String email, int months) {
        List<MonthlyTrendResponse> result = new ArrayList<>();
        ApplicationUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDateTime today = LocalDateTime.now();
        for (int i = months - 1; i >= 0; i--) {
            LocalDateTime start = today.minusMonths(i).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime end = start.plusMonths(1).minusNanos(1);

            BigDecimal income = incomeRepository.findAll().stream()
                    .filter(item -> item.getUser().getId().equals(user.getId()))
                    .filter(item -> !item.getIncomeDate().isBefore(start))
                    .filter(item -> !item.getIncomeDate().isAfter(end))
                    .map(Income::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal expenses = expenseRepository.findAll().stream()
                    .filter(item -> item.getUser().getId().equals(user.getId()))
                    .filter(item -> !item.getTransactionDate().isBefore(start))
                    .filter(item -> !item.getTransactionDate().isAfter(end))
                    .map(Expense::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            MonthlyTrendResponse trend = new MonthlyTrendResponse();
            trend.setMonth(start.getMonth().name() + " " + start.getYear());
            trend.setIncome(income);
            trend.setExpenses(expenses);
            trend.setNet(income.subtract(expenses));
            result.add(trend);
        }

        return result;
    }

    public List<RecentTransactionResponse> getRecentTransactions(String email, int limit) {
        ApplicationUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<RecentTransactionResponse> result = new ArrayList<>();

        for (Expense e : expenseRepository.findAll()) {
            if (e.getUser().getId().equals(user.getId())) {
                RecentTransactionResponse r = new RecentTransactionResponse();
                r.setType("Expense");
                r.setTitle(e.getTitle());
                r.setAmount(e.getAmount());
                r.setCategoryName(e.getCategory().getName());
                r.setTransactionDate(e.getTransactionDate().toString());
                result.add(r);
            }
        }

        for (Income i : incomeRepository.findAll()) {
            if (i.getUser().getId().equals(user.getId())) {
                RecentTransactionResponse r = new RecentTransactionResponse();
                r.setType("Income");
                r.setTitle(i.getTitle());
                r.setAmount(i.getAmount());
                r.setCategoryName(i.getCategory().getName());
                r.setTransactionDate(i.getIncomeDate().toString());
                result.add(r);
            }
        }

        result.sort((a, b) -> b.getTransactionDate().compareTo(a.getTransactionDate()));
        if (result.size() > limit) return result.subList(0, limit);
        return result;
    }
}
