package com.example.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 產品分析回應 DTO
 * 用於 Tool 鏈控制器返回的產品分析結果
 */
@Data
@Builder
public class ProductAnalysisResponse {
    /** 分析是否成功 */
    private boolean success;

    /** 產品代碼 */
    private String productCode;

    /** 分析類型 */
    private String analysisType;

    /** 詳細分析結果 */
    private String analysis;

    /** 執行耗時（毫秒） */
    private Long executionTime;

    /** 錯誤資訊（失敗時） */
    private String error;

    /** 時間戳記 */
    private LocalDateTime timestamp;
}
