package com.finova3d.dto.category;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CreateCategoryRequest {
    private String name;
    private String type = "Expense";
    private String icon = "default";
    private String color = "#808080";
}

@Data
class CategoryResponse {
    private UUID id;
    private String name;
    private String type;
    private String icon;
    private String color;
    private boolean isDefault;
    private LocalDateTime createdAt;
}
