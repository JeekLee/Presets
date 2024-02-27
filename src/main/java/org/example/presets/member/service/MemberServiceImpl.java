package org.example.presets.member.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.presets.core.exception.global.CustomException;
import org.example.presets.core.exception.global.NotFoundException;
import org.example.presets.core.security.JwtUtil;
import org.example.presets.member.entity.Member;
import org.example.presets.member.entity.MemberRole;
import org.example.presets.member.entity.RefreshToken;
import org.example.presets.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.example.presets.core.exception.ErrorCode.*;
import static org.example.presets.core.exception.global.Domain.MEMBER;
import static org.example.presets.core.exception.global.Layer.SERVICE;
import static org.example.presets.core.security.JwtUtil.HEADER_ACCESS;
import static org.example.presets.core.security.JwtUtil.HEADER_REFRESH;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    private void setNewTokens(HttpServletResponse httpServletResponse, Member member) {
        String accessTokenStr = jwtUtil.createAccessToken(member.getId(), member.getMemberRole());
        String refreshTokenStr = jwtUtil.createRefreshToken(member.getId());

        RefreshToken refreshToken
                = memberRepository.saveRefreshToken(
                        RefreshToken.builder().id(member.getId()).token(refreshTokenStr).build());

        httpServletResponse.addHeader(HEADER_ACCESS, accessTokenStr);
        httpServletResponse.addHeader(HEADER_REFRESH, refreshToken.getToken());
    }

    @Override
    @Transactional
    public void signUp(String nickname, String password) {
        memberRepository.findByNickname(nickname).ifPresent(member -> {
            throw new CustomException(MEMBER, SERVICE, DUPLICATED_NICKNAME, nickname);
        });

        Member member = Member.builder()
                .nickname(nickname)
                .password(passwordEncoder.encode(password))
                .memberRole(MemberRole.NORMAL)
                .build();

        memberRepository.save(member);
    }

    @Override
    @Transactional(readOnly = true)
    public void logIn(String nickname, String password, HttpServletResponse httpServletResponse) {
        Member member = memberRepository.findByNickname(nickname).orElseThrow(
                () -> new NotFoundException(MEMBER, SERVICE, MEMBER_NOT_FOUND, nickname));

        if(!passwordEncoder.matches(password, member.getPassword())){
            throw new CustomException(MEMBER, SERVICE, PASSWORD_INCORRECT, null);
        }

        setNewTokens(httpServletResponse, member);
    }

    @Override
    public void logOut(Long memberId) {
        memberRepository.deleteRefreshToken(memberId);
    }

    @Transactional
    public void tokenReissuance(HttpServletRequest request, HttpServletResponse response) {
        Claims refreshTokenInfo = jwtUtil.getLoginMemberInfoFromHttpServletRequest(request, false);
        Long memberId = Long.parseLong(refreshTokenInfo.getSubject());

        RefreshToken refreshToken = memberRepository.findRefreshTokenById(memberId).orElseThrow(
                () -> new CustomException(MEMBER, SERVICE, REFRESHTOKEN_NOT_EXIST, "in Redis Server"));

        if (!refreshToken.getToken().substring(7).equals(jwtUtil.resolveToken(request, HEADER_REFRESH))) {
            throw new CustomException(MEMBER, SERVICE, INVALID_JWT, null);
        }

        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new NotFoundException(MEMBER, SERVICE, MEMBER_NOT_FOUND, memberId));

        setNewTokens(response, member);
    }
}

