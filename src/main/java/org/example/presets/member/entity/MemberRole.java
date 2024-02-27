package org.example.presets.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberRole {
    ADMIN("ADMIN"), NORMAL("NORMAL");

    private final String code;
}
