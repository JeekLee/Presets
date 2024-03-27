package org.example.presets.coupon.repository;

import lombok.RequiredArgsConstructor;
import org.example.presets.coupon.entity.Coupon;
import org.example.presets.coupon.entity.CouponCode;
import org.example.presets.coupon.repository.rds.jpa.JpaCouponCodeRepository;
import org.example.presets.coupon.repository.rds.jpa.JpaCouponRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository{
    private final JpaCouponRepository jpaCouponRepository;
    private final JpaCouponCodeRepository jpaCouponCodeRepository;
    @Override
    public Coupon save(Coupon coupon) {
        return jpaCouponRepository.save(coupon);
    }

    @Override
    public CouponCode saveAll(List<CouponCode> couponCodeList) {
        return null;
    }

    @Override
    public Optional<CouponCode> findCouponCodeByCode(String code) {
        return jpaCouponCodeRepository.findByCode(code);
    }
}
