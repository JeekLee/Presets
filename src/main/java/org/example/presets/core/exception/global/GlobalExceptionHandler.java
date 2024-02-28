package org.example.presets.core.exception.global;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.presets.core.exception.ExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.example.presets.core.exception.global.GlobalErrorCode.INVALID_MEMBER_INFO;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException e) {
        log.error("NotFoundException throwed at " + e.getDomain() + "_"+ e.getLayer() + " : " + e.getGlobalErrorCode());
        log.error("Cause : " + e.getCauseVariable());
        return ExceptionResponse.toResponseEntity(e.getGlobalErrorCode());
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleException(CustomGlobalException e) {
        log.error("NotFoundException throwed at " + e.getDomain() + "_"+ e.getLayer() + " : " + e.getGlobalErrorCode());
        log.error("Cause : " + e.getCauseVariable());
        return ExceptionResponse.toResponseEntity(e.getGlobalErrorCode());
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleValidationException(MethodArgumentNotValidException e) {
        e.getBindingResult().getFieldErrors().forEach(fieldError
                -> log.error("Invalid {} value submitted for {}", fieldError.getRejectedValue(), fieldError.getField()));
        return ExceptionResponse.toResponseEntity(INVALID_MEMBER_INFO);
    }
}
