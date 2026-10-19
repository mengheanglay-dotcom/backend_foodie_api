package com.api.v1.bearerauth.model.entity;


import com.api.v1.bearerauth.model.enumerate.OptionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_option")
public class ProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long optionId;

    @Column(nullable = false)
    private String optionName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OptionType optionType;

    // For SIZE: final unit selling price of this size. For ADD_ON, leave null.
    @Column
    private Double price;

    // For ADD_ON: price added to the selected size. Kept non-null for compatibility with existing schema.
    @Column(nullable = false)
    private Double extraPrice;

    @Column(nullable = false)
    private Boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
