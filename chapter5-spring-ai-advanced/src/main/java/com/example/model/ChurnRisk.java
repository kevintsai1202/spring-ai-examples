package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 流失風險分析
 * 用於表示客戶流失風險的資訊
 */
public record ChurnRisk(
                @JsonProperty("high_risk_count") int highRiskCount,
                @JsonProperty("medium_risk_count") int mediumRiskCount,
                @JsonProperty("low_risk_count") int lowRiskCount,
                @JsonProperty("key_indicators") List<String> keyIndicators) {
}
