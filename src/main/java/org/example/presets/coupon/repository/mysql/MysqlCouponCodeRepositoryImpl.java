package org.example.presets.coupon.repository.mysql;


import lombok.RequiredArgsConstructor;
import org.example.presets.coupon.entity.CouponCode;
import org.example.presets.coupon.repository.CouponCodeRepository;
import org.example.presets.coupon.repository.mysql.jdbc.JdbcCouponCodeRepository;
import org.example.presets.coupon.repository.mysql.jpa.JpaCouponCodeRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Qualifier("mysqlCouponCodeRepositoryImpl")
public class MysqlCouponCodeRepositoryImpl implements CouponCodeRepository {
    private final JdbcCouponCodeRepository jdbcCouponCodeRepository;
    private final JpaCouponCodeRepository jpaCouponCodeRepository;

    @Override
    public void saveAll(List<CouponCode> couponCodeList) {
        jdbcCouponCodeRepository.saveAll(couponCodeList);
    }

    @Override
    public Optional<CouponCode> findFirstByOrderByIdDesc() {
        return jpaCouponCodeRepository.findFirstByOrderByIdDesc();
    }
}
