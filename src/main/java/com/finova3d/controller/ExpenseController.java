package com.finova3d.controller;

import com.finova3d.dto.expense.CreateExpenseRequest;
import com.finova3d.dto.expense.ExpenseResponse;
import com.finova3d.dto.income.CreateIncomeRequest;
import com.finova3d.dto.income.IncomeResponse;
import com.finova3d.service.ExpenseService;
import com.finova3d.service.IncomeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping("/expenses")
    public ResponseEntity<ExpenseResponse> create(@Valid @RequestBody CreateExpenseRequest request, Authentication authentication) {
        return ResponseEntity.ok(expenseService.createExpense(authentication.getName(), request));
    }

    @GetMapping("/expenses")
    public ResponseEntity<List<ExpenseResponse>> list(Authentication authentication) {
        return ResponseEntity.ok(expenseService.getAll(authentication.getName()));
    }
}

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
class IncomeController {

    private final IncomeService incomeService;

    @PostMapping("/income")
    public ResponseEntity<IncomeResponse> create(@Valid @RequestBody CreateIncomeRequest request, Authentication authentication) {
        return ResponseEntity.ok(incomeService.createIncome(authentication.getName(), request));
    }

    @GetMapping("/income")
    public ResponseEntity<List<IncomeResponse>> list(Authentication authentication) {
        return ResponseEntity.ok(incomeService.getAll(authentication.getName()));
    }
}
