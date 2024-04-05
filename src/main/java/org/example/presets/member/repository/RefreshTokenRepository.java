package org.example.presets.member.repository;

import org.example.presets.member.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {
    Optional<RefreshToken> findById(Long id);
    RefreshToken save(RefreshToken refreshToken);
    void delete(Long id);
}
