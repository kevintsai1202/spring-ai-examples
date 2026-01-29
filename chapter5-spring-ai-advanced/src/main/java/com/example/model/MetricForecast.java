package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 指標預測
 * 用於表示單個指標的預測資訊
 */
public record MetricForecast(
                @JsonProperty("metric_name") String metricName,
                @JsonProperty("current_value") double currentValue,
                @JsonProperty("predicted_value") double predictedValue,
                @JsonProperty("change_percentage") double changePercentage,
                @JsonProperty("confidence") double confidence) {
}
