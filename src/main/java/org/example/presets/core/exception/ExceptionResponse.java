package org.example.presets.core.exception;

import lombok.Builder;
import lombok.Getter;
import org.example.presets.core.exception.global.GlobalErrorCode;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Builder
public class ExceptionResponse {
    private final String timeStamp = LocalDateTime.now().toString();
    private final Integer customHttpStatus;
    private final String message;

    public static ResponseEntity<ExceptionResponse> toResponseEntity(GlobalErrorCode globalErrorCode) {
        return ResponseEntity
                .status(globalErrorCode.getHttpStatus())
                .body(
                        ExceptionResponse.builder()
                                .customHttpStatus(globalErrorCode.getCustomHttpStatusCode())
                                .message(globalErrorCode.getMessage())
                                .build()
                );
    }
}
