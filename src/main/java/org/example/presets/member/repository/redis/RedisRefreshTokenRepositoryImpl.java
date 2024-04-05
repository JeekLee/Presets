package org.example.presets.member.repository.redis;

import lombok.RequiredArgsConstructor;
import org.example.presets.member.entity.RefreshToken;
import org.example.presets.member.repository.RefreshTokenRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RedisRefreshTokenRepositoryImpl implements RefreshTokenRepository {
    private final LettuceRefreshTokenRepository lettuceRefreshTokenRepository;

    @Override
    public Optional<RefreshToken> findById(Long id) {
        return lettuceRefreshTokenRepository.findById(id.toString());
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return lettuceRefreshTokenRepository.save(refreshToken);
    }

    @Override
    public void delete(Long id) {
        lettuceRefreshTokenRepository.deleteById(id.toString());
    }
}
