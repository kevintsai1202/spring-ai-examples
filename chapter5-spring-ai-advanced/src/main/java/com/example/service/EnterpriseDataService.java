package com.example.service;

import com.example.model.Product;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

/**
 * 企業資料服務（記憶體模擬）
 */
@Service
public class EnterpriseDataService {

    /**
     * 重要資料：模擬企業產品資料庫
     */
    private final List<Product> productDatabase = buildSampleData();

    /**
     * 依年份與產品關鍵字篩選產品資料
     *
     * @param year    年份（可為空）
     * @param product 產品關鍵字（型號或名稱，可為空）
     * @return 篩選結果
     */
    public List<Product> filterProducts(String year, String product) {
        Stream<Product> stream = productDatabase.stream();

        if (StringUtils.isNotBlank(year)) {
            stream = stream.filter(item -> year.equals(item.getYear()));
        }

        if (StringUtils.isNotBlank(product)) {
            String keyword = product.toLowerCase(Locale.ROOT);
            stream = stream.filter(item ->
                    item.getModel().toLowerCase(Locale.ROOT).contains(keyword)
                            || item.getName().toLowerCase(Locale.ROOT).contains(keyword));
        }

        return stream.toList();
    }

    /**
     * 建立示範用的產品資料
     *
     * @return 模擬資料清單
     */
    private List<Product> buildSampleData() {
        List<Product> data = new ArrayList<>();

        data.add(Product.builder()
                .year("2023")
                .model("PD-1385")
                .name("智能手錶 Sport")
                .quantity(15000)
                .revenue(new BigDecimal("300000000"))
                .category("穿戴裝置")
                .build());

        data.add(Product.builder()
                .year("2023")
                .model("PD-1234")
                .name("筆記型電腦 Ultra")
                .quantity(10000)
                .revenue(new BigDecimal("800000000"))
                .category("筆記型電腦")
                .build());

        data.add(Product.builder()
                .year("2023")
                .model("PD-1405")
                .name("智能手機 Pro")
                .quantity(8500)
                .revenue(new BigDecimal("425000000"))
                .category("智能手機")
                .build());

        data.add(Product.builder()
                .year("2024")
                .model("PD-1405")
                .name("智能手機 Pro Max")
                .quantity(18500)
                .revenue(new BigDecimal("925000000"))
                .category("智能手機")
                .build());

        data.add(Product.builder()
                .year("2024")
                .model("PD-1385")
                .name("智能手錶 Ultra")
                .quantity(17000)
                .revenue(new BigDecimal("510000000"))
                .category("穿戴裝置")
                .build());

        data.add(Product.builder()
                .year("2024")
                .model("PD-1234")
                .name("筆記型電腦 Ultra")
                .quantity(12000)
                .revenue(new BigDecimal("960000000"))
                .category("筆記型電腦")
                .build());

        data.add(Product.builder()
                .year("2024")
                .model("PD-1300")
                .name("智能音箱 Home")
                .quantity(12000)
                .revenue(new BigDecimal("240000000"))
                .category("智能家居")
                .build());

        data.add(Product.builder()
                .year("2024")
                .model("PD-1255")
                .name("無線耳機 Pro")
                .quantity(15000)
                .revenue(new BigDecimal("180000000"))
                .category("穿戴裝置")
                .build());

        return data;
    }
}
