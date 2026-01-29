package com.example.controller;

import com.example.model.AssistantRequest;
import com.example.model.AssistantResponse;
import com.example.tools.CalculatorTools;
import com.example.tools.DateTimeTools;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 智能助手控制器
 */
@RestController
@RequestMapping("/api/assistant")
@RequiredArgsConstructor
@Slf4j
public class SmartAssistantController {

    /**
     * 重要物件：AI 模型
     */
    private final ChatModel chatModel;

    /**
     * 重要物件：時間工具
     */
    private final DateTimeTools dateTimeTools;

    /**
     * 重要物件：計算工具
     */
    private final CalculatorTools calculatorTools;

    /**
     * 智能助手對話
     *
     * @param request 使用者請求
     * @return 助手回應
     */
    @PostMapping("/chat")
    public AssistantResponse chat(@RequestBody AssistantRequest request) {
        log.info("收到問題: {}", request.message());

        // 重要變數：開始時間
        long startTime = System.currentTimeMillis();

        String response = ChatClient.create(chatModel)
                .prompt(request.message())
                .tools(dateTimeTools, calculatorTools)
                .call()
                .content();

        long executionTime = System.currentTimeMillis() - startTime;
        log.info("回應完成，耗時: {}ms", executionTime);

        return new AssistantResponse(
                true,
                request.message(),
                response,
                executionTime,
                LocalDateTime.now()
        );
    }
}
