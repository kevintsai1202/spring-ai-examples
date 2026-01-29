package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 客戶洞察
 * 用於表示客戶行為分析的洞察結果
 */
public record CustomerInsights(
                @JsonProperty("segments") List<CustomerSegment> segments,
                @JsonProperty("behavior_patterns") List<String> behaviorPatterns,
                @JsonProperty("preferences") List<String> preferences,
                @JsonProperty("churn_risk") ChurnRisk churnRisk,
                @JsonProperty("personalization_suggestions") List<String> personalizationSuggestions) {
}
