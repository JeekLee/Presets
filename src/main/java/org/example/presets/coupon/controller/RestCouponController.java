package org.example.presets.coupon.controller;

import lombok.RequiredArgsConstructor;
import org.example.presets.core.exception.global.CustomGlobalException;
import org.example.presets.core.security.CustomUserDetails;
import org.example.presets.coupon.dto.CreateCouponDto;
import org.example.presets.coupon.service.CouponService;
import org.example.presets.member.entity.MemberRole;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.example.presets.core.exception.global.Domain.COUPON;
import static org.example.presets.core.exception.global.GlobalErrorCode.NOT_ENOUGH_AUTH;
import static org.example.presets.core.exception.global.Layer.CONTROLLER;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class RestCouponController {
    @Qualifier("couponServiceImpl")
    private final CouponService couponService;

    @PostMapping("")
    public ResponseEntity<?> createCoupon(
            @RequestBody CreateCouponDto createCouponDto, @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (!userDetails.memberRole().equals(MemberRole.ADMIN)) {
            throw new CustomGlobalException(COUPON, CONTROLLER, NOT_ENOUGH_AUTH, userDetails.memberRole());
        }
        couponService.createCoupon(createCouponDto.getCouponName(), createCouponDto.getCouponQuantity());
        return ResponseEntity.ok().build();
    }

}
