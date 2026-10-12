package com.finova3d.service;

import com.finova3d.dto.recurring.CreateRecurringTransactionRequest;
import com.finova3d.dto.recurring.RecurringTransactionResponse;
import com.finova3d.dto.recurring.UpdateRecurringTransactionRequest;
import com.finova3d.entity.ApplicationUser;
import com.finova3d.entity.Category;
import com.finova3d.entity.RecurringTransaction;
import com.finova3d.repository.CategoryRepository;
import com.finova3d.repository.RecurringTransactionRepository;
import com.finova3d.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecurringTransactionService {

    private final RecurringTransactionRepository recurringTransactionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public RecurringTransactionResponse create(String email, CreateRecurringTransactionRequest request) {
        ApplicationUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (!request.getTransactionType().equalsIgnoreCase("Expense") && !request.getTransactionType().equalsIgnoreCase("Income")) {
            throw new RuntimeException("Transaction type must be Expense or Income");
        }

        if (!"Expense".equalsIgnoreCase(request.getTransactionType()) && !"Income".equalsIgnoreCase(request.getTransactionType())) {
            throw new RuntimeException("Invalid transaction type");
        }

        RecurringTransaction recurring = new RecurringTransaction();
        recurring.setUser(user);
        recurring.setTitle(request.getTitle());
        recurring.setAmount(request.getAmount());
        recurring.setTransactionType(request.getTransactionType());
        recurring.setCategory(category);
        recurring.setFrequency(request.getFrequency());
        recurring.setStartDate(request.getStartDate());
        recurring.setNextDueDate(request.getNextDueDate() != null ? request.getNextDueDate() : request.getStartDate());
        recurring.setEndDate(request.getEndDate());
        recurring.setActive(request.isActive());
        recurring.setCreatedAt(LocalDateTime.now());
        recurring.setUpdatedAt(LocalDateTime.now());

        return toResponse(recurringTransactionRepository.save(recurring));
    }

    public List<RecurringTransactionResponse> getAll(String email) {
        ApplicationUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<RecurringTransactionResponse> response = new ArrayList<>();
        for (RecurringTransaction recurring : recurringTransactionRepository.findByUserId(user.getId())) {
            response.add(toResponse(recurring));
        }
        return response;
    }

    public RecurringTransactionResponse update(String email, UUID id, UpdateRecurringTransactionRequest request) {
        ApplicationUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        RecurringTransaction recurring = recurringTransactionRepository.findByIdAndUserId(id, user.getId());
        if (recurring == null) throw new RuntimeException("Recurring transaction not found");

        if (request.getTitle() != null) recurring.setTitle(request.getTitle());
        if (request.getAmount() != null) recurring.setAmount(request.getAmount());
        if (request.getTransactionType() != null) recurring.setTransactionType(request.getTransactionType());
        if (request.getCategoryId() != null) recurring.setCategory(categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found")));
        if (request.getFrequency() != null) recurring.setFrequency(request.getFrequency());
        if (request.getStartDate() != null) recurring.setStartDate(request.getStartDate());
        if (request.getEndDate() != null) recurring.setEndDate(request.getEndDate());
        if (request.getNextDueDate() != null) recurring.setNextDueDate(request.getNextDueDate());
        if (request.getIsActive() != null) recurring.setActive(request.getIsActive());

        recurring.setUpdatedAt(LocalDateTime.now());
        return toResponse(recurringTransactionRepository.save(recurring));
    }

    public void delete(String email, UUID id) {
        ApplicationUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        RecurringTransaction recurring = recurringTransactionRepository.findByIdAndUserId(id, user.getId());
        if (recurring == null) throw new RuntimeException("Recurring transaction not found");
        recurringTransactionRepository.delete(recurring);
    }

    private RecurringTransactionResponse toResponse(RecurringTransaction recurring) {
        RecurringTransactionResponse response = new RecurringTransactionResponse();
        response.setId(recurring.getId());
        response.setTitle(recurring.getTitle());
        response.setAmount(recurring.getAmount());
        response.setTransactionType(recurring.getTransactionType());
        response.setCategoryId(recurring.getCategory().getId());
        response.setCategoryName(recurring.getCategory().getName());
        response.setFrequency(recurring.getFrequency());
        response.setStartDate(recurring.getStartDate());
        response.setNextDueDate(recurring.getNextDueDate());
        response.setEndDate(recurring.getEndDate());
        response.setActive(recurring.isActive());
        return response;
    }
}
