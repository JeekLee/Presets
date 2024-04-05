package org.example.presets.coupon.repository.mysql.jdbc;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.presets.coupon.entity.CouponCode;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcCouponCodeRepository {
    private final int defaultBatchSize = 1000;
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAll(List<CouponCode> couponCodeList) {
        String sql = "INSERT INTO coupon_code (coupon_id, code) " + "VALUES (?, ?)";

        jdbcTemplate.batchUpdate(
                sql,
                couponCodeList,
                defaultBatchSize,
                (PreparedStatement ps, CouponCode couponCode) -> {
                    ps.setLong(1, couponCode.getCoupon().getId());
                    ps.setString(2, couponCode.getCode());
                }
        );
    }
}
