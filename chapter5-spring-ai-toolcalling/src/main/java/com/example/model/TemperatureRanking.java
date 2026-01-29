package com.example.model;

import java.util.List;

/**
 * 溫度排行資料
 */
public record TemperatureRanking(
        List<LocationTemperature> rankings,
        boolean success,
        String error,
        String updateTime
) {

    /**
     * 建立錯誤回應
     *
     * @param message 錯誤訊息
     * @return 溫度排行錯誤回應
     */
    public static TemperatureRanking error(String message) {
        return new TemperatureRanking(List.of(), false, message, null);
    }
}
