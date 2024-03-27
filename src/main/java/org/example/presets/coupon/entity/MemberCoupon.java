package org.example.presets.coupon.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.presets.member.entity.Member;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(name = "member_coupon")
public class MemberCoupon {
    @Version
    private Long version;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @Column(nullable = false)
    private Boolean isUsed;

    @Builder
    public MemberCoupon(Long id, Member member, Coupon coupon, Boolean isUsed) {
        this.id = id;
        this.member = member;
        this.coupon = coupon;
        this.isUsed = isUsed;
    }
}
