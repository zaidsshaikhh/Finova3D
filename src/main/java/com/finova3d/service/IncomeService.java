package com.finova3d.service;

import com.finova3d.dto.income.CreateIncomeRequest;
import com.finova3d.dto.income.IncomeResponse;
import com.finova3d.entity.ApplicationUser;
import com.finova3d.entity.Category;
import com.finova3d.entity.Income;
import com.finova3d.repository.CategoryRepository;
import com.finova3d.repository.IncomeRepository;
import com.finova3d.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeService {

    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public IncomeResponse createIncome(String email, CreateIncomeRequest request) {
        ApplicationUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (!"Income".equalsIgnoreCase(category.getType())) {
            throw new RuntimeException("Category must be of type Income");
        }

        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be greater than zero");
        }

        Income income = new Income();
        income.setUser(user);
        income.setTitle(request.getTitle());
        income.setAmount(request.getAmount());
        income.setCategory(category);
        income.setIncomeDate(request.getIncomeDate());
        income.setSource(request.getSource());
        income.setPaymentMethod(request.getPaymentMethod() == null ? "Bank Transfer" : request.getPaymentMethod());
        income.setNotes(request.getNotes());
        income.setCreatedAt(LocalDateTime.now());
        income.setUpdatedAt(LocalDateTime.now());

        Income saved = incomeRepository.save(income);
        return toResponse(saved);
    }

    public List<IncomeResponse> getAll(String email) {
        ApplicationUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<IncomeResponse> list = new ArrayList<>();
        for (Income income : incomeRepository.findAll()) {
            if (income.getUser().getId().equals(user.getId())) {
                list.add(toResponse(income));
            }
        }
        return list;
    }

    private IncomeResponse toResponse(Income income) {
        IncomeResponse response = new IncomeResponse();
        response.setId(income.getId());
        response.setTitle(income.getTitle());
        response.setAmount(income.getAmount());
        response.setCategoryId(income.getCategory().getId());
        response.setCategoryName(income.getCategory().getName());
        response.setIncomeDate(income.getIncomeDate());
        response.setSource(income.getSource());
        response.setPaymentMethod(income.getPaymentMethod());
        response.setNotes(income.getNotes());
        response.setCreatedAt(income.getCreatedAt());
        response.setUpdatedAt(income.getUpdatedAt());
        return response;
    }
}
