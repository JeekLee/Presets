package org.example.presets.member.repository;

import lombok.RequiredArgsConstructor;
import org.example.presets.member.entity.Member;
import org.example.presets.member.entity.RefreshToken;
import org.example.presets.member.repository.rds.jpa.JpaMemberRepository;
import org.example.presets.member.repository.lettuce.RefreshTokenRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository{
    private final JpaMemberRepository jpaMemberRepository;
    private final RefreshTokenRepository redisRefreshTokenRepository;
    @Override
    public Member save(Member member) {
        return jpaMemberRepository.save(member);
    }

    @Override
    public Optional<Member> findById(Long id) {
        return jpaMemberRepository.findById(id);
    }

    @Override
    public Optional<Member> findByNickname(String nickname) {
        return jpaMemberRepository.findByNickname(nickname);
    }

    @Override
    public Optional<RefreshToken> findRefreshTokenById(Long id) {
        return redisRefreshTokenRepository.findById(id.toString());
    }

    @Override
    public RefreshToken saveRefreshToken(RefreshToken refreshToken) {
        return redisRefreshTokenRepository.save(refreshToken);
    }

    @Override
    public void deleteRefreshToken(Long id) {
        redisRefreshTokenRepository.deleteById(id.toString());
    }
}
