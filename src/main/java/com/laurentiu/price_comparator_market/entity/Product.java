package com.laurentiu.price_comparator_market.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Product {
    @Id
    private String id;

    private String name;
    private String category;
    private String brand;
    private float packageQuantity;
    private String packageUnit;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Price> prices = new ArrayList<>();
}