package com.example.model;

import java.time.LocalDateTime;

/**
 * 智能助手回應資料
 */
public record AssistantResponse(
        boolean success,
        String question,
        String answer,
        long executionTime,
        LocalDateTime timestamp
) {
}
