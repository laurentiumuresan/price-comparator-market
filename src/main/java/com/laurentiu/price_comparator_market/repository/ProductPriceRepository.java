package com.laurentiu.price_comparator_market.repository;

import com.laurentiu.price_comparator_market.entity.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductPriceRepository extends JpaRepository <ProductPrice, Long> {
}
