package com.laurentiu.price_comparator_market.service;

import com.laurentiu.price_comparator_market.entity.Discount;
import com.laurentiu.price_comparator_market.entity.Product;
import com.laurentiu.price_comparator_market.entity.Supermarket;
import com.laurentiu.price_comparator_market.repository.DiscountRepository;
import com.laurentiu.price_comparator_market.repository.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountService {
    private final PriceRepository priceRepository;
    private final DiscountRepository discountRepository;

    public List<Map<String, Object>> getNewDiscounts() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<Discount> recentDiscounts = discountRepository.findByTimestampAfter(yesterday);
        return recentDiscounts.stream()
                .map(discount -> {
                    Map<String, Object> discountInfo = new LinkedHashMap<>();
                    discountInfo.put("id", discount.getId());
                    discountInfo.put("percentage", discount.getPercentage());
                    discountInfo.put("startDate", discount.getStartDate());
                    discountInfo.put("endDate", discount.getEndDate());

                    Product product = discount.getProduct();
                    Map<String, Object> productInfo = new LinkedHashMap<>();
                    productInfo.put("id", product.getId());
                    productInfo.put("name", product.getName());
                    discountInfo.put("product", productInfo);

                    Supermarket supermarket = discount.getSupermarket();
                    Map<String, Object> storeInfo = new LinkedHashMap<>();
                    storeInfo.put("id", supermarket.getId());
                    storeInfo.put("name", supermarket.getName());
                    discountInfo.put("supermarket", storeInfo);

                    return discountInfo;
                })
                .collect(Collectors.toList());

    }

    public List<Map<String, Object>> getBestDiscountedProducts(){
        List<Discount> latestDiscounts=discountRepository.findAllOrderByTimestampDescPercentageDesc();
        Map<Product, Float> productMaxDiscount = new LinkedHashMap<>();
        latestDiscounts.forEach(discount -> {
            productMaxDiscount.putIfAbsent(discount.getProduct(), discount.getPercentage());
        });
        return  productMaxDiscount.entrySet()
                .stream().sorted(Map.Entry.<Product, Float>comparingByValue().reversed())
                .limit(10).map(entry -> {
                            Map<String, Object> productInfo = new HashMap<>();
                            productInfo.put("product", entry.getKey());
                            productInfo.put("discountPercentage", entry.getValue());
                            return productInfo;
                        }
                )
                .collect(Collectors.toList());
    }
}
