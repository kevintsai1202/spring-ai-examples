package com.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Jackson 配置
 * 處理 OpenAI API 新增欄位相容性問題
 * OpenAI 最近在 API 回應中添加了新的 "annotations" 欄位
 * Spring AI 1.0.0-M4 的 ChatCompletionMessage 類別未配置忽略未知欄位
 * 此配置使 Jackson 能夠忽略未知欄位，解決相容性問題
 */
@Configuration
public class JacksonConfig {

    /**
     * 配置 ObjectMapper bean
     * 設置 Jackson 忽略反序列化時的未知欄位
     * 這允許 API 回應包含新欄位而不會導致異常
     *
     * @return 配置好的 ObjectMapper
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // 忽略未知欄位 - 這是關鍵配置
        // 允許 OpenAI API 回應包含新的 "annotations" 欄位而不會失敗
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 其他有用的配置
        mapper.findAndRegisterModules();

        return mapper;
    }
}
