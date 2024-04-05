package org.example.presets.coupon.repository.mysql;

import lombok.RequiredArgsConstructor;
import org.example.presets.coupon.entity.Coupon;
import org.example.presets.coupon.entity.CouponCode;
import org.example.presets.coupon.repository.CouponRepository;
import org.example.presets.coupon.repository.mysql.jpa.JpaCouponCodeRepository;
import org.example.presets.coupon.repository.mysql.jpa.JpaCouponRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Qualifier("mysqlCouponRepositoryImpl")
public class MysqlCouponRepositoryImpl implements CouponRepository {
    private final JpaCouponRepository jpaCouponRepository;
    @Override
    public Coupon save(Coupon coupon) {
        return jpaCouponRepository.save(coupon);
    }
}
