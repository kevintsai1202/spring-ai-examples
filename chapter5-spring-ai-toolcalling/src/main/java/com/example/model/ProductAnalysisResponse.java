package com.example.model;

import java.time.LocalDateTime;

/**
 * 產品分析回應資料
 */
public record ProductAnalysisResponse(
        boolean success,
        String productCode,
        String analysisType,
        String analysis,
        long executionTime,
        LocalDateTime timestamp
) {
}
