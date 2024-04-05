package org.example.presets.member.repository.mysql;

import lombok.RequiredArgsConstructor;
import org.example.presets.member.entity.Member;
import org.example.presets.member.repository.MemberRepository;
import org.example.presets.member.repository.mysql.jpa.JpaMemberRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RdsMemberRepositoryImpl implements MemberRepository {
    private final JpaMemberRepository jpaMemberRepository;
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
}
