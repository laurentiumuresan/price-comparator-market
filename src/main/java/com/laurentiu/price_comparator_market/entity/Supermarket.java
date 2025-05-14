package com.laurentiu.price_comparator_market.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Supermarket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "supermarket", cascade = CascadeType.ALL)
    private List<Price> prices = new ArrayList<>();
}