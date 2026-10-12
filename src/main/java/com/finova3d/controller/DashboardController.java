package com.finova3d.controller;

import com.finova3d.dto.dashboard.CashFlowPoint;
import com.finova3d.dto.dashboard.CategoryBreakdownResponse;
import com.finova3d.dto.dashboard.DashboardSummaryResponse;
import com.finova3d.dto.dashboard.MonthlyTrendResponse;
import com.finova3d.dto.dashboard.RecentTransactionResponse;
import com.finova3d.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryResponse> summary(Authentication authentication,
                                                           @RequestParam(required = false) String from,
                                                           @RequestParam(required = false) String to) {
        LocalDateTime start = from != null ? LocalDateTime.parse(from) : LocalDateTime.now().minusMonths(1);
        LocalDateTime end = to != null ? LocalDateTime.parse(to) : LocalDateTime.now();
        return ResponseEntity.ok(dashboardService.getSummary(authentication.getName(), start, end));
    }

    @GetMapping("/cash-flow")
    public ResponseEntity<List<CashFlowPoint>> cashFlow(Authentication authentication,
                                                      @RequestParam(required = false) String from,
                                                      @RequestParam(required = false) String to) {
        LocalDateTime start = from != null ? LocalDateTime.parse(from) : LocalDateTime.now().minusMonths(1);
        LocalDateTime end = to != null ? LocalDateTime.parse(to) : LocalDateTime.now();
        return ResponseEntity.ok(dashboardService.getCashFlow(authentication.getName(), start, end));
    }

    @GetMapping("/category-breakdown")
    public ResponseEntity<List<CategoryBreakdownResponse>> categoryBreakdown(Authentication authentication,
                                                                           @RequestParam(required = false) String from,
                                                                           @RequestParam(required = false) String to) {
        LocalDateTime start = from != null ? LocalDateTime.parse(from) : LocalDateTime.now().minusMonths(1);
        LocalDateTime end = to != null ? LocalDateTime.parse(to) : LocalDateTime.now();
        return ResponseEntity.ok(dashboardService.getCategoryBreakdown(authentication.getName(), start, end));
    }

    @GetMapping("/monthly-trends")
    public ResponseEntity<List<MonthlyTrendResponse>> monthlyTrends(Authentication authentication,
                                                                   @RequestParam(defaultValue = "6") int months) {
        return ResponseEntity.ok(dashboardService.getMonthlyTrends(authentication.getName(), months));
    }

    @GetMapping("/recent-transactions")
    public ResponseEntity<List<RecentTransactionResponse>> recentTransactions(Authentication authentication,
                                                                           @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(dashboardService.getRecentTransactions(authentication.getName(), limit));
    }
}
