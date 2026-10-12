package com.finova3d.controller;

import com.finova3d.dto.recurring.CreateRecurringTransactionRequest;
import com.finova3d.dto.recurring.RecurringTransactionResponse;
import com.finova3d.dto.recurring.UpdateRecurringTransactionRequest;
import com.finova3d.service.RecurringTransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/recurring")
@RequiredArgsConstructor
public class RecurringTransactionController {

    private final RecurringTransactionService recurringTransactionService;

    @PostMapping
    public ResponseEntity<RecurringTransactionResponse> create(@Valid @RequestBody CreateRecurringTransactionRequest request,
                                                             Authentication authentication) {
        return ResponseEntity.ok(recurringTransactionService.create(authentication.getName(), request));
    }

    @GetMapping
    public ResponseEntity<List<RecurringTransactionResponse>> list(Authentication authentication) {
        return ResponseEntity.ok(recurringTransactionService.getAll(authentication.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecurringTransactionResponse> update(@PathVariable UUID id,
                                                             @RequestBody UpdateRecurringTransactionRequest request,
                                                             Authentication authentication) {
        return ResponseEntity.ok(recurringTransactionService.update(authentication.getName(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, Authentication authentication) {
        recurringTransactionService.delete(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
