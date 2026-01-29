package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 天氣資訊資料
 */
public record WeatherInfo(
        @JsonProperty("location") String location,
        @JsonProperty("temperature") Double temperature,
        @JsonProperty("humidity") Double humidity,
        @JsonProperty("weather") String weather,
        @JsonProperty("rainfall") Double rainfall,
        @JsonProperty("wind_direction") String windDirection,
        @JsonProperty("wind_speed") Double windSpeed,
        @JsonProperty("success") boolean success,
        @JsonProperty("error") String error,
        @JsonProperty("observation_time") String observationTime
) {

    /**
     * 建立錯誤回應
     *
     * @param message 錯誤訊息
     * @return 錯誤回應
     */
    public static WeatherInfo error(String message) {
        return new WeatherInfo(null, null, null, null, null, null, null,
                false, message, null);
    }

    /**
     * 建立查無資料回應
     *
     * @param message 錯誤訊息
     * @return 查無資料回應
     */
    public static WeatherInfo notFound(String message) {
        return new WeatherInfo(null, null, null, null, null, null, null,
                false, message, null);
    }
}
