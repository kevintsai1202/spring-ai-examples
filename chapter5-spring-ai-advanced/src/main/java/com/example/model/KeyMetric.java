package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 關鍵指標
 * 用於表示業務分析中的關鍵指標
 */
public record KeyMetric(
                @JsonProperty("name") String name,
                @JsonProperty("value") String value,
                @JsonProperty("trend") String trend,
                @JsonProperty("importance") String importance) {
}
