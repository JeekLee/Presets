package org.example.presets.coupon.repository;

import org.example.presets.coupon.entity.Coupon;
import org.example.presets.coupon.entity.CouponCode;

import java.util.List;
import java.util.Optional;

public interface CouponRepository {
    Coupon save(Coupon coupon);

    CouponCode saveAll(List<CouponCode> couponCodeList);

    Optional<CouponCode> findCouponCodeByCode(String code);
}
