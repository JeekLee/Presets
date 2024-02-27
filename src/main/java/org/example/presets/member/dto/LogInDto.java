package org.example.presets.member.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LogInDto {
    @NotEmpty
    @Pattern(regexp = "^[0-9a-zA-Z]{" + 8 + "," + 15 + "}$")
    private String nickname;

    @NotEmpty
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{" + 8 + "," + 15 + "}$")
    private String password;

    @Builder
    public LogInDto(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }
}
