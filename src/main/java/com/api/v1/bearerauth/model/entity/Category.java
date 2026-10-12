package com.api.v1.bearerauth.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "category", uniqueConstraints = {@UniqueConstraint(name = "uk_category_name", columnNames = "category_name")})
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(name = "category_name", nullable = false)
    private String categoryName;

    @Column(length = 1000)
    private String description;

    private String imageUrl;

    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();

}
