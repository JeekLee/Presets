package org.example.presets.coupon.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.presets.member.entity.Member;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Table(name = "coupon_code")
@NoArgsConstructor(access = PROTECTED)
public class CouponCode {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", referencedColumnName = "id", nullable = false)
    private Coupon coupon;

    @Column(name = "code", nullable = false, unique = true)
    @Size(max=16)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member = null;

    @Builder
    public CouponCode(Long id, Coupon coupon, String code, Member member) {
        this.id = id;
        this.coupon = coupon;
        this.code = code;
        this.member = member;
    }
}
