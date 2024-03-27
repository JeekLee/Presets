package org.example.presets.member.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(name = "member")
public class Member {
    @Version
    private Long version;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
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
