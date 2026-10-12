package com.finova3d.dto.dashboard;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class DashboardSummaryResponse {
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal netCashFlow;
    private BigDecimal savingsRate;
    private BigDecimal currentMonthBudgetUtilization;
    private BigDecimal previousPeriodComparison;
    private long transactionCount;
    private long expenseCount;
    private long incomeCount;
}

@Data
public class CashFlowPoint {
    private String label;
    private BigDecimal income;
    private BigDecimal expenses;
}

@Data
public class CategoryBreakdownResponse {
    private String categoryName;
    private BigDecimal totalAmount;
    private double percentage;
}

@Data
public class MonthlyTrendResponse {
    private String month;
    private BigDecimal income;
    private BigDecimal expenses;
    private BigDecimal net;
}

@Data
public class RecentTransactionResponse {
    private String type;
    private String title;
    private BigDecimal amount;
    private String categoryName;
    private String transactionDate;
}
