package com.laurentiu.price_comparator_market.repository;

import com.laurentiu.price_comparator_market.entity.PriceAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceAlertRepository extends JpaRepository<PriceAlert, Long> {
    List<PriceAlert> findByIsActiveTrue();
}
