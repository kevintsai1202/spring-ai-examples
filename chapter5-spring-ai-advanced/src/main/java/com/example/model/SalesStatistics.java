package com.example.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 銷售統計分析結果
 */
@Data
@Builder
public class SalesStatistics {

    private List<Product> topProducts;
    private List<CategoryStat> categoryStats;
}
