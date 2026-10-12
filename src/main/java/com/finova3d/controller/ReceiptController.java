package com.finova3d.controller;

import com.finova3d.dto.receipt.ReceiptUploadResponse;
import com.finova3d.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/receipts")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptService receiptService;

    @PostMapping("/upload/{expenseId}")
    public ResponseEntity<ReceiptUploadResponse> upload(@PathVariable UUID expenseId,
                                                      @RequestParam("file") MultipartFile file,
                                                      Authentication authentication) {
        return ResponseEntity.ok(receiptService.upload(authentication.getName(), expenseId, file));
    }
}
