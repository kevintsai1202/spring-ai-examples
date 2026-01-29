package com.example.controller;

import com.example.dto.ProductAnalysisRequest;
import com.example.dto.ProductAnalysisResponse;
import com.example.tools.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Tool 工具鏈呼叫控制器
 * 實現複雜的工具鏈呼叫功能（5.8 章節）
 */
@RestController
@RequestMapping("/api/tool-chain")
@RequiredArgsConstructor
@Slf4j
public class ToolChainController {

    private final ChatModel chatModel;
    private final ProductDetailsTools productDetailsTools;
    private final EnhancedSalesTools enhancedSalesTools;
    private final DateTimeTools dateTimeTools;
    private final CalculatorTools calculatorTools;

    /**
     * 複雜工具鏈查詢
     * 
     * @param prompt 自然語言查詢
     * @return AI 分析結果
     */
    @GetMapping("/complex-query")
    public String complexQuery(@RequestParam String prompt) {
        try {
            log.info("收到複雜工具鏈查詢：{}", prompt);

            long startTime = System.currentTimeMillis();

            // 建立支援工具鏈的 ChatClient
            ChatClient toolChainClient = ChatClient.create(chatModel);

            String response = toolChainClient
                    .prompt(prompt)
                    .call()
                    .content();

            long endTime = System.currentTimeMillis();

            log.info("複雜查詢完成，耗時：{}ms，回應長度：{} 字元",
                    endTime - startTime, response.length());

            return response;

        } catch (Exception e) {
            log.error("複雜工具鏈查詢失敗：{}", prompt, e);
            return "查詢失敗：" + e.getMessage();
        }
    }

    /**
     * 產品深度分析
     * 
     * @param request 分析請求
     * @return 詳細分析報告
     */
    @PostMapping("/product-analysis")
    public ProductAnalysisResponse productAnalysis(@RequestBody ProductAnalysisRequest request) {
        try {
            log.info("產品深度分析請求：{}", request);

            long startTime = System.currentTimeMillis();

            // 構建分析提示詞
            String prompt = buildAnalysisPrompt(request);

            // 建立支援工具鏈的 ChatClient
            ChatClient analysisClient = ChatClient.create(chatModel);

            String analysis = analysisClient
                    .prompt(prompt)
                    .call()
                    .content();

            long endTime = System.currentTimeMillis();

            return ProductAnalysisResponse.builder()
                    .success(true)
                    .productCode(request.getProductCode())
                    .analysisType(request.getAnalysisType())
                    .analysis(analysis)
                    .executionTime(endTime - startTime)
                    .timestamp(LocalDateTime.now())
                    .build();

        } catch (Exception e) {
            log.error("產品深度分析失敗", e);

            return ProductAnalysisResponse.builder()
                    .success(false)
                    .productCode(request.getProductCode())
                    .error(e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
        }
    }

    /**
     * 工具鏈執行追蹤
     * 
     * @param prompt 查询内容
     * @return 執行追蹤資訊
     */
    @GetMapping("/trace")
    public Map<String, Object> traceExecution(@RequestParam String prompt) {
        try {
            log.info("工具鏈執行追蹤：{}", prompt);

            long startTime = System.currentTimeMillis();

            // 建立支援工具鏈的 ChatClient
            ChatClient traceClient = ChatClient.create(chatModel);

            // 這裡可以添加工具呼叫追蹤邏輯
            String response = traceClient
                    .prompt(prompt)
                    .call()
                    .content();

            long endTime = System.currentTimeMillis();

            return Map.of(
                    "prompt", prompt,
                    "response", response,
                    "executionTime", endTime - startTime,
                    "toolsUsed", List.of("EnhancedSalesTools", "ProductDetailsTools"),
                    "timestamp", LocalDateTime.now());

        } catch (Exception e) {
            log.error("工具鏈執行追蹤失敗", e);
            return Map.of(
                    "error", e.getMessage(),
                    "timestamp", LocalDateTime.now());
        }
    }

    /**
     * 銷售排行分析
     * 場景 1：查詢最熱銷產品及其詳細資訊
     */
    @GetMapping("/best-seller-analysis")
    public String bestSellerAnalysis(@RequestParam(defaultValue = "2024") int year) {
        try {
            String prompt = String.format(
                    "請分析 %d 年最熱銷的產品，並列出該產品所有型號的詳細規格和價格。" +
                            "請提供完整的產品分析報告，包括銷售數據、產品線覆蓋和商業建議。",
                    year);

            log.info("最熱銷產品分析：{}", year);
            return complexQuery(prompt);

        } catch (Exception e) {
            log.error("最熱銷產品分析失敗", e);
            return "分析失敗：" + e.getMessage();
        }
    }

    /**
     * 競品比較分析
     * 場景 2：比較多個產品的銷售表現
     */
    @GetMapping("/competitor-analysis")
    public String competitorAnalysis(
            @RequestParam(defaultValue = "2024") int year,
            @RequestParam(defaultValue = "PD-1405,PD-1234") String products) {
        try {
            String productList = products.replace(",", "、");
            String prompt = String.format(
                    "請比較 %d 年 %s 這些產品的銷售表現，並分析各自的產品優勢。" +
                            "請提供詳細的對比分析、市場佔有率評估和競爭優勢分析。",
                    year, productList);

            log.info("競品分析：{}年，產品：{}", year, products);
            return complexQuery(prompt);

        } catch (Exception e) {
            log.error("競品分析失敗", e);
            return "分析失敗：" + e.getMessage();
        }
    }

    /**
     * 產品線優化建議
     * 查詢產品詳情並提供優化建議
     */
    @GetMapping("/product-optimization")
    public String productOptimization(@RequestParam String productCode) {
        try {
            String prompt = String.format(
                    "請詳細分析產品代碼 %s 的產品線。" +
                            "1. 列出所有型號及規格\n" +
                            "2. 分析價格分層\n" +
                            "3. 評估產品線完整性\n" +
                            "4. 提供優化建議",
                    productCode);

            log.info("產品線優化分析：{}", productCode);
            return complexQuery(prompt);

        } catch (Exception e) {
            log.error("產品線優化分析失敗", e);
            return "分析失敗：" + e.getMessage();
        }
    }

    /**
     * 市場佔有率分析
     */
    @GetMapping("/market-share-analysis")
    public String marketShareAnalysis(@RequestParam(defaultValue = "2024") int year) {
        try {
            String prompt = String.format(
                    "請進行 %d 年的市场佔有率分析。\n" +
                            "1. 列出各產品的銷售量和營收\n" +
                            "2. 計算市場佔有率\n" +
                            "3. 分析市場分佈\n" +
                            "4. 提供市場趨勢見解",
                    year);

            log.info("市場佔有率分析：{}", year);
            return complexQuery(prompt);

        } catch (Exception e) {
            log.error("市場佔有率分析失敗", e);
            return "分析失敗：" + e.getMessage();
        }
    }

    private String buildAnalysisPrompt(ProductAnalysisRequest request) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("請進行深度產品分析：\n");
        prompt.append("產品代碼：").append(request.getProductCode()).append("\n");
        prompt.append("分析類型：").append(request.getAnalysisType()).append("\n");

        if (request.getYear() != null) {
            prompt.append("分析年份：").append(request.getYear()).append("\n");
        }

        prompt.append("\n請提供：\n");

        switch (request.getAnalysisType()) {
            case "comprehensive" -> {
                prompt.append("1. 產品銷售表現分析\n");
                prompt.append("2. 所有產品型號詳細資訊\n");
                prompt.append("3. 市場競爭力評估\n");
                prompt.append("4. 改進建議和策略\n");
            }
            case "sales" -> {
                prompt.append("1. 詳細銷售數據分析\n");
                prompt.append("2. 銷售趨勢和模式\n");
                prompt.append("3. 市場佔有率分析\n");
            }
            case "product" -> {
                prompt.append("1. 產品型號完整資訊\n");
                prompt.append("2. 規格和定價分析\n");
                prompt.append("3. 產品線優化建議\n");
            }
            default -> prompt.append("1. 全面產品分析\n");
        }

        return prompt.toString();
    }
}
