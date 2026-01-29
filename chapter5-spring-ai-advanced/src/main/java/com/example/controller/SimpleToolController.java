package com.example.controller;

import com.example.tools.DateTimeTools;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 基礎工具調用示例控制器
 */
@RestController
@RequestMapping("/api/simple-tool")
@RequiredArgsConstructor
public class SimpleToolController {

    /**
     * 重要物件：AI 模型
     */
    private final ChatModel chatModel;

    /**
     * 重要物件：時間工具
     */
    private final DateTimeTools dateTimeTools;

    /**
     * 基礎工具調用示例
     *
     * @param message 使用者問題
     * @return AI 回應
     */
    @GetMapping("/chat")
    public String chat(@RequestParam String message) {
        return ChatClient.create(chatModel)
                .prompt(message)
                .tools(dateTimeTools)
                .call()
                .content();
    }
}
