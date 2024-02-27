package org.example.presets.member.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
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

import static org.example.presets.core.exception.ErrorCode.*;
import static org.example.presets.core.exception.global.Domain.MEMBER;
import static org.example.presets.core.exception.global.Layer.SERVICE;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
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
    public void logIn(String nickname, String password, HttpServletResponse httpServletResponse) {
        Member member = memberRepository.findByNickname(nickname).orElseThrow(
                () -> new NotFoundException(MEMBER, SERVICE, MEMBER_NOT_FOUND, nickname)
        );
        if(!passwordEncoder.matches(password, member.getPassword())){
            throw new CustomException(MEMBER, SERVICE, PASSWORD_INCORRECT, null);
        }
        String accessToken = jwtUtil.createAccessToken(member.getId(), member.getMemberRole());
        String refreshToken = jwtUtil.createRefreshToken(member.getId());
        httpServletResponse.addHeader(JwtUtil.HEADER_ACCESS, accessToken);
        httpServletResponse.addHeader(JwtUtil.HEADER_REFRESH, refreshToken);

        memberRepository.saveRefreshToken(RefreshToken.builder().id(member.getId()).token(refreshToken).build());
    }

    @Override
    public void logOut(Long memberId) {
        memberRepository.deleteRefreshToken(memberId);
    }
}
