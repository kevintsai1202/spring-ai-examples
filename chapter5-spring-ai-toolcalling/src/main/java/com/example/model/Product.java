package com.example.model;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 產品銷售資料
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonClassDescription("產品銷售資料")
public class Product {

    @JsonProperty("year")
    @JsonPropertyDescription("銷售年份")
    private String year;

    @JsonProperty("model")
    @JsonPropertyDescription("產品型號")
    private String model;

    @JsonProperty("name")
    @JsonPropertyDescription("產品名稱")
    private String name;

    @JsonProperty("quantity")
    @JsonPropertyDescription("銷售數量")
    private Integer quantity;

    @JsonProperty("revenue")
    @JsonPropertyDescription("銷售金額")
    private BigDecimal revenue;

    @JsonProperty("category")
    @JsonPropertyDescription("產品類別")
    private String category;
}
