package org.example.presets.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Member
    INVALID_MEMBER_INFO(HttpStatus.BAD_REQUEST, "유효하지 않은 계정정보입니다.", 4000),
    PASSWORD_INCORRECT(HttpStatus.BAD_REQUEST, "계정 정보가 일치하지 않습니다.", 4000),
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "중복된 이메일입니다.", 4090),
    DUPLICATED_NICKNAME(HttpStatus.BAD_REQUEST, "중복된 닉네임입니다.", 4090),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다.", 4040),

    // JWT
    ACCESSTOKEN_NOT_EXIST(HttpStatus.UNAUTHORIZED, "Access Token이 존재하지 않습니다.", 4011),
    REFRESHTOKEN_NOT_EXIST(HttpStatus.UNAUTHORIZED, "Refresh Token이 존재하지 않습니다.", 4012),
    INVALID_JWT(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.", 4014),
    UNSUPPORTED_JWT(HttpStatus.UNAUTHORIZED, "지원되지 않는 토큰입니다.", 4015),
    EXPIRED_JWT(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다.", 4016),
    MEMBER_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "접근 권한이 없는 사용자입니다.", 4031),
    ;

    private final HttpStatus httpStatus;
    private final String message;
    private final Integer customHttpStatusCode;
}
