package com.example.config;

import com.example.tools.CalculatorTools;
import com.example.tools.DateTimeTools;
import com.example.tools.ProductSalesTools;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 企業資料分析 ChatClient 配置
 */
@Configuration
@RequiredArgsConstructor
public class AiConfig {

    /**
     * 重要物件：產品銷售工具
     */
    private final ProductSalesTools productSalesTools;

    /**
     * 重要物件：日期時間工具
     */
    private final DateTimeTools dateTimeTools;

    /**
     * 重要物件：計算工具
     */
    private final CalculatorTools calculatorTools;

    /**
     * 建立企業分析用 ChatClient
     *
     * @param chatModel AI 模型
     * @return ChatClient
     */
    @Bean
    public ChatClient enterpriseChatClient(ChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultTools(productSalesTools, dateTimeTools, calculatorTools)
                .defaultSystem(String.join("\n",
                        "你是專業的企業資料分析師。",
                        "",
                        "當用戶詢問企業資料時:",
                        "1. 使用適當的工具獲取最新資料",
                        "2. 進行深入的資料分析",
                        "3. 提供清晰的洞察和建議",
                        "",
                        "可用工具:",
                        "- 產品銷售資料查詢",
                        "- 日期時間查詢",
                        "- 數學計算"
                ))
                .build();
    }
}
