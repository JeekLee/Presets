package org.example.presets.core.exception.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.presets.core.exception.ErrorCode;

@Getter
@AllArgsConstructor
public class CustomJwtException extends SecurityException{
    private final ErrorCode errorCode;
}
