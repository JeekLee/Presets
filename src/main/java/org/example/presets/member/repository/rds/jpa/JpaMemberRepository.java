package org.example.presets.member.repository.rds.jpa;

import org.example.presets.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaMemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByNickname(String nickname);
}

