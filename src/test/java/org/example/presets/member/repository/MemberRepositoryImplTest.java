package org.example.presets.member.repository;

import jakarta.transaction.Transactional;
import org.example.presets.member.entity.Member;
import org.example.presets.member.entity.MemberRole;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class MemberRepositoryImplTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void save() {
        Member member = Member.builder()
                .nickname("test")
                .password("test")
                .memberRole(MemberRole.NORMAL).build();
        memberRepository.save(member);
        System.out.println(member.getId());
    }
}