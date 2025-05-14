package com.laurentiu.price_comparator_market.repository;

import com.laurentiu.price_comparator_market.entity.Supermarket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupermarketRepository extends JpaRepository <Supermarket, Long>{
    Optional<Supermarket> findByName(String name);
}
