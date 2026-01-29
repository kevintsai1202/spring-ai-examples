package com.example.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 產品詳情工具
 */
@Component
@Slf4j
public class ProductDetailsTools {

    /**
     * 產品詳情資料模型
     */
    public record ProductDetail(
            String product,
            String productName,
            List<String> models,
            String category,
            String description
    ) {
    }

    /**
     * 產品型號規格資料模型
     */
    public record ProductModel(
            String modelCode,
            String modelName,
            String specifications,
            String price,
            String availability
    ) {
    }

    /**
     * 根據產品代碼獲取產品型號列表
     *
     * @param product 產品代碼或名稱
     * @return 產品詳情
     */
    @Tool(description = "Get detailed product information including all available models by product code or name. "
            + "Returns comprehensive product details with model specifications.")
    public ProductDetail getProductModels(String product) {
        log.info("查詢產品型號：{}", product);

        // 重要物件：模擬產品資料庫
        Map<String, ProductDetail> productDatabase = Map.of(
                "PD-1405", new ProductDetail(
                        "PD-1405",
                        "智能手機 Pro 系列",
                        List.of("1405-001", "1405-002", "1405-003", "1405-004"),
                        "智能手機",
                        "高端智能手機系列，支援 5G 和 AI 攝影"
                ),
                "PD-1234", new ProductDetail(
                        "PD-1234",
                        "筆記型電腦 Ultra 系列",
                        List.of("1234-1", "1234-2", "1234-3", "1234-4", "1234-5"),
                        "筆記型電腦",
                        "輕薄高效能筆記型電腦，適合商務和創作"
                ),
                "PD-1385", new ProductDetail(
                        "PD-1385",
                        "智能手錶系列",
                        List.of("1385-1", "1385-2", "1385-3", "1385-4"),
                        "穿戴裝置",
                        "健康與運動導向的智能手錶"
                )
        );

        return productDatabase.getOrDefault(product,
                new ProductDetail(product, "未知產品", List.of(), "未分類", "查無此產品"));
    }

    /**
     * 獲取特定型號的詳細規格
     *
     * @param productCode 產品代碼
     * @param modelCode   型號代碼
     * @return 產品型號規格
     */
    @Tool(description = "Get detailed specifications for a specific product model. "
            + "Returns comprehensive model information including price and availability.")
    public ProductModel getModelSpecifications(String productCode, String modelCode) {
        log.info("查詢型號規格：{}-{}", productCode, modelCode);

        return switch (productCode) {
            case "PD-1405" -> new ProductModel(
                    modelCode,
                    "智能手機 Pro " + modelCode,
                    generatePhoneSpecs(modelCode),
                    generatePrice("phone", modelCode),
                    "現貨供應"
            );
            case "PD-1234" -> new ProductModel(
                    modelCode,
                    "筆記型電腦 Ultra " + modelCode,
                    generateLaptopSpecs(modelCode),
                    generatePrice("laptop", modelCode),
                    "現貨供應"
            );
            case "PD-1385" -> new ProductModel(
                    modelCode,
                    "智能手錶 " + modelCode,
                    generateWatchSpecs(modelCode),
                    generatePrice("watch", modelCode),
                    "現貨供應"
            );
            default -> new ProductModel(
                    modelCode,
                    "產品型號 " + modelCode,
                    "標準規格",
                    "價格面議",
                    "請洽詢"
            );
        };
    }

    /**
     * 產生手機規格
     *
     * @param modelCode 型號
     * @return 規格描述
     */
    private String generatePhoneSpecs(String modelCode) {
        return switch (modelCode) {
            case "1405-001" -> "6.1吋 OLED 螢幕, 128GB 儲存, 雙鏡頭";
            case "1405-002" -> "6.1吋 OLED 螢幕, 256GB 儲存, 三鏡頭";
            case "1405-003" -> "6.7吋 OLED 螢幕, 512GB 儲存, 三鏡頭 Pro";
            case "1405-004" -> "6.7吋 OLED 螢幕, 1TB 儲存, 四鏡頭 Pro Max";
            default -> "標準智能手機規格";
        };
    }

    /**
     * 產生筆電規格
     *
     * @param modelCode 型號
     * @return 規格描述
     */
    private String generateLaptopSpecs(String modelCode) {
        return switch (modelCode) {
            case "1234-1" -> "13吋, Intel i5, 8GB RAM, 256GB SSD";
            case "1234-2" -> "13吋, Intel i7, 16GB RAM, 512GB SSD";
            case "1234-3" -> "15吋, Intel i7, 16GB RAM, 1TB SSD";
            default -> "標準筆記型電腦規格";
        };
    }

    /**
     * 產生手錶規格
     *
     * @param modelCode 型號
     * @return 規格描述
     */
    private String generateWatchSpecs(String modelCode) {
        return switch (modelCode) {
            case "1385-1" -> "41mm, GPS, 心率監測";
            case "1385-2" -> "45mm, GPS, 心率+血氧";
            case "1385-3" -> "41mm, GPS+Cellular";
            case "1385-4" -> "45mm, 完整功能";
            default -> "標準智能手錶規格";
        };
    }

    /**
     * 產生產品價格
     *
     * @param category  類別
     * @param modelCode 型號
     * @return 價格字串
     */
    private String generatePrice(String category, String modelCode) {
        return switch (category) {
            case "phone" -> switch (modelCode) {
                case "1405-001" -> "NT$ 25,900";
                case "1405-002" -> "NT$ 29,900";
                case "1405-003" -> "NT$ 35,900";
                default -> "NT$ 25,000";
            };
            case "laptop" -> switch (modelCode) {
                case "1234-1" -> "NT$ 35,900";
                case "1234-2" -> "NT$ 45,900";
                case "1234-3" -> "NT$ 55,900";
                default -> "NT$ 40,000";
            };
            case "watch" -> switch (modelCode) {
                case "1385-1" -> "NT$ 12,900";
                case "1385-2" -> "NT$ 15,900";
                case "1385-3" -> "NT$ 18,900";
                case "1385-4" -> "NT$ 22,900";
                default -> "NT$ 12,900";
            };
            default -> "價格面議";
        };
    }
}
