package org.example.presets.coupon.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateCouponDto {
    private final String couponName;
    private final Long couponQuantity;

    @Builder
    public CreateCouponDto(String couponName, Long couponQuantity) {
        this.couponName = couponName;
        this.couponQuantity = couponQuantity;
    }
}
