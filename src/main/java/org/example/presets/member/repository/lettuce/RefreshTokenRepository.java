package org.example.presets.member.repository.lettuce;

import org.example.presets.member.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
