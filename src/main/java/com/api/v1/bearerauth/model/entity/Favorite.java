package com.api.v1.bearerauth.model.entity;

import jakarta.persistence.*;
import lombok.Getter; import lombok.NoArgsConstructor; import lombok.Setter;
import java.time.LocalDateTime;

@Entity @Getter @Setter @NoArgsConstructor
@Table(name="favorite", uniqueConstraints=@UniqueConstraint(columnNames={"user_id","product_id"}))
public class Favorite {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="user_id") private AppUser user;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="product_id") private Product product;
    @Column(nullable=false) private LocalDateTime createdAt;
}
