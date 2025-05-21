package com.laurentiu.price_comparator_market.repository;

import com.laurentiu.price_comparator_market.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository <Product, String> {
    Optional<Product> findByNameAndBrand(String name, String brand);
}
