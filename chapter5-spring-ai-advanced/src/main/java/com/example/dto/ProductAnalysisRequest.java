package com.example.dto;

import lombok.Data;

/**
 * 產品分析請求 DTO
 * 用於 Tool 鏈控制器的產品深度分析請求
 */
@Data
public class ProductAnalysisRequest {
    /** 產品代碼 */
    private String productCode;

    /** 分析類型：comprehensive（全面）, sales（銷售）, product（產品） */
    private String analysisType;

    /** 分析年份 */
    private Integer year;

    /** 焦點領域 */
    private String focusArea;
}
