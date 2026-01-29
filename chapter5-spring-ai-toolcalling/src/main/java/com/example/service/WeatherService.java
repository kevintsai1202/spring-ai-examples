package com.example.service;

import com.example.model.LocationTemperature;
import com.example.model.TemperatureRanking;
import com.example.model.WeatherInfo;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 天氣查詢服務
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherService {

    /**
     * 重要物件：HTTP 客戶端
     */
    private final RestTemplate restTemplate;

    @Value("${cwa.api.key}")
    private String apiKey;

    @Value("${cwa.api.base-url}")
    private String baseUrl;

    /**
     * 取得指定地區的目前天氣資訊
     *
     * @param location 地區名稱
     * @return 天氣資訊
     */
    @Tool(description = "Get current weather information for a specific location in Taiwan. "
            + "Supports major cities and counties like Taipei, Taoyuan, Taichung, Tainan, Kaohsiung, etc.")
    public WeatherInfo getCurrentWeather(String location) {
        try {
            log.info("查詢天氣資訊：{}", location);

            String url = baseUrl + "/O-A0003-001?Authorization=" + apiKey;
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);

            if (response == null || !response.has("records")) {
                return WeatherInfo.error("無法取得天氣資料");
            }

            JsonNode records = response.get("records");
            JsonNode locations = records.get("location");

            for (JsonNode locationNode : locations) {
                String locationName = locationNode.get("locationName").asText();

                if (locationName.contains(location) || location.contains(locationName)) {
                    WeatherInfo result = parseWeatherData(locationNode, locationName);
                    log.info("成功取得 {} 的天氣資訊", locationName);
                    return result;
                }
            }

            return WeatherInfo.notFound("找不到 " + location + " 的天氣資料");
        } catch (Exception e) {
            log.error("取得天氣資料時發生錯誤: {}", e.getMessage(), e);
            return WeatherInfo.error("取得天氣資料失敗：" + e.getMessage());
        }
    }

    /**
     * 取得全台各地的溫度排行榜
     *
     * @param topCount 前幾名
     * @return 溫度排行
     */
    @Tool(description = "Get temperature ranking for all locations in Taiwan. "
            + "Returns top N locations sorted by temperature.")
    public TemperatureRanking getTemperatureRanking(Integer topCount) {
        int limit = (topCount == null || topCount <= 0) ? 10 : topCount;

        try {
            log.info("查詢溫度排行榜：前 {} 名", limit);

            String url = baseUrl + "/O-A0003-001?Authorization=" + apiKey;
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);

            if (response == null || !response.has("records")) {
                return TemperatureRanking.error("無法取得天氣資料");
            }

            List<LocationTemperature> temperatures = new ArrayList<>();
            JsonNode locations = response.get("records").get("location");

            for (JsonNode locationNode : locations) {
                String locationName = locationNode.get("locationName").asText();
                JsonNode weatherElements = locationNode.get("weatherElement");

                Double temp = extractTemperature(weatherElements);
                if (temp != null) {
                    temperatures.add(new LocationTemperature(locationName, temp));
                }
            }

            List<LocationTemperature> topTemperatures = temperatures.stream()
                    .sorted((a, b) -> Double.compare(b.temperature(), a.temperature()))
                    .limit(limit)
                    .toList();

            return new TemperatureRanking(
                    topTemperatures,
                    true,
                    null,
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            );
        } catch (Exception e) {
            log.error("取得溫度排行時發生錯誤: {}", e.getMessage(), e);
            return TemperatureRanking.error("取得溫度排行失敗：" + e.getMessage());
        }
    }

    /**
     * 解析單一地點天氣資料
     *
     * @param locationNode 地點節點
     * @param locationName 地點名稱
     * @return 天氣資訊
     */
    private WeatherInfo parseWeatherData(JsonNode locationNode, String locationName) {
        JsonNode weatherElements = locationNode.get("weatherElement");

        Double temperature = extractTemperature(weatherElements);
        Double humidity = extractHumidity(weatherElements);
        String weather = extractWeatherDescription(weatherElements);
        Double rainfall = extractRainfall(weatherElements);
        String windDirection = extractWindDirection(weatherElements);
        Double windSpeed = extractWindSpeed(weatherElements);

        return new WeatherInfo(
                locationName,
                temperature,
                humidity,
                weather,
                rainfall,
                windDirection,
                windSpeed,
                true,
                null,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
    }

    /**
     * 取得溫度
     *
     * @param weatherElements 氣象元素
     * @return 溫度
     */
    private Double extractTemperature(JsonNode weatherElements) {
        return extractElementValue(weatherElements, "TEMP");
    }

    /**
     * 取得濕度
     *
     * @param weatherElements 氣象元素
     * @return 濕度
     */
    private Double extractHumidity(JsonNode weatherElements) {
        return extractElementValue(weatherElements, "HUMD");
    }

    /**
     * 取得降雨量
     *
     * @param weatherElements 氣象元素
     * @return 降雨量
     */
    private Double extractRainfall(JsonNode weatherElements) {
        return extractElementValue(weatherElements, "24R");
    }

    /**
     * 取得風速
     *
     * @param weatherElements 氣象元素
     * @return 風速
     */
    private Double extractWindSpeed(JsonNode weatherElements) {
        return extractElementValue(weatherElements, "WDSD");
    }

    /**
     * 取得天氣描述
     *
     * @param weatherElements 氣象元素
     * @return 天氣描述
     */
    private String extractWeatherDescription(JsonNode weatherElements) {
        JsonNode element = findWeatherElement(weatherElements, "Weather");
        if (element != null && element.has("elementValue")) {
            return element.get("elementValue").asText();
        }
        return null;
    }

    /**
     * 取得風向
     *
     * @param weatherElements 氣象元素
     * @return 風向描述
     */
    private String extractWindDirection(JsonNode weatherElements) {
        JsonNode element = findWeatherElement(weatherElements, "WDIR");
        if (element != null && element.has("elementValue")) {
            return element.get("elementValue").asText();
        }
        return null;
    }

    /**
     * 取得氣象元素數值
     *
     * @param weatherElements 氣象元素
     * @param elementName     元素名稱
     * @return 數值
     */
    private Double extractElementValue(JsonNode weatherElements, String elementName) {
        JsonNode element = findWeatherElement(weatherElements, elementName);
        if (element != null && element.has("elementValue")) {
            try {
                return element.get("elementValue").asDouble();
            } catch (Exception e) {
                log.warn("無法解析 {} 的數值", elementName);
            }
        }
        return null;
    }

    /**
     * 尋找指定氣象元素
     *
     * @param weatherElements 氣象元素
     * @param elementName     元素名稱
     * @return 元素節點
     */
    private JsonNode findWeatherElement(JsonNode weatherElements, String elementName) {
        if (weatherElements != null && weatherElements.isArray()) {
            for (JsonNode element : weatherElements) {
                if (element.has("elementName")
                        && elementName.equals(element.get("elementName").asText())) {
                    return element;
                }
            }
        }
        return null;
    }
}
