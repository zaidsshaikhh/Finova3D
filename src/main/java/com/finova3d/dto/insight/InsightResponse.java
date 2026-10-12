package com.finova3d.dto.insight;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class InsightResponse {
    private String type;
    private String title;
    private String description;
    private String severity;
    private UUID relatedCategoryId;
    private LocalDateTime createdAt;
}
