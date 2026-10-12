package com.finova3d.service;

import com.finova3d.dto.receipt.ReceiptUploadResponse;
import com.finova3d.entity.ApplicationUser;
import com.finova3d.entity.Expense;
import com.finova3d.repository.ExpenseRepository;
import com.finova3d.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;

    public ReceiptUploadResponse upload(String email, UUID expenseId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("No file selected");
        }

        ApplicationUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You cannot access this expense receipt");
        }

        String allowedTypes = "jpg,jpeg,png,pdf";
        String original = file.getOriginalFilename();
        String ext = original != null && original.contains(".") ? original.substring(original.lastIndexOf('.') + 1).toLowerCase() : "";
        if (!allowedTypes.contains(ext)) {
            throw new RuntimeException("Unsupported file type");
        }

        try {
            Path uploadDir = Paths.get("uploads/receipts");
            Files.createDirectories(uploadDir);
            String serverFileName = UUID.randomUUID() + "." + ext;
            Path target = uploadDir.resolve(serverFileName);
            Files.write(target, file.getBytes());

            expense.setReceiptPath(target.toString());
            expenseRepository.save(expense);

            ReceiptUploadResponse response = new ReceiptUploadResponse();
            response.setFileName(serverFileName);
            response.setFileUrl("/api/receipts/" + expenseId);
            response.setMessage("Receipt uploaded successfully");
            return response;
        } catch (IOException e) {
            throw new RuntimeException("Receipt upload failed", e);
        }
    }
}
