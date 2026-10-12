package com.finova3d.controller;

import com.finova3d.dto.insight.InsightResponse;
import com.finova3d.service.InsightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class InsightController {

    private final InsightService insightService;

    @GetMapping("/insights")
    public ResponseEntity<List<InsightResponse>> insights(Authentication authentication) {
        return ResponseEntity.ok(insightService.getInsights(authentication.getName()));
    }
}
