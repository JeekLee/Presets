package org.example.presets.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false, unique = true)
    private String nickname;

    @Column (nullable = false)
    private String password;

    @Enumerated(value = EnumType.STRING)
    @Column (nullable = false)
    private MemberRole memberRole = MemberRole.NORMAL;

    @Builder
    public Member(Long id, String nickname, String password, MemberRole memberRole) {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
        this.memberRole = memberRole;
    }
}
