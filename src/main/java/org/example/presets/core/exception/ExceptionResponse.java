package org.example.presets.core.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Builder
public class ExceptionResponse {
    private final String timeStamp = LocalDateTime.now().toString();
    private final Integer customHttpStatus;
    private final String message;

    public static ResponseEntity<ExceptionResponse> toResponseEntity(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(
                        ExceptionResponse.builder()
                                .customHttpStatus(errorCode.getCustomHttpStatusCode())
                                .message(errorCode.getMessage())
                                .build()
                );
    }
}
