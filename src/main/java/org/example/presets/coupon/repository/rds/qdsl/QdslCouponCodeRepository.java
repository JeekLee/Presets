package org.example.presets.coupon.repository.rds.qdsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QdslCouponCodeRepository {
    private final JPAQueryFactory queryFactory;
}
