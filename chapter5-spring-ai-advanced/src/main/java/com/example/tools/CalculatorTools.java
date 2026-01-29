/**
 * 計算機工具類別
 * 提供基本的數學運算以解決 AI 計算準確性問題
 */
package com.example.tools;

import org.springframework.stereotype.Component;

@Component
public class CalculatorTools {

    /**
     * 基本數學運算
     * 
     * @param operation 運算類型 (add, subtract, multiply, divide)
     * @param a         第一個數字
     * @param b         第二個數字
     * @return 運算結果
     */
    public String calculate(String operation, double a, double b) {
        try {
            double result;

            if ("add".equalsIgnoreCase(operation)) {
                result = a + b;
            } else if ("subtract".equalsIgnoreCase(operation)) {
                result = a - b;
            } else if ("multiply".equalsIgnoreCase(operation)) {
                result = a * b;
            } else if ("divide".equalsIgnoreCase(operation)) {
                if (b == 0) {
                    return "錯誤: 不能除以零";
                }
                result = a / b;
            } else {
                return "不支援的運算: " + operation;
            }

            return String.format("%.2f %s %.2f = %.2f", a, getOperationSymbol(operation), b, result);

        } catch (Exception e) {
            return "計算錯誤: " + e.getMessage();
        }
    }

    /**
     * 複雜數學運算式
     * 
     * @param expression 數學運算式 (例如: "2 + 3 * 4")
     * @return 計算結果
     */
    public String evaluateExpression(String expression) {
        try {
            // 可以整合數學運算式解析器
            // 目前僅為佔位符
            return "運算式評估 '" + expression + "' 開發中";
        } catch (Exception e) {
            return "運算式評估錯誤: " + e.getMessage();
        }
    }

    private String getOperationSymbol(String operation) {
        if ("add".equalsIgnoreCase(operation)) {
            return "+";
        } else if ("subtract".equalsIgnoreCase(operation)) {
            return "-";
        } else if ("multiply".equalsIgnoreCase(operation)) {
            return "*";
        } else if ("divide".equalsIgnoreCase(operation)) {
            return "/";
        } else {
            return "?";
        }
    }
}
