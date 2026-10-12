package com.finova3d.service;

import com.finova3d.dto.expense.CreateExpenseRequest;
import com.finova3d.dto.expense.ExpenseResponse;
import com.finova3d.entity.ApplicationUser;
import com.finova3d.entity.Category;
import com.finova3d.entity.Expense;
import com.finova3d.repository.CategoryRepository;
import com.finova3d.repository.ExpenseRepository;
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
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public ExpenseResponse createExpense(String email, CreateExpenseRequest request) {
        ApplicationUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (!"Expense".equalsIgnoreCase(category.getType())) {
            throw new RuntimeException("Category must be of type Expense");
        }

        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be greater than zero");
        }

        Expense expense = new Expense();
        expense.setUser(user);
        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setCategory(category);
        expense.setTransactionDate(request.getTransactionDate());
        expense.setPaymentMethod(request.getPaymentMethod() == null ? "Cash" : request.getPaymentMethod());
        expense.setMerchant(request.getMerchant());
        expense.setNotes(request.getNotes());
        expense.setCreatedAt(LocalDateTime.now());
        expense.setUpdatedAt(LocalDateTime.now());

        Expense saved = expenseRepository.save(expense);
        return toResponse(saved);
    }

    public List<ExpenseResponse> getAll(String email) {
        ApplicationUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<ExpenseResponse> list = new ArrayList<>();
        for (Expense expense : expenseRepository.findAll()) {
            if (expense.getUser().getId().equals(user.getId())) {
                list.add(toResponse(expense));
            }
        }
        return list;
    }

    private ExpenseResponse toResponse(Expense expense) {
        ExpenseResponse response = new ExpenseResponse();
        response.setId(expense.getId());
        response.setTitle(expense.getTitle());
        response.setAmount(expense.getAmount());
        response.setCategoryId(expense.getCategory().getId());
        response.setCategoryName(expense.getCategory().getName());
        response.setTransactionDate(expense.getTransactionDate());
        response.setPaymentMethod(expense.getPaymentMethod());
        response.setMerchant(expense.getMerchant());
        response.setNotes(expense.getNotes());
        response.setCreatedAt(expense.getCreatedAt());
        response.setUpdatedAt(expense.getUpdatedAt());
        return response;
    }
}
