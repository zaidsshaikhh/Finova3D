package com.finova3d.controller;

import com.finova3d.dto.category.CreateCategoryRequest;
import com.finova3d.entity.Category;
import com.finova3d.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> getAll(Authentication authentication) {
        return ResponseEntity.ok(categoryService.getAllForUser(UUID.fromString(authentication.getName())));
    }

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody CreateCategoryRequest request, Authentication authentication) {
        return ResponseEntity.ok(categoryService.createCategory(authentication.getName(), request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable UUID id, @RequestBody CreateCategoryRequest request, Authentication authentication) {
        return ResponseEntity.ok(categoryService.updateCategory(id, UUID.fromString(authentication.getName()), request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, Authentication authentication) {
        categoryService.deleteCategory(id, UUID.fromString(authentication.getName()));
        return ResponseEntity.noContent().build();
    }
}
