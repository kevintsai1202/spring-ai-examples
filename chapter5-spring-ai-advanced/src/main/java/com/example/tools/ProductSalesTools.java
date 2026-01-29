package com.example.tools;

import com.example.model.CategoryStat;
import com.example.model.Product;
import com.example.model.ProductSalesResponse;
import com.example.model.SalesStatistics;
import com.example.service.EnterpriseDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 企業產品銷售工具
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ProductSalesTools {

    /**
     * 重要物件：企業資料服務
     */
    private final EnterpriseDataService dataService;

    /**
     * 查詢產品銷售資料
     *
     * @param year    查詢年份
     * @param product 產品關鍵字（型號或名稱）
     * @return 產品銷售回應
     */
    @Tool(description = "Get company product sales information by year and product model. "
            + "Can filter by year (e.g., '2023', '2024') and product (model or name). "
            + "Returns detailed sales data including quantity, revenue, and product information.")
    public ProductSalesResponse getProductSales(String year, String product) {
        log.info("查詢產品銷售：年份={}, 產品={}", year, product);

        List<Product> products = dataService.filterProducts(year, product);

        int totalQuantity = products.stream()
                .mapToInt(Product::getQuantity)
                .sum();

        BigDecimal totalRevenue = products.stream()
                .map(Product::getRevenue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return ProductSalesResponse.builder()
                .products(products)
                .totalQuantity(totalQuantity)
                .totalRevenue(totalRevenue)
                .queryYear(year)
                .queryProduct(product)
                .build();
    }

    /**
     * 獲取銷售統計分析
     *
     * @param year 查詢年份
     * @return 統計分析結果
     */
    @Tool(description = "Get sales statistics and analysis for a specific year. "
            + "Returns top-selling products, category breakdown, and growth metrics.")
    public SalesStatistics getSalesStatistics(String year) {
        List<Product> products = dataService.filterProducts(year, null);

        List<CategoryStat> categoryStats = products.stream()
                .collect(Collectors.groupingBy(Product::getCategory))
                .entrySet()
                .stream()
                .map(entry -> buildCategoryStat(entry.getKey(), entry.getValue(), products))
                .toList();

        List<Product> topProducts = products.stream()
                .sorted(Comparator.comparing(Product::getQuantity).reversed())
                .limit(5)
                .toList();

        return SalesStatistics.builder()
                .topProducts(topProducts)
                .categoryStats(categoryStats)
                .build();
    }

    /**
     * 比較不同年份的銷售表現
     *
     * @param year1 年份一
     * @param year2 年份二
     * @return 比較結果文字
     */
    @Tool(description = "Compare sales performance between two years. "
            + "Returns detailed comparison including growth rates and product performance changes.")
    public String compareSalesByYear(String year1, String year2) {
        List<Product> products1 = dataService.filterProducts(year1, null);
        List<Product> products2 = dataService.filterProducts(year2, null);

        int total1 = products1.stream().mapToInt(Product::getQuantity).sum();
        int total2 = products2.stream().mapToInt(Product::getQuantity).sum();

        BigDecimal revenue1 = products1.stream()
                .map(Product::getRevenue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal revenue2 = products2.stream()
                .map(Product::getRevenue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        double quantityGrowth = calculateGrowthRate(total1, total2);
        double revenueGrowth = calculateGrowthRate(revenue1, revenue2);

        return String.format(
                "年度銷售比較分析 (%s vs %s):\n"
                        + "銷售數量：%d → %d (成長率：%.2f%%)\n"
                        + "銷售金額：%s → %s (成長率：%.2f%%)\n"
                        + "產品數量：%d → %d\n",
                year1, year2,
                total1, total2, quantityGrowth,
                formatCurrency(revenue1), formatCurrency(revenue2), revenueGrowth,
                products1.size(), products2.size());
    }

    /**
     * 組裝分類統計資訊
     *
     * @param category 分類
     * @param products 分類產品
     * @param all      全部產品
     * @return 分類統計
     */
    private CategoryStat buildCategoryStat(String category, List<Product> products, List<Product> all) {
        int quantity = products.stream()
                .mapToInt(Product::getQuantity)
                .sum();

        BigDecimal revenue = products.stream()
                .map(Product::getRevenue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        double percentage = calculatePercentage(quantity, all);

        return CategoryStat.builder()
                .category(category)
                .quantity(quantity)
                .revenue(revenue)
                .percentage(percentage)
                .build();
    }

    /**
     * 計算占比
     *
     * @param value   分類數量
     * @param allData 全部資料
     * @return 百分比
     */
    private double calculatePercentage(int value, List<Product> allData) {
        int total = allData.stream().mapToInt(Product::getQuantity).sum();
        if (total == 0) {
            return 0.0;
        }
        return BigDecimal.valueOf((double) value / total * 100)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    /**
     * 計算成長率（整數）
     *
     * @param oldValue 舊值
     * @param newValue 新值
     * @return 成長率百分比
     */
    private double calculateGrowthRate(int oldValue, int newValue) {
        if (oldValue == 0) {
            return 0.0;
        }
        return BigDecimal.valueOf(((double) newValue - oldValue) / oldValue * 100)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    /**
     * 計算成長率（BigDecimal）
     *
     * @param oldValue 舊值
     * @param newValue 新值
     * @return 成長率百分比
     */
    private double calculateGrowthRate(BigDecimal oldValue, BigDecimal newValue) {
        if (oldValue.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        BigDecimal diff = newValue.subtract(oldValue);
        return diff.divide(oldValue, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"))
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
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
