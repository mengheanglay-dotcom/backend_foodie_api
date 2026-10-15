package com.api.v1.bearerauth.model.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "promos", uniqueConstraints = @UniqueConstraint(name = "uk_promo_code", columnNames = "code"))
@Getter
@Setter
@NoArgsConstructor
public class Promo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long promoId;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(length = 255)
    private String description;

    // 0.10 means 10%
    @Column(nullable = false)
    private Double discountRate;

    @Column(nullable = false)
    private Double minimumSubtotal = 0.0;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    @Column(nullable = false)
    private Boolean active = true;
}
