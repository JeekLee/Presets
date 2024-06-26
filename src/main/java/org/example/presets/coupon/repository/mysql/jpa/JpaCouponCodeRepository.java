package org.example.presets.coupon.repository.mysql.jpa;

import org.example.presets.coupon.entity.CouponCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaCouponCodeRepository extends JpaRepository<CouponCode, Long> {
    Optional<CouponCode> findByCode(String code);

    Optional<CouponCode> findFirstByOrderByIdDesc();
}
