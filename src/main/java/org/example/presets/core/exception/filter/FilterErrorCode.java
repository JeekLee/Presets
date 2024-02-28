package org.example.presets.core.exception.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FilterErrorCode {
    // Security
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근이 거부되었습니다.", 4030),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "인가되지 않은 사용자입니다.", 4010),

    // JWT
    ACCESSTOKEN_NOT_EXIST(HttpStatus.UNAUTHORIZED, "Access Token이 존재하지 않습니다.", 4011),
    REFRESHTOKEN_NOT_EXIST(HttpStatus.UNAUTHORIZED, "Refresh Token이 존재하지 않습니다.", 4012),
    INVALID_JWT(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.", 4014),
    UNSUPPORTED_JWT(HttpStatus.UNAUTHORIZED, "지원되지 않는 토큰입니다.", 4015),
    EXPIRED_JWT(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다.", 4016),
    ;

    private final HttpStatus httpStatus;
    private final String message;
    private final Integer customHttpStatusCode;
}
