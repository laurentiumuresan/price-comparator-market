package com.laurentiu.price_comparator_market.repository;

import com.laurentiu.price_comparator_market.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository <Discount, Long> {
}
