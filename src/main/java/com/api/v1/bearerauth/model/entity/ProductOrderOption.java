
package com.api.v1.bearerauth.model.entity;

import com.api.v1.bearerauth.model.enumerate.OptionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_order_option")
public class ProductOrderOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String optionName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OptionType optionType;

    // Snapshot of final size price (SIZE options only).
    @Column
    private Double price;

    // Snapshot of the add-on charge; zero for SIZE options.
    @Column(nullable = false)
    private Double extraPrice;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_order_id", nullable = false)
    private ProductOrder productOrder;
}
