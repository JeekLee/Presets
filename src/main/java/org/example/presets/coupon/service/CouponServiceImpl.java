package org.example.presets.coupon.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.presets.aop.lock.DistributedLock;
import org.example.presets.coupon.entity.Coupon;
import org.example.presets.coupon.entity.CouponCode;
import org.example.presets.coupon.repository.CouponCodeRepository;
import org.example.presets.coupon.repository.CouponRepository;
import org.example.presets.coupon.util.CodeGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Qualifier("couponServiceImpl")
public class CouponServiceImpl implements CouponService {
    @Qualifier("mysqlCouponRepositoryImpl")
    private final CouponRepository couponRepository;
    @Qualifier("mysqlCouponCodeRepositoryImpl")
    private final CouponCodeRepository couponCodeRepository;

    @Override
//    @DistributedLock(key = "'CreateCoupon'")
    @Transactional
    public void createCoupon(String couponName, Long couponQuantity) {
        Coupon coupon = couponRepository.save(
                Coupon.builder()
                        .name(couponName)
                        .totalQuantity(couponQuantity)
                        .quantity(couponQuantity)
                        .build()
        );

        List<CouponCode> couponCodeList = new ArrayList<>();

        Long tmp = 0L;
        Optional<CouponCode> firstCouponCode = couponCodeRepository.findFirstByOrderByIdDesc();

        if (firstCouponCode.isPresent()) {
            tmp = firstCouponCode.get().getId();
        }

        for (int i = 0; i < couponQuantity; i++) {
            couponCodeList.add(
                    CouponCode.builder()
                            .code(CodeGenerator.generateCodeById(tmp + i))
                            .coupon(coupon)
                            .build()
            );
        }

        couponCodeRepository.saveAll(couponCodeList);
    }
}
