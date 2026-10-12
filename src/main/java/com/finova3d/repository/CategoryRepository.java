package com.finova3d.repository;

import com.finova3d.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findByUserIdOrUserIsNull(UUID userId);
    Category findByIdAndUserId(UUID id, UUID userId);
    boolean existsByNameAndTypeAndUserId(String name, String type, UUID userId);
}
