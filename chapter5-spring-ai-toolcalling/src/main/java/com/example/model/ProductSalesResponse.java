package com.example.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 產品銷售查詢回應
 */
@Data
@Builder
public class ProductSalesResponse {

    private List<Product> products;
    private int totalQuantity;
    private BigDecimal totalRevenue;
    private String queryYear;
    private String queryProduct;
}
