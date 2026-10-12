package com.finova3d.dto.receipt;

import lombok.Data;

@Data
public class ReceiptUploadResponse {
    private String fileName;
    private String fileUrl;
    private String message;
}
