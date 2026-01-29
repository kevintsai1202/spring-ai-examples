package com.example.config;

import com.example.tools.CalculatorTools;
import com.example.tools.DateTimeTools;
import com.example.tools.EnhancedSalesTools;
import com.example.tools.ProductDetailsTools;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 工具鏈 ChatClient 配置
 */
@Configuration
@RequiredArgsConstructor
public class AdvancedAiConfig {

    /**
     * 重要物件：產品詳情工具
     */
    private final ProductDetailsTools productDetailsTools;

    /**
     * 重要物件：進階銷售工具
     */
    private final EnhancedSalesTools enhancedSalesTools;

    /**
     * 重要物件：日期時間工具
     */
    private final DateTimeTools dateTimeTools;

    /**
     * 重要物件：計算工具
     */
    private final CalculatorTools calculatorTools;

    /**
     * 建立工具鏈 ChatClient
     *
     * @param chatModel AI 模型
     * @return ChatClient
     */
    @Bean
    public ChatClient toolChainChatClient(ChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultTools(
                        enhancedSalesTools,
                        productDetailsTools,
                        calculatorTools,
                        dateTimeTools
                )
                .defaultSystem(String.join("\n",
                        "你是一個專業的企業資料分析專家，擅長使用多種工具進行深度分析。",
                        "",
                        "工具使用策略：",
                        "1. 根據用戶查詢自動選擇合適的工具組合",
                        "2. 按邏輯順序調用工具（先獲取基礎資料，再進行詳細分析）",
                        "3. 充分利用工具鏈的優勢，避免一次性獲取過多資料",
                        "4. 提供深入的分析洞察和商業建議",
                        "",
                        "回答風格：",
                        "- 使用專業但易懂的語言",
                        "- 提供結構化的分析結果",
                        "- 包含具體的數據和洞察",
                        "- 給出實用的商業建議"
                ))
                .build();
    }
}
