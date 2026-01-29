package com.example.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

/**
 * 進階銷售分析工具
 */
@Component
@Slf4j
public class EnhancedSalesTools {

    /**
     * 產品銷售資料模型
     */
    public record ProductSales(
            String product,
            String productName,
            int salesVolume,
            BigDecimal revenue,
            String category,
            double marketShare
    ) {
    }

    /**
     * 銷售排行資料模型
     */
    public record SalesRanking(
            List<ProductSales> topProducts,
            ProductSales bestSeller,
            int totalVolume,
            BigDecimal totalRevenue,
            String analysisYear
    ) {
    }

    /**
     * 獲取指定年份的產品銷售排行
     *
     * @param year 年份
     * @return 銷售排行
     */
    @Tool(description = "Get comprehensive product sales data for a specific year. "
            + "Returns detailed sales information including volume, revenue, and market share.")
    public SalesRanking getProductSalesRanking(int year) {
        log.info("查詢年度銷售排行：{}", year);

        List<ProductSales> salesData = getSalesDataByYear(year);

        if (salesData.isEmpty()) {
            return new SalesRanking(List.of(), null, 0, BigDecimal.ZERO, String.valueOf(year));
        }

        int totalVolume = salesData.stream()
                .mapToInt(ProductSales::salesVolume)
                .sum();

        BigDecimal totalRevenue = salesData.stream()
                .map(ProductSales::revenue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<ProductSales> topProducts = salesData.stream()
                .sorted((p1, p2) -> Integer.compare(p2.salesVolume(), p1.salesVolume()))
                .toList();

        ProductSales bestSeller = topProducts.get(0);

        return new SalesRanking(
                topProducts,
                bestSeller,
                totalVolume,
                totalRevenue,
                String.valueOf(year)
        );
    }

    /**
     * 比較不同產品的銷售表現
     *
     * @param year     年份
     * @param products 產品清單
     * @return 比較結果文字
     */
    @Tool(description = "Compare sales performance between multiple products in a specific year. "
            + "Returns detailed comparison with rankings and performance metrics.")
    public String compareProductPerformance(int year, List<String> products) {
        log.info("比較產品銷售表現：{}年，產品：{}", year, products);

        List<ProductSales> allSales = getSalesDataByYear(year);

        List<ProductSales> targetProducts = allSales.stream()
                .filter(sale -> products.contains(sale.product()))
                .sorted((p1, p2) -> Integer.compare(p2.salesVolume(), p1.salesVolume()))
                .toList();

        if (targetProducts.isEmpty()) {
            return "未找到指定產品的銷售資料";
        }

        StringBuilder comparison = new StringBuilder();
        comparison.append(String.format("📊 %d年產品銷售表現比較\n\n", year));

        for (int i = 0; i < targetProducts.size(); i++) {
            ProductSales product = targetProducts.get(i);
            comparison.append(String.format(
                    "%d. %s (%s)\n"
                            + "   銷售量：%,d 台\n"
                            + "   營收：%s\n"
                            + "   市場占有率：%.2f%%\n\n",
                    i + 1,
                    product.productName(),
                    product.product(),
                    product.salesVolume(),
                    formatCurrency(product.revenue()),
                    product.marketShare()
            ));
        }

        return comparison.toString();
    }

    /**
     * 取得指定年份銷售資料
     *
     * @param year 年份
     * @return 銷售資料清單
     */
    private List<ProductSales> getSalesDataByYear(int year) {
        Map<Integer, List<ProductSales>> salesDatabase = Map.of(
                2023, List.of(
                        new ProductSales("PD-1385", "智能手錶系列", 15000,
                                new BigDecimal("300000000"), "穿戴裝置", 35.7),
                        new ProductSales("PD-1234", "筆記型電腦 Ultra 系列", 10000,
                                new BigDecimal("800000000"), "筆記型電腦", 23.8),
                        new ProductSales("PD-1405", "智能手機 Pro 系列", 8500,
                                new BigDecimal("425000000"), "智能手機", 20.2)
                ),
                2024, List.of(
                        new ProductSales("PD-1405", "智能手機 Pro 系列", 18500,
                                new BigDecimal("925000000"), "智能手機", 28.5),
                        new ProductSales("PD-1385", "智能手錶系列", 17000,
                                new BigDecimal("510000000"), "穿戴裝置", 26.2),
                        new ProductSales("PD-1234", "筆記型電腦 Ultra 系列", 12000,
                                new BigDecimal("960000000"), "筆記型電腦", 18.5)
                )
        );

        return salesDatabase.getOrDefault(year, List.of());
    }

    /**
     * 格式化金額顯示
     *
     * @param amount 金額
     * @return 顯示字串
     */
    private String formatCurrency(BigDecimal amount) {
        if (amount.compareTo(new BigDecimal("100000000")) >= 0) {
            return String.format("NT$ %.1f億",
                    amount.divide(new BigDecimal("100000000"), 1, RoundingMode.HALF_UP)
                            .doubleValue());
        }
        return String.format("NT$ %,d", amount.intValue());
    }
}
