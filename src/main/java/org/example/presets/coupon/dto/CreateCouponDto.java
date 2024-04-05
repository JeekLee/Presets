package org.example.presets.coupon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateCouponDto {
    @JsonProperty(value = "coupon_name")
    private final String couponName;
    @JsonProperty(value = "coupon_quantity")
    private final Long couponQuantity;

    @Builder
    public CreateCouponDto(String couponName, Long couponQuantity) {
        this.couponName = couponName;
        this.couponQuantity = couponQuantity;
    }
}
