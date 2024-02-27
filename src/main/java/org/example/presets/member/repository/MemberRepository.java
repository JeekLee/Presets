package org.example.presets.member.repository;

import org.example.presets.member.entity.Member;
import org.example.presets.member.entity.RefreshToken;

import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(Long id);
    Optional<Member> findByNickname(String nickname);
    RefreshToken saveRefreshToken(RefreshToken refreshToken);
    void deleteRefreshToken(Long id);
}
