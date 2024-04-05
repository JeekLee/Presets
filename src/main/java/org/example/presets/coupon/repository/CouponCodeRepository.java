package org.example.presets.coupon.repository;

import org.example.presets.coupon.entity.CouponCode;

import java.util.List;
import java.util.Optional;

public interface CouponCodeRepository {
    void saveAll(List<CouponCode> couponCodeList);
    Optional<CouponCode> findFirstByOrderByIdDesc();
}
