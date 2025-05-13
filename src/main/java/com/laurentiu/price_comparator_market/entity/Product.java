package com.laurentiu.price_comparator_market.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Product {
    @Id
    private String id;

    private String name;
    private String brand;
    private String category;
    private float packageQuantity;
    private String packageUnit;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Price> prices;
}
