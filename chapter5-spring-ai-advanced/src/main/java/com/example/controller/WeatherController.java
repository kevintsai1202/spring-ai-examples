package com.example.controller;

import com.example.model.TemperatureRanking;
import com.example.model.WeatherInfo;
import com.example.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 天氣查詢控制器
 */
@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
@Slf4j
public class WeatherController {

    /**
     * 重要物件：ChatClient
     */
    @Qualifier("chatClient")
    private final ChatClient chatClient;

    /**
     * 重要物件：天氣服務
     */
    private final WeatherService weatherService;

    /**
     * AI 天氣查詢
     *
     * @param question 問題內容
     * @return AI 回應
     */
    @GetMapping("/chat")
    public ResponseEntity<WeatherChatResponse> chatWeather(@RequestParam String question) {
        try {
            log.info("收到天氣查詢：{}", question);

            String response = chatClient
                    .prompt(question)
                    .tools(weatherService)
                    .call()
                    .content();

            WeatherChatResponse chatResponse = new WeatherChatResponse(
                    question,
                    response,
                    true,
                    null,
                    System.currentTimeMillis()
            );

            return ResponseEntity.ok(chatResponse);
        } catch (Exception e) {
            log.error("天氣查詢失敗: {}", e.getMessage(), e);

            WeatherChatResponse errorResponse = new WeatherChatResponse(
                    question,
                    null,
                    false,
                    "天氣查詢失敗：" + e.getMessage(),
                    System.currentTimeMillis()
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    /**
     * 直接取得天氣資訊
     *
     * @param location 地點
     * @return 天氣資訊
     */
    @GetMapping("/current")
    public ResponseEntity<WeatherInfo> getCurrentWeather(@RequestParam String location) {
        WeatherInfo weatherInfo = weatherService.getCurrentWeather(location);

        if (weatherInfo.success()) {
            return ResponseEntity.ok(weatherInfo);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(weatherInfo);
    }

    /**
     * 取得溫度排行榜
     *
     * @param topCount 前幾名
     * @return 溫度排行
     */
    @GetMapping("/temperature-ranking")
    public ResponseEntity<TemperatureRanking> getTemperatureRanking(
            @RequestParam(defaultValue = "10") Integer topCount) {
        TemperatureRanking ranking = weatherService.getTemperatureRanking(topCount);
        return ResponseEntity.ok(ranking);
    }

    /**
     * 天氣對話回應
     */
    public record WeatherChatResponse(
            String question,
            String answer,
            boolean success,
            String error,
            long timestamp
    ) {
    }
}
