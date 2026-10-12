package com.finova3d.controller;

import com.finova3d.dto.budget.BudgetResponse;
import com.finova3d.dto.budget.CreateBudgetRequest;
import com.finova3d.dto.budget.UpdateBudgetRequest;
import com.finova3d.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    public ResponseEntity<BudgetResponse> create(@Valid @RequestBody CreateBudgetRequest request, Authentication authentication) {
        return ResponseEntity.ok(budgetService.createBudget(authentication.getName(), request));
    }

    @GetMapping
    public ResponseEntity<List<BudgetResponse>> list(Authentication authentication) {
        return ResponseEntity.ok(budgetService.getAll(authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetResponse> getById(@PathVariable UUID id, Authentication authentication) {
        return ResponseEntity.ok(budgetService.getById(authentication.getName(), id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetResponse> update(@PathVariable UUID id, @RequestBody UpdateBudgetRequest request, Authentication authentication) {
        return ResponseEntity.ok(budgetService.updateBudget(authentication.getName(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, Authentication authentication) {
        budgetService.deleteBudget(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
