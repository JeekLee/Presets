package org.example.presets.member.repository.redis;

import org.example.presets.member.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface LettuceRefreshTokenRepository extends CrudRepository<RefreshToken, String> {

}
