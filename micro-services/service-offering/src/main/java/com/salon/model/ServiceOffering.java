package com.salon.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ServiceOffering {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int duration;

    @Column(nullable = false, name = "salon_id")
    private Long salonId;

    @Column(nullable = false, name = "category_id")
    private Long categoryId;

    private String image;
}
