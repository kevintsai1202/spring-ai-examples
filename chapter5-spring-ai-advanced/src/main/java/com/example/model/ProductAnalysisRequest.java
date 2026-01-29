package com.example.model;

/**
 * 產品分析請求資料
 */
public record ProductAnalysisRequest(
        String productCode,
        String analysisType,
        Integer year
) {
}
