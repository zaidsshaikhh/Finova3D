package com.finova3d.controller;

import com.finova3d.dto.goal.CreateGoalRequest;
import com.finova3d.dto.goal.GoalContributionResponse;
import com.finova3d.dto.goal.GoalResponse;
import com.finova3d.dto.goal.UpdateGoalRequest;
import com.finova3d.service.GoalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;

    @PostMapping
    public ResponseEntity<GoalResponse> create(@Valid @RequestBody CreateGoalRequest request, Authentication authentication) {
        return ResponseEntity.ok(goalService.createGoal(authentication.getName(), request));
    }

    @GetMapping
    public ResponseEntity<List<GoalResponse>> list(Authentication authentication) {
        return ResponseEntity.ok(goalService.getAll(authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GoalResponse> getById(@PathVariable UUID id, Authentication authentication) {
        return ResponseEntity.ok(goalService.getById(authentication.getName(), id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GoalResponse> update(@PathVariable UUID id, @RequestBody UpdateGoalRequest request, Authentication authentication) {
        return ResponseEntity.ok(goalService.updateGoal(authentication.getName(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, Authentication authentication) {
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/contributions")
    public ResponseEntity<GoalContributionResponse> addContribution(@PathVariable UUID id,
                                                                  @RequestParam BigDecimal amount,
                                                                  @RequestParam(required = false) String notes,
                                                                  Authentication authentication) {
        return ResponseEntity.ok(goalService.addContribution(authentication.getName(), id, amount, notes));
    }

    @GetMapping("/{id}/contributions")
    public ResponseEntity<List<GoalContributionResponse>> getContributions(@PathVariable UUID id, Authentication authentication) {
        return ResponseEntity.ok(goalService.getContributions(authentication.getName(), id));
    }
}
