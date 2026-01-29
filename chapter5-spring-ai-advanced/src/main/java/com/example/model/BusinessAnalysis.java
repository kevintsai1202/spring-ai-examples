package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 企業資料分析結果
 * 用於表示業務分析的綜合結果
 */
public record BusinessAnalysis(
                @JsonProperty("summary") String summary,
                @JsonProperty("key_metrics") List<KeyMetric> keyMetrics,
                @JsonProperty("recommendations") List<String> recommendations,
                @JsonProperty("risk_factors") List<String> riskFactors,
                @JsonProperty("confidence_score") double confidenceScore) {
}
