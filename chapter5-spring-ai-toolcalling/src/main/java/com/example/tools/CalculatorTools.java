package com.example.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

/**
 * 基礎計算工具
 */
@Component
public class CalculatorTools {

    /**
     * 執行基礎數學運算
     *
     * @param operation 運算類型（add/subtract/multiply/divide）
     * @param a         參數一
     * @param b         參數二
     * @return 運算結果字串
     */
    @Tool(description = "Perform basic math operations. "
            + "Operations: add, subtract, multiply, divide. "
            + "Example: calculate('add', 10, 5) returns 15")
    public String calculate(String operation, double a, double b) {
        // 重要變數：運算結果
        double result = switch (operation.toLowerCase()) {
            case "add" -> a + b;
            case "subtract" -> a - b;
            case "multiply" -> a * b;
            case "divide" -> {
                if (b == 0) {
                    yield Double.NaN;
                }
                yield a / b;
            }
            default -> throw new IllegalArgumentException("不支援的運算: " + operation);
        };

        if (Double.isNaN(result)) {
            return "錯誤：除數不能為零";
        }

        String symbol = getSymbol(operation);
        return String.format("%.2f %s %.2f = %.2f", a, symbol, b, result);
    }

    /**
     * 取得運算符號
     *
     * @param operation 運算類型
     * @return 對應符號
     */
    private String getSymbol(String operation) {
        return switch (operation.toLowerCase()) {
            case "add" -> "+";
            case "subtract" -> "-";
            case "multiply" -> "×";
            case "divide" -> "÷";
            default -> "?";
        };
    }
}
