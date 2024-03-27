package org.example.presets.coupon.repository.rds.jpa;

import org.example.presets.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCouponRepository extends JpaRepository<Coupon, Long> {

}
