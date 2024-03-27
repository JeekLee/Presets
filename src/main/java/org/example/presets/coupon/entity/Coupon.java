package org.example.presets.coupon.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(name = "coupon")
public class Coupon {
    @Version
    private Long version;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long quantity;

    @Column(nullable = false, updatable = false)
    private Long totalQuantity;

    @Builder
    public Coupon(Long id, String name, Long quantity, Long totalQuantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.totalQuantity = totalQuantity;
    }
}
