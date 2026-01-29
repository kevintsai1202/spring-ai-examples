package com.example.tools;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

/**
 * 日期時間相關工具
 */
@Component
public class DateTimeTools {

    /**
     * 取得台灣目前日期時間
     *
     * @return 台灣時區日期時間字串
     */
    @Tool(description = "Get the current date and time in Taiwan (Asia/Taipei timezone)")
    public String getCurrentDateTime() {
        // 重要物件：台灣時區
        ZoneId zoneId = ZoneId.of("Asia/Taipei");
        ZonedDateTime now = ZonedDateTime.now(zoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss");

        return String.format("當前時間：%s（台灣時間）", now.format(formatter));
    }

    /**
     * 依指定時區取得目前日期時間
     *
     * @param timezone 時區代碼（例如 Asia/Taipei）
     * @return 指定時區日期時間字串
     */
    @Tool(description = "Get the current date and time in specified timezone, "
            + "e.g. 'Asia/Taipei', 'America/New_York', 'Europe/London'.")
    public String getCurrentDateTimeByZone(String timezone) {
        try {
            // 重要物件：使用者指定時區
            ZoneId zoneId = ZoneId.of(timezone);
            ZonedDateTime now = ZonedDateTime.now(zoneId);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");

            return now.format(formatter);
        } catch (Exception e) {
            return "Invalid timezone: " + timezone;
        }
    }
}
