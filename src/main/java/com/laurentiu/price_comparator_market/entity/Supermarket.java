package com.laurentiu.price_comparator_market.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Supermarket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "supermarket" )
    private List<Price> productPrices;
}
