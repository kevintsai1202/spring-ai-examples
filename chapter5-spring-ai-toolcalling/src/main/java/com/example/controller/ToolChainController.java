package com.example.controller;

import com.example.model.ProductAnalysisRequest;
import com.example.model.ProductAnalysisResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 工具鏈控制器
 */
@RestController
@RequestMapping("/api/tool-chain")
@RequiredArgsConstructor
@Slf4j
public class ToolChainController {

    /**
     * 重要物件：工具鏈 ChatClient
     */
    @Qualifier("toolChainChatClient")
    private final ChatClient toolChainChatClient;

    /**
     * 複雜工具鏈查詢
     *
     * @param prompt 使用者問題
     * @return AI 回應
     */
    @GetMapping("/complex-query")
    public String complexQuery(@RequestParam String prompt) {
        log.info("收到複雜工具鏈查詢：{}", prompt);

        // 重要變數：開始時間
        long startTime = System.currentTimeMillis();

        String response = toolChainChatClient
                .prompt(prompt)
                .call()
                .content();

        long endTime = System.currentTimeMillis();
        log.info("複雜查詢完成，耗時：{}ms", endTime - startTime);

        return response;
    }

    /**
     * 產品深度分析
     *
     * @param request 分析請求
     * @return 分析回應
     */
    @PostMapping("/product-analysis")
    public ProductAnalysisResponse productAnalysis(@RequestBody ProductAnalysisRequest request) {
        log.info("產品深度分析請求：{}", request);

        // 重要變數：開始時間
        long startTime = System.currentTimeMillis();

        String prompt = buildAnalysisPrompt(request);
        String analysis = toolChainChatClient
                .prompt(prompt)
                .call()
                .content();

        long endTime = System.currentTimeMillis();

        return new ProductAnalysisResponse(
                true,
                request.productCode(),
                request.analysisType(),
                analysis,
                endTime - startTime,
                LocalDateTime.now()
        );
    }

    /**
     * 建立分析提示詞
     *
     * @param request 分析請求
     * @return 提示詞
     */
    private String buildAnalysisPrompt(ProductAnalysisRequest request) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("請進行深度產品分析：\n");
        prompt.append("產品代碼：").append(request.productCode()).append("\n");
        prompt.append("分析類型：").append(request.analysisType()).append("\n");

        if (request.year() != null) {
            prompt.append("分析年份：").append(request.year()).append("\n");
        }

        return prompt.toString();
    }
}
