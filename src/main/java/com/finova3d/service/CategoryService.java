package com.finova3d.service;

import com.finova3d.dto.category.CreateCategoryRequest;
import com.finova3d.entity.ApplicationUser;
import com.finova3d.entity.Category;
import com.finova3d.repository.CategoryRepository;
import com.finova3d.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public List<Category> getAllForUser(UUID userId) {
        return categoryRepository.findByUserIdOrUserIsNull(userId);
    }

    public Category createCategory(String email, CreateCategoryRequest request) {
        ApplicationUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (categoryRepository.existsByNameAndTypeAndUserId(request.getName(), request.getType(), user.getId())) {
            throw new RuntimeException("Category already exists for this user and type");
        }

        Category category = new Category();
        category.setUser(user);
        category.setName(request.getName().trim());
        category.setType(request.getType());
        category.setIcon(request.getIcon());
        category.setColor(request.getColor());
        category.setDefault(false);
        category.setCreatedAt(LocalDateTime.now());

        return categoryRepository.save(category);
    }

    public Category updateCategory(UUID categoryId, UUID userId, CreateCategoryRequest request) {
        Category category = categoryRepository.findByIdAndUserId(categoryId, userId);
        if (category == null) throw new RuntimeException("Category not found");

        category.setName(request.getName().trim());
        category.setType(request.getType());
        category.setIcon(request.getIcon());
        category.setColor(request.getColor());
        return categoryRepository.save(category);
    }

    public void deleteCategory(UUID categoryId, UUID userId) {
        Category category = categoryRepository.findByIdAndUserId(categoryId, userId);
        if (category == null) throw new RuntimeException("Category not found");
        categoryRepository.delete(category);
    }
}
