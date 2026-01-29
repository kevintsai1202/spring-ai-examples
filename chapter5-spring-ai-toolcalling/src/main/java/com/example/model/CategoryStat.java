package com.example.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 產品分類統計
 */
@Data
@Builder
public class CategoryStat {

    private String category;
    private int quantity;
    private BigDecimal revenue;
    private double percentage;
}
