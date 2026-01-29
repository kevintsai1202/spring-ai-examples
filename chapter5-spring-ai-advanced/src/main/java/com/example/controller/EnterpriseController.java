package com.example.controller;

import com.example.model.AssistantRequest;
import com.example.model.AssistantResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 企業資料分析控制器
 */
@RestController
@RequestMapping("/api/enterprise")
@RequiredArgsConstructor
@Slf4j
public class EnterpriseController {

    /**
     * 重要物件：企業分析 ChatClient
     */
    @Qualifier("enterpriseChatClient")
    private final ChatClient enterpriseChatClient;

    /**
     * 企業資料分析對話
     *
     * @param request 使用者請求
     * @return 分析回應
     */
    @PostMapping("/chat")
    public AssistantResponse chat(@RequestBody AssistantRequest request) {
        log.info("收到企業分析問題: {}", request.message());

        // 重要變數：開始時間
        long startTime = System.currentTimeMillis();

        String response = enterpriseChatClient
                .prompt(request.message())
                .call()
                .content();

        long executionTime = System.currentTimeMillis() - startTime;

        return new AssistantResponse(
                true,
                request.message(),
                response,
                executionTime,
                LocalDateTime.now()
        );
    }
}
