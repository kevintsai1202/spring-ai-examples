package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 產品推薦列表
 * 用於表示一組推薦的產品
 */
public record ProductRecommendations(
        @JsonProperty("category") String category,
        @JsonProperty("count") int count,
        @JsonProperty("products") List<ProductItem> products,
        @JsonProperty("error") String error) {
    /**
     * 便捷建構子（不包含error欄位）
     */
    public ProductRecommendations(String category, int count, List<ProductItem> products) {
        this(category, count, products, null);
    }
}

/**
 * 產品項目
 */
record ProductItem(
        @JsonProperty("name") String name,
        @JsonProperty("brand") String brand,
        @JsonProperty("price") double price,
        @JsonProperty("rating") double rating,
        @JsonProperty("description") String description) {
}
